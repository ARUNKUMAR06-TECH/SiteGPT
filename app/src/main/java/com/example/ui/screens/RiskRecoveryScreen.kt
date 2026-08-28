package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RecoveryStrategy
import com.example.model.RiskItem
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
fun RiskRecoveryScreen(
    state: SiteGptState,
    onApplyStrategy: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableStateOf(0) } // 0: Recovery Strategies, 1: Risk Register

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Switcher Tab
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(100.dp))
                .background(CyberViolet800)
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(100.dp))
                    .background(if (selectedSection == 0) NeonViolet else Color.Transparent)
                    .clickable { selectedSection = 0 }
                    .padding(vertical = 8.dp)
                    .testTag("tab_recovery_strategies"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = if (selectedSection == 0) Color.White else TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Recovery Planner (4)",
                        color = if (selectedSection == 0) Color.White else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(100.dp))
                    .background(if (selectedSection == 1) NeonViolet else Color.Transparent)
                    .clickable { selectedSection = 1 }
                    .padding(vertical = 8.dp)
                    .testTag("tab_risk_register"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = if (selectedSection == 1) Color.White else TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Risk Matrix (5)",
                        color = if (selectedSection == 1) Color.White else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedSection == 0) {
            // Recovery Planner Section
            LazyColumn(
                modifier = Modifier.fillMaxSize().testTag("recovery_strategies_list"),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = "Side-by-Side Recovery Strategy Evaluator",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Evaluate tradeoff between time recovery, resource requirements, cost impact, and operational risk. Tap 'Activate' to update live schedule forecast.",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                    )
                }

                items(state.recoveryStrategies, key = { it.id }) { strategy ->
                    RecoveryStrategyCard(
                        strategy = strategy,
                        onToggle = { onApplyStrategy(strategy.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        } else {
            // Risk Register Section
            LazyColumn(
                modifier = Modifier.fillMaxSize().testTag("risk_register_list"),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Ranked Risk Matrix (Criticality × Prob × Impact)",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Identified through schedule float deficit, productivity anomalies, and environmental telemetry.",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                    )
                }

                items(state.risks, key = { it.riskId }) { risk ->
                    RiskMatrixCard(risk = risk)
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun RecoveryStrategyCard(
    strategy: RecoveryStrategy,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("strategy_card_${strategy.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CyberViolet800),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (strategy.isApplied) StatusGreen else CyberViolet700
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(
                            if (strategy.isApplied) StatusGreen.copy(alpha = 0.15f) else CyberViolet700.copy(alpha = 0.35f),
                            CyberViolet800
                        )
                    )
                )
                .padding(16.dp)
        ) {
            // Title & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = strategy.name,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${strategy.category} • ${strategy.targetActivityOrPackage}",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(StatusGreen.copy(alpha = 0.2f))
                        .border(1.dp, StatusGreen.copy(alpha = 0.5f), RoundedCornerShape(100.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "+${strategy.expectedDaysRecovered} Days Recovered",
                        color = StatusGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Metrics: Resources, Cost, Risk
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberViolet900)
                        .padding(8.dp)
                ) {
                    Column {
                        Text(text = "Cost Impact", color = TextMuted, fontSize = 9.sp)
                        Text(
                            text = "₹${strategy.costImpactLakhs} Lakhs",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberViolet900)
                        .padding(8.dp)
                ) {
                    Column {
                        Text(text = "Risk Level", color = TextMuted, fontSize = 9.sp)
                        Text(
                            text = "${strategy.riskLevel} Risk",
                            color = if (strategy.riskLevel == "Low") StatusGreen else StatusAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Resource requirement text
            Text(
                text = "🛠️ Resources: ${strategy.resourceRequirement}",
                color = TextSecondary,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Pros & Cons
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                strategy.advantages.take(2).forEach { adv ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.ThumbUp, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = adv, color = TextPrimary, fontSize = 10.sp)
                    }
                }
                strategy.disadvantages.take(1).forEach { dis ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.ThumbDown, contentDescription = null, tint = StatusAmber, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = dis, color = TextMuted, fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Button
            Button(
                onClick = onToggle,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .testTag("btn_apply_strategy_${strategy.id}"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (strategy.isApplied) StatusGreen.copy(alpha = 0.3f) else CyberViolet700
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (strategy.isApplied) Icons.Default.CheckCircle else Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = if (strategy.isApplied) StatusGreen else NeonLavender,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (strategy.isApplied) "Active in Live Schedule (Tap to Remove)" else "Activate Recovery Strategy",
                        color = if (strategy.isApplied) StatusGreen else TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun RiskMatrixCard(risk: RiskItem) {
    val severityColor = when (risk.severity) {
        "CRITICAL" -> StatusRed
        "HIGH" -> StatusRed
        "MEDIUM" -> StatusAmber
        else -> StatusGreen
    }

    Card(
        modifier = Modifier.fillMaxWidth().testTag("risk_card_${risk.riskId}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CyberViolet800),
        border = androidx.compose.foundation.BorderStroke(1.dp, severityColor.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(CyberViolet700.copy(alpha = 0.3f), CyberViolet800)
                    )
                )
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(severityColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = risk.riskId,
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = risk.category,
                        color = NeonLavender,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(severityColor.copy(alpha = 0.2f))
                        .border(1.dp, severityColor.copy(alpha = 0.5f), RoundedCornerShape(100.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${risk.severity} • Score: ${risk.score}",
                        color = severityColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = risk.activityName,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Text(
                text = "${risk.packageName} • Probability: ${risk.probability}% • Impact: +${risk.impactDays}d (₹${risk.impactCostLakhs}L)",
                color = TextSecondary,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Cause Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberViolet900)
                    .padding(8.dp)
            ) {
                Column {
                    Text(text = "Root Cause:", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = risk.cause, color = TextSecondary, fontSize = 10.sp, lineHeight = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Mitigation Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NeonViolet.copy(alpha = 0.15f))
                    .padding(8.dp)
            ) {
                Column {
                    Text(text = "Recommended Mitigation:", color = NeonLavender, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = risk.mitigation, color = TextPrimary, fontSize = 10.sp, lineHeight = 14.sp)
                }
            }
        }
    }
}
