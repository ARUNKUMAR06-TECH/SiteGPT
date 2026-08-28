package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.state.SiteGptState
import com.example.ui.theme.CyberViolet700
import com.example.ui.theme.CyberViolet800
import com.example.ui.theme.CyberViolet900
import com.example.ui.theme.NeonLavender
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.StatusAmber
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DashboardScreen(
    state: SiteGptState,
    onNavigateTab: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val overview = state.projectOverview
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Critical Health Alert Strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            StatusRed.copy(alpha = 0.25f),
                            CyberViolet800
                        )
                    )
                )
                .border(1.dp, StatusRed.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                .clickable { onNavigateTab(3) }
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .testTag("health_alert_banner")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(StatusRed.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = StatusRed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "PROJECT HEALTH: ${overview.healthIndicator}",
                            color = StatusRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Pier P12 casting velocity is 29% below baseline. Weather alert tomorrow.",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = NeonLavender,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Section 1: Main Project Overview Card
        Card(
            modifier = Modifier.fillMaxWidth().testTag("project_overview_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CyberViolet800),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonViolet.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                CyberViolet700.copy(alpha = 0.5f),
                                CyberViolet800
                            )
                        )
                    )
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = overview.projectName,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "${overview.location} • Contract: ${overview.projectId}",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(StatusRed.copy(alpha = 0.2f))
                            .border(1.dp, StatusRed.copy(alpha = 0.5f), RoundedCornerShape(100.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${overview.daysBehindAhead} Days Behind",
                            color = StatusRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Progress Bar Planned vs Actual
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Actual Progress", color = TextSecondary, fontSize = 11.sp)
                        Text(
                            text = "${overview.actualProgress}%",
                            color = NeonLavender,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Planned Progress", color = TextSecondary, fontSize = 11.sp)
                        Text(
                            text = "${overview.plannedProgress}%",
                            color = TextPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Dual progress indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(CyberViolet900)
                ) {
                    // Planned target ghost
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(overview.plannedProgress.toFloat() / 100f)
                            .height(12.dp)
                            .background(NeonViolet.copy(alpha = 0.35f))
                    )
                    // Actual fill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(overview.actualProgress.toFloat() / 100f)
                            .height(12.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(NeonViolet, NeonPink)
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dates & Schedule Variance Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = StatusRed,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Variance: ${overview.scheduleVariance}%",
                            color = StatusRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "Baseline: ${overview.plannedEndDate} | Forecast: ${overview.forecastEndDate}",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Section 1.2: Enterprise KPI Cards (Section 1 specs)
        Text(
            text = "Activity Portfolio & Health Metrics",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            KpiMetricCard(
                title = "Total Activities",
                count = "24",
                subtitle = "Across 4 Packages",
                color = NeonLavender,
                modifier = Modifier.weight(1f)
            )
            KpiMetricCard(
                title = "Completed",
                count = "7",
                subtitle = "29.1% WBS Done",
                color = StatusGreen,
                modifier = Modifier.weight(1f)
            )
            KpiMetricCard(
                title = "Critical / Delayed",
                count = "4",
                subtitle = "Bridge B04 / Road R01",
                color = StatusRed,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            KpiMetricCard(
                title = "On Track",
                count = "9",
                subtitle = "Stable Execution",
                color = NeonViolet,
                modifier = Modifier.weight(1f)
            )
            KpiMetricCard(
                title = "At-Risk",
                count = "4",
                subtitle = "Productivity Slip",
                color = StatusAmber,
                modifier = Modifier.weight(1f)
            )
            KpiMetricCard(
                title = "Budget Spent",
                count = "₹840 Cr",
                subtitle = "of ₹1,420 Cr (59%)",
                color = NeonPink,
                modifier = Modifier.weight(1f)
            )
        }

        // Section 1.3: Planned vs Actual Velocity & Delay Distribution
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = CyberViolet800),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700)
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(CyberViolet700.copy(alpha = 0.4f), CyberViolet800)
                        )
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Package Delay & Velocity Breakdown",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                PackageDelayRow(
                    packageName = "Bridge Package B04",
                    progress = 48,
                    delayDays = -18,
                    statusColor = StatusRed,
                    activitiesCount = "6 Acts"
                )
                Spacer(modifier = Modifier.height(10.dp))
                PackageDelayRow(
                    packageName = "Road Package R01",
                    progress = 67,
                    delayDays = -4,
                    statusColor = StatusAmber,
                    activitiesCount = "7 Acts"
                )
                Spacer(modifier = Modifier.height(10.dp))
                PackageDelayRow(
                    packageName = "Bridge Package B01",
                    progress = 89,
                    delayDays = 0,
                    statusColor = StatusGreen,
                    activitiesCount = "6 Acts"
                )
                Spacer(modifier = Modifier.height(10.dp))
                PackageDelayRow(
                    packageName = "Drainage Package D02",
                    progress = 85,
                    delayDays = 0,
                    statusColor = StatusGreen,
                    activitiesCount = "5 Acts"
                )
            }
        }

        // Section 1.4: Quick Action Launchpad
        Text(
            text = "Planning-to-Execution Intelligence Loop",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickLaunchCard(
                title = "Capture DPR",
                desc = "Upload & Extract",
                icon = Icons.Default.PendingActions,
                accentColor = NeonPink,
                onClick = { onNavigateTab(1) },
                modifier = Modifier.weight(1f)
            )
            QuickLaunchCard(
                title = "What-If Engine",
                desc = "Simulate Scenarios",
                icon = Icons.Default.Psychology,
                accentColor = NeonViolet,
                onClick = { onNavigateTab(4) },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickLaunchCard(
                title = "Weather Impact",
                desc = "Rain Risk Action",
                icon = Icons.Default.Cloud,
                accentColor = NeonLavender,
                onClick = { onNavigateTab(3) },
                modifier = Modifier.weight(1f)
            )
            QuickLaunchCard(
                title = "SiteGPT Copilot",
                desc = "Ask AI Assistant",
                icon = Icons.Default.AutoAwesome,
                accentColor = NeonPink,
                onClick = { onNavigateTab(6) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun KpiMetricCard(
    title: String,
    count: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(CyberViolet800)
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(text = title, color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = count, color = color, fontSize = 18.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, color = TextMuted, fontSize = 9.sp)
        }
    }
}

@Composable
fun PackageDelayRow(
    packageName: String,
    progress: Int,
    delayDays: Int,
    statusColor: Color,
    activitiesCount: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(statusColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = packageName,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "($activitiesCount)", color = TextMuted, fontSize = 10.sp)
            }
            Text(
                text = if (delayDays < 0) "$delayDays days" else "On Schedule",
                color = statusColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(CyberViolet900)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress / 100f)
                        .height(6.dp)
                        .background(statusColor)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "$progress%", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun QuickLaunchCard(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        CyberViolet700.copy(alpha = 0.5f),
                        CyberViolet800
                    )
                )
            )
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(accentColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = desc,
                color = TextSecondary,
                fontSize = 10.sp
            )
        }
    }
}
