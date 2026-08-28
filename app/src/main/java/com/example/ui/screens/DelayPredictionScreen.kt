package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
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
import com.example.model.WeatherForecastItem
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
fun DelayPredictionScreen(
    state: SiteGptState,
    onNavigateWhatIf: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val criticalAct = state.activities.firstOrNull { it.activityId == "ACT-B04-01" } ?: state.activities.first()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Column {
            Text(
                text = "Delay Prediction & Weather Intelligence",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "AI prediction engine combines real-time site telemetry, CPM float deficits, productivity velocity, and coastal weather forecasts.",
                color = TextSecondary,
                fontSize = 11.sp
            )
        }

        // Section 4.1: Critical Delay Prediction Card
        Card(
            modifier = Modifier.fillMaxWidth().testTag("delay_prediction_card"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = CyberViolet800),
            border = androidx.compose.foundation.BorderStroke(1.dp, StatusRed.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                CyberViolet700.copy(alpha = 0.4f),
                                CyberViolet800
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
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "High Risk Delay: ${criticalAct.activityName}",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "${criticalAct.activityId} • ${criticalAct.packageName}",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(StatusRed.copy(alpha = 0.2f))
                            .border(1.dp, StatusRed.copy(alpha = 0.6f), RoundedCornerShape(100.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${criticalAct.delayProbability}% Delay Prob",
                            color = StatusRed,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3 Metrics Row: Expected Delay, Forecast Date, Risk Score
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricMiniBox(
                        label = "Expected Delay",
                        value = "+6 Days",
                        color = StatusRed,
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniBox(
                        label = "Forecast End",
                        value = criticalAct.forecastEnd,
                        color = NeonLavender,
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniBox(
                        label = "AI Risk Score",
                        value = "${criticalAct.riskScore} / 100",
                        color = StatusRed,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Productivity Velocity Comparison
                Text(
                    text = "Productivity Rate vs Baseline Benchmark",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Actual: ${criticalAct.productivityRate} ${criticalAct.unit}/day",
                        color = StatusRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Planned: ${criticalAct.plannedProductivityRate} ${criticalAct.unit}/day",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                // Productivity deficit bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(CyberViolet900)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(criticalAct.productivityRate.toFloat() / criticalAct.plannedProductivityRate.toFloat())
                            .height(8.dp)
                            .background(StatusRed)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⚠️ Deficit: -29.6% productivity variance due to batching turnaround & manpower shortfall.",
                    color = TextMuted,
                    fontSize = 10.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Downstream Dependency Propagation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CyberViolet900)
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "🔗 Downstream Cascading Impact:",
                            color = NeonLavender,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "• ACT-B04-03: Pier Cap P12 Staging (Direct delay +5 days)\n• ACT-B04-04: Launching Gantry Span 12-13 (Direct delay +7 days)",
                            color = TextSecondary,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action to What-If
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(100.dp))
                        .background(Brush.horizontalGradient(listOf(NeonViolet, NeonPink)))
                        .clickable { onNavigateWhatIf() }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Simulate Recovery in What-If Engine",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Section 4.2: Weather-To-Action Intelligence Module
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Weather-To-Action Intelligence",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(NeonViolet.copy(alpha = 0.2f))
                    .border(1.dp, NeonLavender.copy(alpha = 0.4f), RoundedCornerShape(100.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "SIMULATED CHENNAI SENSORS",
                    color = NeonLavender,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 4-Day Forecast Cards List
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            state.weatherForecast.forEach { item ->
                WeatherActionCard(item = item)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun MetricMiniBox(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(CyberViolet900)
            .border(1.dp, CyberViolet700, RoundedCornerShape(10.dp))
            .padding(8.dp)
    ) {
        Column {
            Text(text = label, color = TextMuted, fontSize = 9.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun WeatherActionCard(item: WeatherForecastItem) {
    val riskColor = when (item.riskLevel) {
        "HIGH" -> StatusRed
        "MEDIUM" -> StatusAmber
        else -> StatusGreen
    }

    Card(
        modifier = Modifier.fillMaxWidth().testTag("weather_card_${item.dayName}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CyberViolet800),
        border = androidx.compose.foundation.BorderStroke(1.dp, riskColor.copy(alpha = 0.4f))
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
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(riskColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when {
                                item.rainProbability > 60 -> Icons.Default.WaterDrop
                                item.rainProbability > 30 -> Icons.Default.CloudQueue
                                else -> Icons.Default.WbSunny
                            },
                            contentDescription = null,
                            tint = riskColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "${item.dayName} (${item.date})",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = item.condition,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(riskColor.copy(alpha = 0.2f))
                        .border(1.dp, riskColor.copy(alpha = 0.5f), RoundedCornerShape(100.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${item.riskLevel} RISK",
                        color = riskColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Weather Metrics (Temp, Rain %, Wind)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Thermostat, contentDescription = null, tint = NeonLavender, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "${item.tempC}°C", color = TextSecondary, fontSize = 10.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.WaterDrop, contentDescription = null, tint = NeonPink, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "${item.rainProbability}% Rain", color = if (item.rainProbability > 50) StatusRed else TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Air, contentDescription = null, tint = NeonLavender, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "${item.windSpeedKmh} km/h Wind", color = TextSecondary, fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Affected Activities
            if (item.affectedActivities.isNotEmpty()) {
                Text(
                    text = "Affected: ${item.affectedActivities.joinToString(", ")}",
                    color = TextPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Concrete Actionable Recommendation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberViolet900)
                    .padding(8.dp)
            ) {
                Text(
                    text = "💡 Action: ${item.recommendedAction}",
                    color = NeonLavender,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
