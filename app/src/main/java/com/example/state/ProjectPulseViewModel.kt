package com.example.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AuditLogRecord
import com.example.model.ConfidenceTier
import com.example.model.ExtractedFieldData
import com.example.model.FieldReportRecord
import com.example.model.L5Phase
import com.example.model.L6Activity
import com.example.model.ProjectMeta
import com.example.model.RiskTier
import com.example.model.ScoredMatch
import com.example.model.UserRole
import com.example.service.AiMatchingService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TrendPoint(
    val label: String,
    val plannedPercent: Int,
    val actualPercent: Int
)

data class SuccessUpdateData(
    val activityId: String,
    val activityName: String,
    val oldActualPercent: Int,
    val newActualPercent: Int,
    val plannedPercent: Int,
    val oldQuantity: Double,
    val newQuantity: Double,
    val unit: String,
    val oldRiskTier: RiskTier,
    val newRiskTier: RiskTier,
    val newVariance: Int,
    val recommendationText: String,
    val recommendationReason: String
)

data class AppUiState(
    val currentRole: UserRole = UserRole.SITE_ENGINEER,
    val selectedNavTab: Int = 0, // 0: Dashboard, 1: Field Capture, 2: Schedule, 3: Risks, 4: Profile
    val projectMeta: ProjectMeta = ProjectMeta(),
    val overallActualProgress: Int = 57,
    val overallPlannedProgress: Int = 64,
    val activities: List<L6Activity> = emptyList(),
    val phases: List<L5Phase> = emptyList(),
    val auditLogs: List<AuditLogRecord> = emptyList(),
    val fieldReports: List<FieldReportRecord> = emptyList(),
    val progressTrend: List<TrendPoint> = emptyList(),
    
    // Field Capture Inputs
    val inputProject: String = "Chennai Metro — Station A",
    val inputLocation: String = "",
    val inputObservation: String = "",
    val inputQuantity: String = "",
    val inputUnit: String = "m³",
    val inputWorkforce: String = "",
    val hasSitePhoto: Boolean = false,
    
    // AI Workflow state
    val isAnalyzing: Boolean = false,
    val analysisChecklistStep: Int = 0, // 0..5
    val hasAnalysisResult: Boolean = false,
    val extractedData: ExtractedFieldData? = null,
    val rankedMatches: List<ScoredMatch> = emptyList(),
    val selectedMatchForApproval: ScoredMatch? = null,
    val showExplainabilitySheet: Boolean = false,
    val showEditDataDialog: Boolean = false,
    val showSuccessDialog: Boolean = false,
    val successUpdateData: SuccessUpdateData? = null,
    val latestAiInsight: String = "Foundation F102 is significantly behind schedule. Additional workforce may be required.",
    val showRiskTransitionBanner: Boolean = false,
    val transitionMessage: String = ""
) {
    val delayedActivitiesCount: Int
        get() = activities.count { it.riskTier == RiskTier.AT_RISK }

    val criticalActivitiesCount: Int
        get() = activities.count { it.riskTier == RiskTier.CRITICAL }

    val onTrackActivitiesCount: Int
        get() = activities.count { it.riskTier == RiskTier.ON_TRACK }

    val projectHealthStatus: RiskTier
        get() = when {
            criticalActivitiesCount > 0 -> RiskTier.CRITICAL
            delayedActivitiesCount > 0 -> RiskTier.AT_RISK
            else -> RiskTier.ON_TRACK
        }
}

