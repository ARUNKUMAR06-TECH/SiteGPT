package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.L6Activity
import com.example.model.RiskTier
import com.example.state.AppUiState
import com.example.ui.components.RiskBadge
import com.example.ui.components.VarianceBadge
import com.example.ui.theme.BlueElectric
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.StatusAmber
import com.example.ui.theme.StatusAmberBg
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenBg
import com.example.ui.theme.StatusRed
import com.example.ui.theme.StatusRedBg
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun RisksScreen(
    state: AppUiState,
    modifier: Modifier = Modifier
) {
    val criticalActivities = state.activities.filter { it.riskTier == RiskTier.CRITICAL }
    val atRiskActivities = state.activities.filter { it.riskTier == RiskTier.AT_RISK }
    val onTrackActivities = state.activities.filter { it.riskTier == RiskTier.ON_TRACK }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            RisksHeader()
        }

        // Running Counts Summary Bar
        item {
            RiskSummaryChipsRow(
                criticalCount = criticalActivities.size,
                atRiskCount = atRiskActivities.size,
                onTrackCount = onTrackActivities.size
            )
        }

        // Critical Tier Group
        item {
            RiskGroupHeader(
                title = "🔴 Critical Variance (< -10%)",
                count = criticalActivities.size,
                color = StatusRed
            )
        }

        if (criticalActivities.isEmpty()) {
            item {
                AllCriticalResolvedCard()
            }
        } else {
            items(criticalActivities, key = { it.id }) { act ->
                RiskActivityItem(activity = act, tier = RiskTier.CRITICAL)
            }
        }

        // At Risk Tier Group
        item {
            RiskGroupHeader(
                title = "🟡 At Risk Activities (-1% to -10%)",
                count = atRiskActivities.size,
                color = StatusAmber
            )
        }

        items(atRiskActivities, key = { it.id }) { act ->
            RiskActivityItem(activity = act, tier = RiskTier.AT_RISK)
        }

        // On Track Tier Group
        item {
            RiskGroupHeader(
                title = "🟢 On Track Activities (≥ 0% Variance)",
                count = onTrackActivities.size,
                color = StatusGreen
            )
        }

        items(onTrackActivities, key = { it.id }) { act ->
            RiskActivityItem(activity = act, tier = RiskTier.ON_TRACK)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RisksHeader() {
    Column {
        Text(
            text = "Risk Engine & Variance Matrix",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "AI-calculated risk tiers updated dynamically from daily site updates",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun RiskSummaryChipsRow(
    criticalCount: Int,
    atRiskCount: Int,
    onTrackCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RiskChip(
            label = "Critical",
            count = criticalCount,
            color = StatusRed,
            bgColor = StatusRedBg,
            modifier = Modifier.weight(1f)
        )
        RiskChip(
            label = "At Risk",
            count = atRiskCount,
            color = StatusAmber,
            bgColor = StatusAmberBg,
            modifier = Modifier.weight(1f)
        )
        RiskChip(
            label = "On Track",
            count = onTrackCount,
            color = StatusGreen,
            bgColor = StatusGreenBg,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RiskChip(
    label: String,
    count: Int,
    color: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        bgColor.copy(alpha = 0.35f),
                        com.example.ui.theme.CyberViolet800
                    )
                )
            )
            .border(1.dp, color.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp, horizontal = 12.dp)
    ) {
        Column {
            Text(text = label, color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$count",
                color = color,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
private fun RiskGroupHeader(
    title: String,
    count: Int,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "$count Items",
            color = TextMuted,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun AllCriticalResolvedCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.CyberViolet800),
        border = androidx.compose.foundation.BorderStroke(1.dp, StatusGreen.copy(alpha = 0.45f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            StatusGreen.copy(alpha = 0.15f),
                            com.example.ui.theme.CyberViolet800
                        )
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = StatusGreen,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "No Critical Activities",
                    color = StatusGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "Foundation F102 was recovered to At Risk (-7%) via verified field progress approval.",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun RiskActivityItem(
    activity: L6Activity,
    tier: RiskTier
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.CyberViolet800),
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.NeonViolet.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            com.example.ui.theme.CyberViolet700.copy(alpha = 0.35f),
                            com.example.ui.theme.CyberViolet800
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${activity.id} • Grid ${activity.location}",
                    color = com.example.ui.theme.NeonLavender,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                VarianceBadge(variance = activity.variance)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = activity.name,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Progress: ${activity.actualProgress}% (Planned: ${activity.plannedProgress}%)",
                color = TextSecondary,
                fontSize = 11.sp
            )

            // AI Recommendation Box
            if (activity.recommendation.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            com.example.ui.theme.CyberViolet700,
                            RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, com.example.ui.theme.NeonViolet.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = com.example.ui.theme.NeonLavender,
                            modifier = Modifier.size(14.dp).padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI Recommendation: ${activity.recommendation}",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}
