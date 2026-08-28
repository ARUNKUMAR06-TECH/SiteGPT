package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.state.SiteGptViewModel
import com.example.ui.components.AiExplainabilityModal
import com.example.ui.components.ManualEditActivityDialog
import com.example.ui.components.SiteGptHeader
import com.example.ui.screens.CopilotScreen
import com.example.ui.screens.DailySummaryScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DataCaptureScreen
import com.example.ui.screens.DelayPredictionScreen
import com.example.ui.screens.RiskRecoveryScreen
import com.example.ui.screens.ScheduleScreen
import com.example.ui.screens.TimelineGanttScreen
import com.example.ui.screens.WhatIfSimulatorScreen
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyberBackgroundGradient
import com.example.ui.theme.CyberVioletDark
import com.example.ui.theme.CyberViolet700
import com.example.ui.theme.CyberViolet800
import com.example.ui.theme.CyberViolet900
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonLavender
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: SiteGptViewModel = viewModel()
                SiteGptAppContent(viewModel = viewModel)
            }
        }
    }
}

data class BottomNavItem(
    val index: Int,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun SiteGptAppContent(viewModel: SiteGptViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val bottomNavItems = listOf(
        BottomNavItem(0, "Dashboard", Icons.Filled.Dashboard, Icons.Outlined.Dashboard, "nav_dashboard"),
        BottomNavItem(1, "Capture", Icons.Filled.CloudUpload, Icons.Outlined.CloudUpload, "nav_capture"),
        BottomNavItem(2, "Schedule", Icons.Filled.Assignment, Icons.Outlined.Assignment, "nav_schedule"),
        BottomNavItem(4, "What-If", Icons.Filled.Psychology, Icons.Outlined.Psychology, "nav_what_if"),
        BottomNavItem(6, "Copilot", Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome, "nav_copilot"),
        BottomNavItem(8, "Summary", Icons.Filled.Description, Icons.Outlined.Description, "nav_summary")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CyberVioletDark,
        topBar = {
            SiteGptHeader(
                state = uiState,
                onTabSelected = { viewModel.setNavTab(it) },
                onRoleSelected = { viewModel.setRole(it) },
                onResetDemo = { viewModel.resetDemo() }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(SurfaceBorder)
                )
                NavigationBar(
                    modifier = Modifier
                        .background(SurfaceDark)
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("bottom_navigation_bar"),
                    containerColor = SurfaceDark,
                    tonalElevation = 0.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = uiState.selectedNavTab == item.index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.setNavTab(item.index) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) NeonLavender else TextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    color = if (isSelected) NeonLavender else TextMuted,
                                    fontSize = 9.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeonLavender,
                                selectedTextColor = NeonLavender,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted,
                                indicatorColor = NeonViolet.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier.testTag(item.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CyberBackgroundGradient)
        ) {
            when (uiState.selectedNavTab) {
                0 -> DashboardScreen(
                    state = uiState,
                    onNavigateTab = { viewModel.setNavTab(it) }
                )
                1 -> DataCaptureScreen(
                    state = uiState,
                    onPhotoCaptured = { bitmap, uri, isDemo ->
                        viewModel.setCapturedPhoto(bitmap, uri, isDemo)
                    },
                    onClearPhoto = { viewModel.clearCapturedPhoto() },
                    onVoiceTranscriptChanged = { viewModel.setVoiceTranscript(it) },
                    onTypedObservationChanged = { viewModel.updateTypedObservation(it) },
                    onStartAnalysis = { viewModel.startMultimodalAnalysis() },
                    onLoadDemoData = { viewModel.loadDemoCaptureData() },
                    onConfirmSaveReport = { viewModel.confirmAndSaveSiteReport() },
                    onDismissConfirmation = { viewModel.dismissConfirmationDialog() },
                    onSelectManualActivity = { viewModel.setManualSelectedActivity(it) },
                    onUpdatePendingResult = { actName, actId, prog, delayH, delayR, obs ->
                        viewModel.updatePendingCaptureResult(actName, actId, prog, delayH, delayR, obs)
                    },
                    onSelectEvidenceForDetail = { viewModel.selectEvidenceForDetail(it) },
                    onDismissEvidenceDetail = { viewModel.dismissEvidenceDetail() },
                    onSetUploadType = { viewModel.setUploadType(it) },
                    onSelectCaptureTab = { viewModel.setSelectedCaptureTab(it) },
                    onSetUploadedPdf = { name, uri -> viewModel.setUploadedPdf(name, uri) },
                    onLoadSamplePdf = { viewModel.loadSamplePdfReport() },
                    onClearPdf = { viewModel.clearUploadedPdf() },
                    onStartPdfAnalysis = { viewModel.startPdfDocumentAnalysis() },
                    onSetUploadedSpreadsheet = { name, uri, rows -> viewModel.setUploadedSpreadsheet(name, uri, rows) },
                    onLoadSampleSpreadsheet = { viewModel.loadSampleSpreadsheet() },
                    onClearSpreadsheet = { viewModel.clearUploadedSpreadsheet() },
                    onSelectSpreadsheetRow = { viewModel.selectSpreadsheetRow(it) }
                )
                2 -> ScheduleScreen(
                    state = uiState,
                    onPackageFilter = { viewModel.setPackageFilter(it) },
                    onStatusFilter = { viewModel.setStatusFilter(it) },
                    onSearchQuery = { viewModel.setSearchQuery(it) },
                    onExplainActivity = { viewModel.showExplainability(it) },
                    onEditActivity = { viewModel.showEditActivity(it) }
                )
                3 -> DelayPredictionScreen(
                    state = uiState,
                    onNavigateWhatIf = { viewModel.setNavTab(4) }
                )
                4 -> WhatIfSimulatorScreen(
                    state = uiState,
                    onPromptChange = { viewModel.updateWhatIfPrompt(it) },
                    onRunSimulation = { viewModel.runWhatIfSimulation(it) }
                )
                5 -> RiskRecoveryScreen(
                    state = uiState,
                    onApplyStrategy = { viewModel.applyRecoveryStrategy(it) }
                )
                6 -> CopilotScreen(
                    state = uiState,
                    onInputChange = { viewModel.updateCopilotInput(it) },
                    onSendMessage = { viewModel.sendCopilotMessage(it) }
                )
                7 -> TimelineGanttScreen(
                    state = uiState
                )
                8 -> DailySummaryScreen(
                    state = uiState,
                    onInsightAction = { insight ->
                        if (insight.tier == "CRITICAL") {
                            viewModel.setNavTab(4)
                        } else if (insight.tier == "WARNING") {
                            viewModel.setNavTab(3)
                        } else {
                            viewModel.setNavTab(5)
                        }
                    }
                )
            }

            // Success Commit Notification Banner
            AnimatedVisibility(
                visible = uiState.showCommitSuccessSnackbar,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CyberViolet900)
                        .border(1.dp, StatusGreen.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = StatusGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiState.commitSnackbarMessage,
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        IconButton(
                            onClick = { viewModel.dismissCommitSnackbar() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = TextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // AI Explainability Modal
            if (uiState.isExplainabilityOpen && uiState.selectedActivityForDetail != null) {
                AiExplainabilityModal(
                    activity = uiState.selectedActivityForDetail!!,
                    onDismiss = { viewModel.dismissExplainability() },
                    onNavigateWhatIf = { viewModel.setNavTab(4) }
                )
            }

            // Manual Edit Activity Dialog
            if (uiState.isEditActivityOpen && uiState.selectedActivityForDetail != null) {
                ManualEditActivityDialog(
                    activity = uiState.selectedActivityForDetail!!,
                    onDismiss = { viewModel.dismissEditActivity() },
                    onSave = { id, qty, mp, prg, st ->
                        viewModel.updateActivityManual(id, qty, mp, prg, st)
                    }
                )
            }
        }
    }
}
