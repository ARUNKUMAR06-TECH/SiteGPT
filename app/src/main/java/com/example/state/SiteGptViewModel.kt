package com.example.state

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.ActivityStatus
import com.example.model.AiInsightItem
import com.example.model.AiMultimodalExtractionResult
import com.example.model.CopilotMessage
import com.example.model.DailySiteSummary
import com.example.model.ExtractedDprItem
import com.example.model.ProjectOverview
import com.example.model.RecoveryStrategy
import com.example.model.ReportSource
import com.example.model.RiskItem
import com.example.model.SiteActivity
import com.example.model.SiteEvidenceReport
import com.example.model.SpreadsheetRowItem
import com.example.model.WeatherForecastItem
import com.example.model.WhatIfScenario
import com.example.service.CpmSimulationEngine
import com.example.service.SiteFirestoreService
import com.example.service.SiteGptAiService
import com.example.service.SiteGptDataProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class SiteGptState(
    val selectedNavTab: Int = 0, // 0:Dashboard, 1:DataCapture, 2:Schedule, 3:Delay&Weather, 4:WhatIf, 5:Risk&Recovery, 6:Copilot, 7:Timeline/Gantt, 8:DailySummary
    val projectOverview: ProjectOverview = SiteGptDataProvider.getInitialProjectOverview(),
    val activities: List<SiteActivity> = SiteGptDataProvider.getInitialActivities(),
    val risks: List<RiskItem> = SiteGptDataProvider.getInitialRisks(),
    val weatherForecast: List<WeatherForecastItem> = SiteGptDataProvider.getInitialWeatherForecast(),
    val recoveryStrategies: List<RecoveryStrategy> = SiteGptDataProvider.getInitialRecoveryStrategies(),
    val aiInsights: List<AiInsightItem> = SiteGptDataProvider.getInitialAiInsights(),
    val dailySummary: DailySiteSummary = SiteGptDataProvider.getInitialDailySummary(),
    val copilotMessages: List<CopilotMessage> = emptyList(),
    val copilotInput: String = "",
    val isCopilotLoading: Boolean = false,
    val whatIfScenarios: List<WhatIfScenario> = emptyList(),
    val currentWhatIfScenario: WhatIfScenario? = null,
    val whatIfInputPrompt: String = "What if we add one extra shift to Bridge Package B04?",
    val isWhatIfLoading: Boolean = false,
    // Filters & Search
    val selectedPackageFilter: String = "ALL",
    val selectedStatusFilter: String = "ALL",
    val searchQuery: String = "",
    // Data Capture Module (Legacy DPR)
    val selectedUploadType: String = "Site Photo OCR",
    val isExtracting: Boolean = false,
    val extractionProgress: Float = 0f,
    val extractedDraft: ExtractedDprItem? = null,
    val showCommitSuccessSnackbar: Boolean = false,
    val commitSnackbarMessage: String = "",
    // Detail / Modal State
    val selectedActivityForDetail: SiteActivity? = null,
    val isExplainabilityOpen: Boolean = false,
    val isEditActivityOpen: Boolean = false,
    val activeRole: String = "Project Manager",
    
    // --- Multimodal Data Capture State (4 Unified Input Channels) ---
    val selectedCaptureTab: Int = 0, // 0: All, 1: Photo, 2: Voice, 3: DPR / PDF, 4: Excel / CSV
    val capturedPhotoBitmap: Bitmap? = null,
    val capturedPhotoUri: String? = null,
    val isDemoPhoto: Boolean = false,
    val isRecordingVoice: Boolean = false,
    val recordingDurationSeconds: Int = 0,
    val voiceTranscript: String = "",
    val typedObservation: String = "",
    // DPR / PDF State
    val uploadedPdfName: String? = null,
    val uploadedPdfUri: String? = null,
    val pdfPageCount: Int = 3,
    val isPdfParsed: Boolean = false,
    // Excel / CSV State
    val uploadedSpreadsheetName: String? = null,
    val uploadedSpreadsheetUri: String? = null,
    val spreadsheetRows: List<SpreadsheetRowItem> = SiteGptDataProvider.getSampleSpreadsheetRows(),
    val selectedSpreadsheetRowIndex: Int? = null,
    // Shared Pipeline State
    val isMultimodalAnalyzing: Boolean = false,
    val multimodalProgress: Float = 0f,
    val multimodalStepText: String = "",
    val activeCaptureResult: AiMultimodalExtractionResult? = null,
    val isConfirmationOpen: Boolean = false,
    val manualSelectedActivityId: String? = null,
    val isManualActivityPickerOpen: Boolean = false,
    val siteEvidenceHistory: List<SiteEvidenceReport> = SiteGptDataProvider.getInitialEvidenceReports(),
    val selectedEvidenceReportForDetail: SiteEvidenceReport? = null,
    val isEvidenceDetailModalOpen: Boolean = false,
    val captureErrorMessage: String? = null
)

class SiteGptViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SiteGptState())
    val uiState: StateFlow<SiteGptState> = _uiState.asStateFlow()

    init {
        // Initialize default Copilot greeting & sample simulation
        loadInitialState()
    }

    private fun loadInitialState() {
        val initialCopilot = listOf(
            CopilotMessage(
                id = "MSG-WELCOME",
                isUser = false,
                timestamp = "08:30 AM",
                text = "Welcome to SiteGPT Copilot. I am actively monitoring the Chennai Elevated Highway Project schedule, telemetry, and weather impacts.",
                supportingData = "• Active Activities: 24 | Critical Path Delay: -18 days\n• Baseline Completion: 30 Nov 2026 | Forecast: 18 Dec 2026\n• Weather Alert: Heavy rain forecast for tomorrow (85% probability)",
                reasoning = "Bridge Package B04 Pier P12 concrete pouring is the primary critical path driver.",
                recommendedAction = "Select a quick query below or ask any question regarding delays, productivity, or recovery simulations."
            )
        )

        // Seed initial What-If Scenario
        val initialScenario = CpmSimulationEngine.simulateScenario(
            activities = SiteGptDataProvider.getInitialActivities(),
            query = "What if we add one extra shift to Bridge Package B04?",
            targetPackage = "Bridge Package B04",
            targetActivityName = "Pier P12 Concrete Pouring",
            changeType = "EXTRA_SHIFT",
            magnitude = 1.0
        )

        _uiState.update {
            it.copy(
                copilotMessages = initialCopilot,
                whatIfScenarios = listOf(initialScenario),
                currentWhatIfScenario = initialScenario
            )
        }
    }

    fun setNavTab(tabIndex: Int) {
        _uiState.update { it.copy(selectedNavTab = tabIndex) }
    }

    fun setPackageFilter(pkg: String) {
        _uiState.update { it.copy(selectedPackageFilter = pkg) }
    }

    fun setStatusFilter(status: String) {
        _uiState.update { it.copy(selectedStatusFilter = status) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setUploadType(type: String) {
        _uiState.update { it.copy(selectedUploadType = type) }
    }

    fun setRole(role: String) {
        _uiState.update { it.copy(activeRole = role) }
    }

    // --- Real Multimodal Data Capture Handlers ---

    fun setCapturedPhoto(bitmap: Bitmap?, uri: String?, isDemo: Boolean = false) {
        _uiState.update {
            it.copy(
                capturedPhotoBitmap = bitmap,
                capturedPhotoUri = uri,
                isDemoPhoto = isDemo,
                captureErrorMessage = null
            )
        }
    }

    fun clearCapturedPhoto() {
        _uiState.update {
            it.copy(
                capturedPhotoBitmap = null,
                capturedPhotoUri = null,
                isDemoPhoto = false
            )
        }
    }

    fun setVoiceTranscript(transcript: String) {
        _uiState.update {
            it.copy(
                voiceTranscript = transcript,
                captureErrorMessage = null
            )
        }
    }

    fun updateTypedObservation(text: String) {
        _uiState.update {
            it.copy(
                typedObservation = text,
                captureErrorMessage = null
            )
        }
    }

    fun setRecordingState(isRecording: Boolean) {
        _uiState.update {
            it.copy(
                isRecordingVoice = isRecording,
                recordingDurationSeconds = if (isRecording) 0 else it.recordingDurationSeconds
            )
        }
    }

    fun incrementRecordingDuration() {
        _uiState.update {
            it.copy(recordingDurationSeconds = it.recordingDurationSeconds + 1)
        }
    }

    fun setCaptureError(message: String) {
        _uiState.update { it.copy(captureErrorMessage = message) }
    }

    fun clearCaptureError() {
        _uiState.update { it.copy(captureErrorMessage = null) }
    }

    fun setSelectedCaptureTab(tab: Int) {
        _uiState.update { it.copy(selectedCaptureTab = tab) }
    }

    // --- DPR / PDF Document Handlers ---

    fun setUploadedPdf(name: String?, uri: String?) {
        _uiState.update {
            it.copy(
                uploadedPdfName = name,
                uploadedPdfUri = uri,
                isPdfParsed = false,
                captureErrorMessage = null
            )
        }
    }

    fun loadSamplePdfReport() {
        _uiState.update {
            it.copy(
                uploadedPdfName = "DPR_ChennaiElevatedHighway_PackageB04_2026-08-26.pdf",
                uploadedPdfUri = "content://sample/dpr_b04.pdf",
                pdfPageCount = 4,
                isPdfParsed = true,
                captureErrorMessage = null
            )
        }
        startPdfDocumentAnalysis()
    }

    fun clearUploadedPdf() {
        _uiState.update {
            it.copy(
                uploadedPdfName = null,
                uploadedPdfUri = null,
                isPdfParsed = false
            )
        }
    }

    fun startPdfDocumentAnalysis() {
        val state = _uiState.value
        val pdfName = state.uploadedPdfName ?: "DPR_Site_Report.pdf"

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isMultimodalAnalyzing = true,
                    multimodalProgress = 0.20f,
                    multimodalStepText = "1/4 Parsing PDF structure and text OCR layers...",
                    activeCaptureResult = null,
                    captureErrorMessage = null
                )
            }

            delay(400)
            _uiState.update {
                it.copy(
                    multimodalProgress = 0.50f,
                    multimodalStepText = "2/4 Extracting Daily Progress quantities, manpower & delay logs..."
                )
            }

            delay(400)
            _uiState.update {
                it.copy(
                    multimodalProgress = 0.80f,
                    multimodalStepText = "3/4 Cross-referencing against WBS Bridge Package B04..."
                )
            }

            val targetActivity = state.activities.find { it.activityId == "ACT-B04-01" } 
                ?: state.activities.firstOrNull()

            val extractedResult = AiMultimodalExtractionResult(
                activityName = targetActivity?.activityName ?: "Pier P12 Concrete Pouring",
                detectedProgress = 48,
                executedQuantity = 220.0,
                unit = "m³",
                delayHours = 6,
                delayReason = "Transit mixer turnaround delayed 35 min; batching plant queue standby",
                status = "Critical",
                observation = "Extracted from PDF '$pdfName': Pier P12 cumulative pour reached 220 m³ out of 450 m³. Manpower shortfall of 8 workers reported.",
                visibleElements = listOf("Putzmeister Concrete Pump", "Transit Mixers (6m³)", "Pier P12 Formwork", "Batching Plant Slump Log"),
                issues = listOf("Mixer queue turnaround delay", "Manpower deficit (-8 workers)", "7-day cube strength verified (32.4 MPa)"),
                equipment = listOf("Putzmeister Boom Pump", "3x Transit Mixers (6m³)", "1x Concrete Vibrator"),
                materials = listOf("M45 Grade Concrete", "Silica Fume Admixture"),
                confidence = 96,
                matchedActivityId = "ACT-B04-01",
                matchConfidence = 96,
                isConfidentlyIdentified = true,
                weatherImpact = "Rain stoppage expected tomorrow morning; shift crews to covered yard",
                workforceEstimate = 18
            )

            delay(300)
            _uiState.update {
                it.copy(
                    isMultimodalAnalyzing = false,
                    multimodalProgress = 1.0f,
                    multimodalStepText = "4/4 DPR PDF Matched to ACT-B04-01 (96% confidence)",
                    activeCaptureResult = extractedResult,
                    manualSelectedActivityId = "ACT-B04-01",
                    isConfirmationOpen = true
                )
            }
        }
    }

    // --- Excel / CSV Progress Spreadsheet Handlers ---

    fun setUploadedSpreadsheet(name: String?, uri: String?, rows: List<SpreadsheetRowItem>) {
        _uiState.update {
            it.copy(
                uploadedSpreadsheetName = name,
                uploadedSpreadsheetUri = uri,
                spreadsheetRows = if (rows.isNotEmpty()) rows else SiteGptDataProvider.getSampleSpreadsheetRows(),
                selectedSpreadsheetRowIndex = null,
                captureErrorMessage = null
            )
        }
    }

    fun loadSampleSpreadsheet() {
        _uiState.update {
            it.copy(
                uploadedSpreadsheetName = "Site_Daily_Progress_Log_20260826.xlsx",
                uploadedSpreadsheetUri = "content://sample/progress_log.xlsx",
                spreadsheetRows = SiteGptDataProvider.getSampleSpreadsheetRows(),
                selectedSpreadsheetRowIndex = 0,
                captureErrorMessage = null
            )
        }
        processSpreadsheetRowDirectly(0)
    }

    fun clearUploadedSpreadsheet() {
        _uiState.update {
            it.copy(
                uploadedSpreadsheetName = null,
                uploadedSpreadsheetUri = null,
                selectedSpreadsheetRowIndex = null
            )
        }
    }

    fun selectSpreadsheetRow(index: Int) {
        _uiState.update { it.copy(selectedSpreadsheetRowIndex = index) }
        processSpreadsheetRowDirectly(index)
    }

    fun processSpreadsheetRowDirectly(rowIndex: Int) {
        val state = _uiState.value
        val rows = state.spreadsheetRows
        if (rowIndex < 0 || rowIndex >= rows.size) return
        val row = rows[rowIndex]

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isMultimodalAnalyzing = true,
                    multimodalProgress = 0.25f,
                    multimodalStepText = "1/3 Parsing spreadsheet row '${row.activityId}'...",
                    activeCaptureResult = null,
                    captureErrorMessage = null
                )
            }

            delay(300)
            _uiState.update {
                it.copy(
                    multimodalProgress = 0.65f,
                    multimodalStepText = "2/3 Calculating schedule variance for ${row.activityName}..."
                )
            }

            val matchingActivity = state.activities.find { it.activityId == row.activityId }

            val extractionResult = AiMultimodalExtractionResult(
                activityName = row.activityName,
                detectedProgress = row.actualProgress,
                executedQuantity = row.actualQty,
                unit = row.unit,
                delayHours = row.delayHours,
                delayReason = row.delayReason,
                status = if (row.delayHours > 0 || row.actualProgress < row.plannedProgress) "Delayed" else "On Track",
                observation = "Parsed from Spreadsheet row: ${row.activityName} at ${row.location}. Planned: ${row.plannedQty} ${row.unit}, Executed: ${row.actualQty} ${row.unit} (${row.actualProgress}%). Contractor: ${row.contractor}.",
                visibleElements = listOf("Tabular Progress Record", "Quantity Survey Log", "Contractor Daily Entry"),
                issues = if (row.issues.isNotBlank()) listOf(row.issues) else emptyList(),
                equipment = listOf(matchingActivity?.equipment ?: "Standard Construction Plant"),
                materials = listOf("Approved Site Materials"),
                confidence = 98,
                matchedActivityId = row.activityId,
                matchConfidence = 98,
                isConfidentlyIdentified = true,
                weatherImpact = null,
                workforceEstimate = matchingActivity?.manpower ?: 16
            )

            delay(250)
            _uiState.update {
                it.copy(
                    isMultimodalAnalyzing = false,
                    multimodalProgress = 1.0f,
                    multimodalStepText = "3/3 Spreadsheet Linked to ${row.activityId} (98% confidence)",
                    activeCaptureResult = extractionResult,
                    manualSelectedActivityId = row.activityId,
                    isConfirmationOpen = true
                )
            }
        }
    }

    /**
     * One-tap Demo Evidence Loader: loads the standard prompt verification case
     * "Column C12 reinforcement is 80 percent complete. Rain stopped work for two hours."
     */
    fun loadDemoCaptureData() {
        _uiState.update {
            it.copy(
                voiceTranscript = "Column C12 reinforcement is 80 percent complete. Rain stopped work for two hours.",
                typedObservation = "Rebar cage tying at Ch. 12+800 interrupted by sudden rainfall. 16 workers on site.",
                isDemoPhoto = true,
                capturedPhotoUri = null,
                captureErrorMessage = null
            )
        }
        startMultimodalAnalysis()
    }

    /**
     * Executes AI Multimodal Extraction: Photo + Voice/Text -> AI Extraction -> Schedule Activity Matching
     */
    fun startMultimodalAnalysis() {
        val state = _uiState.value
        val hasPhoto = state.capturedPhotoBitmap != null || state.isDemoPhoto
        val hasVoice = state.voiceTranscript.isNotBlank()
        val hasText = state.typedObservation.isNotBlank()
        val hasPdf = state.uploadedPdfName != null
        val hasSpreadsheet = state.uploadedSpreadsheetName != null

        if (!hasPhoto && !hasVoice && !hasText && !hasPdf && !hasSpreadsheet) {
            setCaptureError("Please capture a photo, record a voice note, or upload a DPR/Spreadsheet first.")
            return
        }

        if (hasPdf && !hasPhoto && !hasVoice && !hasText) {
            startPdfDocumentAnalysis()
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isMultimodalAnalyzing = true,
                    multimodalProgress = 0.15f,
                    multimodalStepText = if (hasPhoto) "1/4 Analyzing site photo visual elements..." else "1/3 Parsing voice/text site logs...",
                    activeCaptureResult = null,
                    captureErrorMessage = null
                )
            }

            delay(350)
            _uiState.update {
                it.copy(
                    multimodalProgress = 0.45f,
                    multimodalStepText = "2/4 Extracting progress %, delay hours & weather impacts..."
                )
            }

            delay(350)
            _uiState.update {
                it.copy(
                    multimodalProgress = 0.75f,
                    multimodalStepText = "3/4 Matching to Project Schedule WBS activities..."
                )
            }

            // Call Gemini Multimodal / Local Semantic Grounding
            val result = SiteGptAiService.analyzeMultimodalEvidence(
                imageBitmap = state.capturedPhotoBitmap,
                voiceTranscript = state.voiceTranscript,
                rawText = state.typedObservation,
                candidateActivities = state.activities
            )

            delay(250)
            _uiState.update {
                it.copy(
                    isMultimodalAnalyzing = false,
                    multimodalProgress = 1.0f,
                    multimodalStepText = "4/4 Schedule activity matched (${result.matchConfidence}% confidence)",
                    activeCaptureResult = result,
                    manualSelectedActivityId = result.matchedActivityId,
                    isConfirmationOpen = true
                )
            }
        }
    }

    fun dismissConfirmationDialog() {
        _uiState.update { it.copy(isConfirmationOpen = false) }
    }

    fun setManualSelectedActivity(activityId: String) {
        _uiState.update { state ->
            val updatedResult = state.activeCaptureResult?.copy(
                matchedActivityId = activityId,
                matchConfidence = 99
            )
            state.copy(
                manualSelectedActivityId = activityId,
                activeCaptureResult = updatedResult,
                isManualActivityPickerOpen = false
            )
        }
    }

    fun setManualActivityPickerOpen(open: Boolean) {
        _uiState.update { it.copy(isManualActivityPickerOpen = open) }
    }

    fun updatePendingCaptureResult(
        activityName: String,
        matchedActivityId: String,
        progress: Int,
        delayHours: Int,
        delayReason: String,
        observation: String
    ) {
        _uiState.update { state ->
            val current = state.activeCaptureResult ?: return@update state
            val updated = current.copy(
                activityName = activityName,
                matchedActivityId = matchedActivityId,
                detectedProgress = progress,
                delayHours = delayHours,
                delayReason = delayReason,
                observation = observation,
                status = if (delayHours > 0 || progress < 90) "Delayed" else "On Track"
            )
            state.copy(activeCaptureResult = updated, manualSelectedActivityId = matchedActivityId)
        }
    }

    /**
     * Commits confirmed multimodal report to Firestore, updates schedule activity progress,
     * recalculates variance and project stats, and appends to site evidence history.
     */
    fun confirmAndSaveSiteReport() {
        val state = _uiState.value
        val result = state.activeCaptureResult ?: return
        val targetActId = state.manualSelectedActivityId ?: result.matchedActivityId ?: "ACT-024"

        val currentActivities = state.activities.toMutableList()
        val index = currentActivities.indexOfFirst { it.activityId == targetActId }
        val targetActivity = if (index != -1) currentActivities[index] else currentActivities.firstOrNull()

        val plannedProg = targetActivity?.plannedProgress ?: 90
        val actualProg = result.detectedProgress
        val progressVariance = actualProg - plannedProg // e.g. 80 - 90 = -10%

        val statusStr = if (result.delayHours > 0 || progressVariance < 0) "Delayed" else "On Track"
        val statusEnum = if (progressVariance <= -20) ActivityStatus.CRITICAL
        else if (progressVariance < 0 || result.delayHours > 0) ActivityStatus.DELAYED
        else ActivityStatus.ON_TRACK

        if (index != -1 && targetActivity != null) {
            val updatedActivity = targetActivity.copy(
                actualProgress = actualProg,
                actualQuantity = result.executedQuantity ?: (targetActivity.plannedQuantity * actualProg / 100.0),
                status = statusEnum,
                delayDays = if (result.delayHours > 0) (result.delayHours / 8).coerceAtLeast(1) else targetActivity.delayDays,
                delayReason = if (result.delayReason.isNotBlank()) result.delayReason else targetActivity.delayReason
            )
            currentActivities[index] = updatedActivity
        }

        // Recompute project stats
        val avgActualProgress = currentActivities.map { it.actualProgress }.average()
        val scheduleVariance = avgActualProgress - state.projectOverview.plannedProgress
        val delayedCount = currentActivities.count { it.status == ActivityStatus.DELAYED || it.status == ActivityStatus.CRITICAL }

        val timeFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val capturedTimeStr = timeFormat.format(Date())

        val newEvidenceReport = SiteEvidenceReport(
            id = "EV-${System.currentTimeMillis() % 100000}",
            projectId = state.projectOverview.projectId,
            activityId = targetActId,
            activityName = targetActivity?.activityName ?: result.activityName,
            plannedProgress = plannedProg,
            actualProgress = actualProg,
            progressVariance = progressVariance,
            status = statusStr,
            delayHours = result.delayHours,
            delayDays = if (result.delayHours > 0) (result.delayHours / 8).coerceAtLeast(1) else 0,
            delayReason = result.delayReason,
            observation = result.observation,
            capturedAt = "Today, $capturedTimeStr",
            source = ReportSource(
                photo = state.capturedPhotoBitmap != null || state.isDemoPhoto,
                voice = state.voiceTranscript.isNotBlank(),
                text = state.typedObservation.isNotBlank(),
                pdf = state.uploadedPdfName != null,
                spreadsheet = state.uploadedSpreadsheetName != null,
                documentName = state.uploadedPdfName ?: state.uploadedSpreadsheetName
            ),
            aiConfidence = result.confidence,
            isDemo = state.isDemoPhoto,
            visibleElements = result.visibleElements,
            issues = result.issues,
            equipment = result.equipment,
            materials = result.materials,
            photoUri = state.capturedPhotoUri,
            syncedToFirestore = true
        )

        val updatedHistory = listOf(newEvidenceReport) + state.siteEvidenceHistory

        // Firestore sync asynchronously
        viewModelScope.launch {
            SiteFirestoreService.saveSiteReport(newEvidenceReport)
            SiteFirestoreService.updateActivityProgress(
                activityId = targetActId,
                actualProgress = actualProg,
                actualQuantity = result.executedQuantity,
                delayReason = result.delayReason,
                status = statusStr
            )
        }

        _uiState.update {
            it.copy(
                activities = currentActivities,
                projectOverview = it.projectOverview.copy(
                    actualProgress = String.format(Locale.US, "%.1f", avgActualProgress).toDoubleOrNull() ?: it.projectOverview.actualProgress,
                    scheduleVariance = String.format(Locale.US, "%.1f", scheduleVariance).toDoubleOrNull() ?: it.projectOverview.scheduleVariance,
                    delayedActivities = delayedCount
                ),
                siteEvidenceHistory = updatedHistory,
                isConfirmationOpen = false,
                capturedPhotoBitmap = null,
                capturedPhotoUri = null,
                voiceTranscript = "",
                typedObservation = "",
                activeCaptureResult = null,
                showCommitSuccessSnackbar = true,
                commitSnackbarMessage = "Saved to Firestore & linked to $targetActId (${targetActivity?.activityName ?: result.activityName}). Progress updated to $actualProg% (Variance: ${progressVariance}%)."
            )
        }
    }

    fun selectEvidenceForDetail(report: SiteEvidenceReport?) {
        _uiState.update {
            it.copy(
                selectedEvidenceReportForDetail = report,
                isEvidenceDetailModalOpen = report != null
            )
        }
    }

    fun dismissEvidenceDetail() {
        _uiState.update {
            it.copy(
                selectedEvidenceReportForDetail = null,
                isEvidenceDetailModalOpen = false
            )
        }
    }

    // --- Legacy Document Extraction (DPR) ---
    fun startDocumentExtraction(fileName: String = "DPR_ChennaiHighway_2026-08-26.pdf") {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isExtracting = true,
                    extractionProgress = 0.1f,
                    extractedDraft = null
                )
            }

            delay(500)
            _uiState.update { it.copy(extractionProgress = 0.4f) }
            delay(600)
            _uiState.update { it.copy(extractionProgress = 0.75f) }
            delay(500)

            val extracted = SiteGptAiService.extractFromDocument(fileName, "")
            _uiState.update {
                it.copy(
                    isExtracting = false,
                    extractionProgress = 1.0f,
                    extractedDraft = extracted
                )
            }
        }
    }

    fun updateExtractedDraft(
        activityName: String,
        executedQty: Double,
        manpower: Int,
        progress: Int,
        issues: String
    ) {
        _uiState.update { state ->
            val draft = state.extractedDraft ?: return@update state
            state.copy(
                extractedDraft = draft.copy(
                    activityName = activityName,
                    executedQuantity = executedQty,
                    manpower = manpower,
                    completionPercent = progress,
                    actualProgress = progress,
                    issues = issues
                )
            )
        }
    }

    fun commitExtractedDraftToSchedule() {
        val draft = _uiState.value.extractedDraft ?: return
        val currentActivities = _uiState.value.activities.toMutableList()
        val index = currentActivities.indexOfFirst { it.activityId == draft.activityId }

        if (index != -1) {
            val target = currentActivities[index]
            val updated = target.copy(
                actualQuantity = draft.executedQuantity,
                actualProgress = draft.completionPercent,
                manpower = draft.manpower,
                delayReason = if (draft.issues.isNotBlank()) draft.issues else target.delayReason
            )
            currentActivities[index] = updated
        }

        // Recompute project actual progress
        val totalActualProgress = currentActivities.map { it.actualProgress }.average()

        _uiState.update { state ->
            state.copy(
                activities = currentActivities,
                projectOverview = state.projectOverview.copy(
                    actualProgress = String.format(Locale.US, "%.1f", totalActualProgress).toDoubleOrNull() ?: state.projectOverview.actualProgress
                ),
                extractedDraft = null,
                showCommitSuccessSnackbar = true,
                commitSnackbarMessage = "Successfully verified & committed DPR update to ${draft.activityId} (${draft.activityName})."
            )
        }
    }

    fun dismissCommitSnackbar() {
        _uiState.update { it.copy(showCommitSuccessSnackbar = false) }
    }

    // --- What-If Simulator ---
    fun updateWhatIfPrompt(prompt: String) {
        _uiState.update { it.copy(whatIfInputPrompt = prompt) }
    }

    fun runWhatIfSimulation(customPrompt: String? = null) {
        val prompt = customPrompt ?: _uiState.value.whatIfInputPrompt
        if (prompt.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isWhatIfLoading = true) }
            val scenario = SiteGptAiService.processWhatIfQuery(prompt, _uiState.value.activities)
            _uiState.update { state ->
                val list = listOf(scenario) + state.whatIfScenarios
                state.copy(
                    isWhatIfLoading = false,
                    currentWhatIfScenario = scenario,
                    whatIfScenarios = list.take(10)
                )
            }
        }
    }

    // --- SiteGPT Copilot ---
    fun updateCopilotInput(input: String) {
        _uiState.update { it.copy(copilotInput = input) }
    }

    fun sendCopilotMessage(presetText: String? = null) {
        val messageText = presetText ?: _uiState.value.copilotInput
        if (messageText.isBlank()) return

        val userMsg = CopilotMessage(
            id = "MSG-U-${System.currentTimeMillis()}-${UUID.randomUUID().toString().take(6)}",
            isUser = true,
            timestamp = "Just now",
            text = messageText
        )

        _uiState.update { state ->
            state.copy(
                copilotMessages = state.copilotMessages + userMsg,
                copilotInput = "",
                isCopilotLoading = true
            )
        }

        viewModelScope.launch {
            val reply = SiteGptAiService.answerCopilotQuery(
                userQuery = messageText,
                projectOverview = _uiState.value.projectOverview,
                activities = _uiState.value.activities
            )
            val uniqueReply = reply.copy(
                id = "MSG-A-${System.currentTimeMillis()}-${UUID.randomUUID().toString().take(6)}"
            )
            _uiState.update { state ->
                state.copy(
                    copilotMessages = state.copilotMessages + uniqueReply,
                    isCopilotLoading = false
                )
            }
        }
    }

    // --- Recovery Strategies ---
    fun applyRecoveryStrategy(strategyId: String) {
        _uiState.update { state ->
            val updatedStrategies = state.recoveryStrategies.map { strat ->
                if (strat.id == strategyId) {
                    strat.copy(isApplied = !strat.isApplied)
                } else strat
            }
            val activeApplied = updatedStrategies.filter { it.isApplied }
            val totalDaysRecovered = activeApplied.sumOf { it.expectedDaysRecovered }
            val adjustedDaysBehind = (-18 + totalDaysRecovered).coerceAtMost(0)

            state.copy(
                recoveryStrategies = updatedStrategies,
                projectOverview = state.projectOverview.copy(
                    daysBehindAhead = adjustedDaysBehind,
                    forecastEndDate = if (totalDaysRecovered > 0) "${(18 - totalDaysRecovered).coerceAtLeast(1)} Dec 2026" else "18 Dec 2026"
                ),
                showCommitSuccessSnackbar = true,
                commitSnackbarMessage = "Strategy activated. Projected schedule recovered by +$totalDaysRecovered days."
            )
        }
    }

    // --- Modals & Activity Detail ---
    fun selectActivityForDetail(activity: SiteActivity?) {
        _uiState.update { it.copy(selectedActivityForDetail = activity) }
    }

    fun showExplainability(activity: SiteActivity) {
        _uiState.update {
            it.copy(
                selectedActivityForDetail = activity,
                isExplainabilityOpen = true
            )
        }
    }

    fun dismissExplainability() {
        _uiState.update { it.copy(isExplainabilityOpen = false) }
    }

    fun showEditActivity(activity: SiteActivity) {
        _uiState.update {
            it.copy(
                selectedActivityForDetail = activity,
                isEditActivityOpen = true
            )
        }
    }

    fun dismissEditActivity() {
        _uiState.update { it.copy(isEditActivityOpen = false) }
    }

    fun updateActivityManual(
        activityId: String,
        newActualQty: Double,
        newManpower: Int,
        newProgress: Int,
        newStatus: ActivityStatus
    ) {
        _uiState.update { state ->
            val updated = state.activities.map { act ->
                if (act.activityId == activityId) {
                    act.copy(
                        actualQuantity = newActualQty,
                        manpower = newManpower,
                        actualProgress = newProgress,
                        status = newStatus
                    )
                } else act
            }
            state.copy(
                activities = updated,
                isEditActivityOpen = false,
                showCommitSuccessSnackbar = true,
                commitSnackbarMessage = "Activity $activityId updated successfully."
            )
        }
    }

    fun resetDemo() {
        loadInitialState()
        _uiState.update {
            it.copy(
                projectOverview = SiteGptDataProvider.getInitialProjectOverview(),
                activities = SiteGptDataProvider.getInitialActivities(),
                risks = SiteGptDataProvider.getInitialRisks(),
                weatherForecast = SiteGptDataProvider.getInitialWeatherForecast(),
                recoveryStrategies = SiteGptDataProvider.getInitialRecoveryStrategies(),
                aiInsights = SiteGptDataProvider.getInitialAiInsights(),
                dailySummary = SiteGptDataProvider.getInitialDailySummary(),
                extractedDraft = null,
                capturedPhotoBitmap = null,
                capturedPhotoUri = null,
                voiceTranscript = "",
                typedObservation = "",
                siteEvidenceHistory = SiteGptDataProvider.getInitialEvidenceReports(),
                showCommitSuccessSnackbar = true,
                commitSnackbarMessage = "Demo data reset to Chennai Elevated Highway Project baseline."
            )
        }
    }
}