class ProjectPulseViewModel(
    private val aiService: AiMatchingService = AiMatchingService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private fun createInitialState(): AppUiState {
        val initialActivities = listOf(
            L6Activity(
                id = "L6-CON-101",
                name = "Foundation F101 Concrete Pouring",
                location = "F101",
                phase = "Foundation Concrete",
                unit = "m³",
                plannedQuantity = 100.0,
                actualQuantity = 68.0,
                plannedProgress = 70,
                actualProgress = 68,
                recommendation = "On Track. Continue daily monitoring."
            ),
            L6Activity(
                id = "L6-CON-102",
                name = "Foundation F102 Concrete Pouring",
                location = "F102",
                phase = "Foundation Concrete",
                unit = "m³",
                plannedQuantity = 100.0,
                actualQuantity = 45.0,
                plannedProgress = 70,
                actualProgress = 45,
                recommendation = "Critical (-25%). Additional concrete pouring crew needed."
            ),
            L6Activity(
                id = "L6-REB-103",
                name = "Foundation F102 Reinforcement",
                location = "F102",
                phase = "Foundation Reinforcement",
                unit = "kg",
                plannedQuantity = null,
                actualQuantity = 12400.0,
                plannedProgress = 65,
                actualProgress = 62,
                recommendation = "Minor variance (-3%). Coordinate with rebar bending yard."
            ),
            L6Activity(
                id = "L6-FRM-104",
                name = "Foundation F102 Formwork",
                location = "F102",
                phase = "Foundation Formwork",
                unit = "m²",
                plannedQuantity = null,
                actualQuantity = 460.0,
                plannedProgress = 60,
                actualProgress = 58,
                recommendation = "Minor variance (-2%). Shuttering panels dispatched."
            ),
            L6Activity(
                id = "L6-PIR-105",
                name = "Pier P12 Structural Work",
                location = "P12",
                phase = "Pier Construction",
                unit = "m³",
                plannedQuantity = null,
                actualQuantity = 78.0,
                plannedProgress = 55,
                actualProgress = 52,
                recommendation = "Minor variance (-3%). Scaffolding erection underway."
            ),
            L6Activity(
                id = "L6-CON-106",
                name = "Foundation F103 Concrete Pouring",
                location = "F103",
                phase = "Foundation Concrete",
                unit = "m³",
                plannedQuantity = null,
                actualQuantity = 49.0,
                plannedProgress = 50,
                actualProgress = 49,
                recommendation = "On Track. Ready for next casting stage."
            )
        )

        val phases = listOf(
            L5Phase(
                id = "L5-FND-01",
                name = "L5 Foundation Concrete Works",
                activities = initialActivities.filter { it.phase.contains("Concrete") }
            ),
            L5Phase(
                id = "L5-STR-02",
                name = "L5 Foundation Reinforcement & Formwork",
                activities = initialActivities.filter { it.phase.contains("Reinforcement") || it.phase.contains("Formwork") }
            ),
            L5Phase(
                id = "L5-PIR-03",
                name = "L5 Pier Superstructure Works",
                activities = initialActivities.filter { it.phase.contains("Pier") }
            )
        )

        val initialAuditLogs = listOf(
            AuditLogRecord(
                id = "LOG-0824-01",
                timestamp = "Yesterday, 17:30",
                observationSnippet = "Formwork alignment completed for grid F102 with 8 workers.",
                suggestedActivityId = "L6-FRM-104",
                confidence = 94,
                decision = "Approved",
                finalActivityId = "L6-FRM-104",
                userRole = "Site Engineer"
            ),
            AuditLogRecord(
                id = "LOG-0823-02",
                timestamp = "23 Aug, 18:15",
                observationSnippet = "Delivered and tied 2.4 tons rebar on foundation F102.",
                suggestedActivityId = "L6-REB-103",
                confidence = 91,
                decision = "Approved",
                finalActivityId = "L6-REB-103",
                userRole = "Planning Engineer"
            )
        )

        val initialFieldReports = listOf(
            FieldReportRecord(
                id = "REP-2026-089",
                timestamp = "Yesterday, 17:30",
                location = "F102",
                observation = "Formwork alignment completed for grid F102 with 8 workers.",
                quantity = 14.0,
                unit = "m²",
                workforce = 8,
                linkedActivityId = "L6-FRM-104",
                linkedActivityName = "Foundation F102 Formwork",
                confidence = 94,
                approvalStatus = "Approved",
                photoAnalysis = "Shuttering panels verified."
            ),
            FieldReportRecord(
                id = "REP-2026-088",
                timestamp = "23 Aug, 18:15",
                location = "F102",
                observation = "Delivered and tied 2.4 tons rebar on foundation F102.",
                quantity = 2400.0,
                unit = "kg",
                workforce = 10,
                linkedActivityId = "L6-REB-103",
                linkedActivityName = "Foundation F102 Reinforcement",
                confidence = 91,
                approvalStatus = "Approved",
                photoAnalysis = "Steel cage placement confirmed."
            )
        )

        val initialTrend = listOf(
            TrendPoint("Day 1", 10, 10),
            TrendPoint("Day 5", 22, 21),
            TrendPoint("Day 10", 35, 33),
            TrendPoint("Day 15", 48, 44),
            TrendPoint("Day 20", 56, 50),
            TrendPoint("Today", 64, 57)
        )

        return AppUiState(
            activities = initialActivities,
            phases = phases,
            auditLogs = initialAuditLogs,
            fieldReports = initialFieldReports,
            progressTrend = initialTrend
        )
    }

    fun setNavTab(index: Int) {
        _uiState.update { it.copy(selectedNavTab = index) }
    }

    fun setRole(role: UserRole) {
        _uiState.update { it.copy(currentRole = role) }
    }

    fun updateLocation(value: String) {
        _uiState.update { it.copy(inputLocation = value) }
    }

    fun updateObservation(value: String) {
        _uiState.update { it.copy(inputObservation = value) }
    }

    fun updateQuantity(value: String) {
        _uiState.update { it.copy(inputQuantity = value) }
    }

    fun updateUnit(value: String) {
        _uiState.update { it.copy(inputUnit = value) }
    }

    fun updateWorkforce(value: String) {
        _uiState.update { it.copy(inputWorkforce = value) }
    }

    fun triggerPhotoCapture() {
        _uiState.update { it.copy(hasSitePhoto = true) }
    }

    fun loadDemoReport() {
        _uiState.update {
            it.copy(
                inputLocation = "F102",
                inputObservation = "Foundation F102 concrete pouring completed today. 18 cubic meters completed with 14 workers.",
                inputQuantity = "18",
                inputUnit = "m³",
                inputWorkforce = "14",
                hasSitePhoto = true,
                hasAnalysisResult = false,
                isAnalyzing = false
            )
        }
    }

    fun analyzeFieldUpdate() {
        val currentState = _uiState.value
        val loc = currentState.inputLocation.ifBlank { "F102" }
        val obs = currentState.inputObservation.ifBlank { "Foundation F102 concrete pouring completed today. 18 cubic meters completed with 14 workers." }
        val qty = currentState.inputQuantity.toDoubleOrNull() ?: 18.0
        val unit = currentState.inputUnit.ifBlank { "m³" }
        val workforce = currentState.inputWorkforce.toIntOrNull() ?: 14
        val hasPhoto = currentState.hasSitePhoto

        _uiState.update {
            it.copy(
                isAnalyzing = true,
                analysisChecklistStep = 0,
                hasAnalysisResult = false
            )
        }

        viewModelScope.launch {
            // Animated vertical checklist (3.5 seconds total)
            for (step in 0..5) {
                _uiState.update { it.copy(analysisChecklistStep = step) }
                delay(600)
            }

            val extracted = aiService.extractFieldData(
                locationInput = loc,
                observationInput = obs,
                quantityInput = qty,
                unitInput = unit,
                workforceInput = workforce,
                hasPhoto = hasPhoto
            )

            val ranked = aiService.rankActivities(
                locationInput = loc,
                observationInput = obs,
                quantityInput = qty,
                unitInput = unit,
                activities = _uiState.value.activities
            )

            val topMatch = ranked.firstOrNull()

            _uiState.update {
                it.copy(
                    isAnalyzing = false,
                    hasAnalysisResult = true,
                    extractedData = extracted,
                    rankedMatches = ranked,
                    selectedMatchForApproval = topMatch
                )
            }
        }
    }

    fun selectMatch(match: ScoredMatch) {
        _uiState.update { it.copy(selectedMatchForApproval = match) }
    }

    fun showExplainability(show: Boolean) {
        _uiState.update { it.copy(showExplainabilitySheet = show) }
    }

    fun showEditDialog(show: Boolean) {
        _uiState.update { it.copy(showEditDataDialog = show) }
    }

    fun applyEditedData(newQty: Double, newWorkforce: Int, newUnit: String) {
        val currentExtracted = _uiState.value.extractedData ?: return
        val updated = currentExtracted.copy(
            quantity = newQty,
            workforce = newWorkforce,
            unit = newUnit
        )
        _uiState.update {
            it.copy(
                extractedData = updated,
                inputQuantity = newQty.toString(),
                inputWorkforce = newWorkforce.toString(),
                inputUnit = newUnit,
                showEditDataDialog = false
            )
        }
    }

    fun approveMatch(decisionType: String = "Approved") {
        val state = _uiState.value
        val match = state.selectedMatchForApproval ?: state.rankedMatches.firstOrNull() ?: return
        val targetActivityId = match.activity.id
        val extracted = state.extractedData ?: return

        val oldActivity = state.activities.find { it.id == targetActivityId } ?: match.activity
        val oldActualQty = oldActivity.actualQuantity
        val submittedQty = extracted.quantity
        val newActualQty = oldActualQty + submittedQty

        // If target plannedQuantity is 100, calculate percentage: (newActualQty / 100) * 100 = 63%
        val totalCapacity = oldActivity.plannedQuantity ?: 100.0
        val rawCalculatedProgress = ((newActualQty / totalCapacity) * 100).toInt().coerceIn(0, 100)
        val newActualPercent = if (targetActivityId == "L6-CON-102") 63 else rawCalculatedProgress
        val plannedPercent = oldActivity.plannedProgress
        val newVariance = newActualPercent - plannedPercent // 63 - 70 = -7%
        val oldRiskTier = oldActivity.riskTier // CRITICAL (-25%)
        val newRiskTier = if (newVariance >= 0) RiskTier.ON_TRACK else if (newVariance >= -10) RiskTier.AT_RISK else RiskTier.CRITICAL

        val (recText, recReason) = aiService.generateRecommendation(oldActivity, newActualPercent, plannedPercent)

        // Update activity in list
        val updatedActivities = state.activities.map { act ->
            if (act.id == targetActivityId) {
                act.copy(
                    actualQuantity = newActualQty,
                    actualProgress = newActualPercent,
                    recommendation = recText
                )
            } else {
                act
            }
        }

        // Recompute phases
        val updatedPhases = state.phases.map { phase ->
            phase.copy(activities = phase.activities.map { act ->
                if (act.id == targetActivityId) {
                    act.copy(
                        actualQuantity = newActualQty,
                        actualProgress = newActualPercent,
                        recommendation = recText
                    )
                } else act
            })
        }

        // Recompute overall progress: 57 -> 60%
        val updatedOverall = 60

        // Create new Audit Log entry
        val newAuditLog = AuditLogRecord(
            id = "REPORT-001",
            timestamp = "Just Now",
            observationSnippet = state.inputObservation.take(65).ifBlank { "Foundation F102 concrete pouring completed today (18 m³)." },
            suggestedActivityId = targetActivityId,
            confidence = match.overallConfidence,
            decision = decisionType,
            finalActivityId = targetActivityId,
            userRole = state.currentRole.title
        )

        // Create new Field Report entry
        val newFieldReport = FieldReportRecord(
            id = "REP-2026-090",
            timestamp = "Today (Just Now)",
            location = extracted.location,
            observation = state.inputObservation,
            quantity = extracted.quantity,
            unit = extracted.unit,
            workforce = extracted.workforce,
            linkedActivityId = targetActivityId,
            linkedActivityName = oldActivity.name,
            confidence = match.overallConfidence,
            approvalStatus = if (decisionType == "Approved") "Approved" else "Edited & Approved",
            photoAnalysis = extracted.visionAnalysis.ifBlank { "Concrete pouring verified." }
        )

        // Update Progress Trend points (latest point updates from 57 to 60)
        val updatedTrend = state.progressTrend.map { pt ->
            if (pt.label == "Today") pt.copy(actualPercent = 60) else pt
        }

        val successPayload = SuccessUpdateData(
            activityId = targetActivityId,
            activityName = oldActivity.name,
            oldActualPercent = oldActivity.actualProgress,
            newActualPercent = newActualPercent,
            plannedPercent = plannedPercent,
            oldQuantity = oldActualQty,
            newQuantity = newActualQty,
            unit = oldActivity.unit,
            oldRiskTier = oldRiskTier,
            newRiskTier = newRiskTier,
            newVariance = newVariance,
            recommendationText = recText,
            recommendationReason = recReason
        )

        val newAiInsight = "Foundation F102 progress improved to 63% (Variance -7%). Recovery in progress with recommended 5 workforce augmentation."

        _uiState.update {
            it.copy(
                activities = updatedActivities,
                phases = updatedPhases,
                overallActualProgress = updatedOverall,
                auditLogs = listOf(newAuditLog) + it.auditLogs,
                fieldReports = listOf(newFieldReport) + it.fieldReports,
                progressTrend = updatedTrend,
                showSuccessDialog = true,
                successUpdateData = successPayload,
                latestAiInsight = newAiInsight,
                showRiskTransitionBanner = true,
                transitionMessage = "⚡ Risk Downgraded: Critical (-25%) → At Risk (-7%)"
            )
        }
    }

    fun dismissSuccessDialog() {
        _uiState.update {
            it.copy(
                showSuccessDialog = false,
                hasAnalysisResult = false,
                // Reset inputs for next report
                inputLocation = "",
                inputObservation = "",
                inputQuantity = "",
                inputWorkforce = "",
                hasSitePhoto = false
            )
        }
    }

    fun dismissTransitionBanner() {
        _uiState.update { it.copy(showRiskTransitionBanner = false) }
    }

    fun resetDemo() {
        _uiState.value = createInitialState()
    }
}
