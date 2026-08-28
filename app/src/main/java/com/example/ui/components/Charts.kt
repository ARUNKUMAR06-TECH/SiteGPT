package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.state.TrendPoint
import com.example.ui.theme.StatusAmber
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PlannedVsActualBarChart(
    items: List<Pair<String, Pair<Int, Int>>>, // Name -> (Planned%, Actual%)
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        com.example.ui.theme.CyberViolet700.copy(alpha = 0.4f),
                        com.example.ui.theme.CyberViolet800
                    )
                ),
                RoundedCornerShape(18.dp)
            )
            .border(1.dp, com.example.ui.theme.NeonViolet.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Planned vs. Actual by Activity",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).background(com.example.ui.theme.NeonViolet.copy(alpha = 0.5f), CircleShape))
                Text(text = " Planned", color = TextSecondary, fontSize = 11.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.size(8.dp).background(com.example.ui.theme.NeonPink, CircleShape))
                Text(text = " Actual", color = TextSecondary, fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        items.forEach { (name, pair) ->
            val (planned, actual) = pair
            val animatedActual by animateFloatAsState(
                targetValue = actual / 100f,
                animationSpec = tween(600),
                label = "bar_$name"
            )
            val plannedFraction = planned / 100f

            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = name,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                    Text(
                        text = "$actual% / $planned%",
                        color = if (actual < planned) StatusAmber else StatusGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(com.example.ui.theme.CyberViolet900, RoundedCornerShape(5.dp))
                ) {
                    // Planned Ghost Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(plannedFraction)
                            .height(10.dp)
                            .background(com.example.ui.theme.NeonViolet.copy(alpha = 0.35f), RoundedCornerShape(5.dp))
                    )
                    // Actual Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedActual)
                            .height(10.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        com.example.ui.theme.NeonViolet,
                                        com.example.ui.theme.NeonPink
                                    )
                                ),
                                RoundedCornerShape(5.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun RiskDistributionChart(
    onTrackCount: Int,
    atRiskCount: Int,
    criticalCount: Int,
    modifier: Modifier = Modifier
) {
    val total = (onTrackCount + atRiskCount + criticalCount).coerceAtLeast(1)
    val onTrackRatio = onTrackCount.toFloat() / total
    val atRiskRatio = atRiskCount.toFloat() / total
    val criticalRatio = criticalCount.toFloat() / total

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        com.example.ui.theme.CyberViolet700.copy(alpha = 0.4f),
                        com.example.ui.theme.CyberViolet800
                    )
                ),
                RoundedCornerShape(18.dp)
            )
            .border(1.dp, com.example.ui.theme.NeonViolet.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Risk Distribution",
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Segmented bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .background(com.example.ui.theme.CyberViolet900, RoundedCornerShape(7.dp))
                .padding(2.dp)
        ) {
            if (onTrackCount > 0) {
                Box(
                    modifier = Modifier
                        .weight(onTrackRatio)
                        .fillMaxSize()
                        .background(StatusGreen, RoundedCornerShape(5.dp))
                )
            }
            if (atRiskCount > 0) {
                Spacer(modifier = Modifier.width(2.dp))
                Box(
                    modifier = Modifier
                        .weight(atRiskRatio)
                        .fillMaxSize()
                        .background(StatusAmber, RoundedCornerShape(5.dp))
                )
            }
            if (criticalCount > 0) {
                Spacer(modifier = Modifier.width(2.dp))
                Box(
                    modifier = Modifier
                        .weight(criticalRatio)
                        .fillMaxSize()
                        .background(StatusRed, RoundedCornerShape(5.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RiskLegendItem(color = StatusGreen, label = "On Track", count = onTrackCount)
            RiskLegendItem(color = StatusAmber, label = "At Risk", count = atRiskCount)
            RiskLegendItem(color = StatusRed, label = "Critical", count = criticalCount)
        }
    }
}

@Composable
private fun RiskLegendItem(color: Color, label: String, count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$label: $count",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ProgressTrendLineChart(
    points: List<TrendPoint>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        com.example.ui.theme.CyberViolet700.copy(alpha = 0.4f),
                        com.example.ui.theme.CyberViolet800
                    )
                ),
                RoundedCornerShape(18.dp)
            )
            .border(1.dp, com.example.ui.theme.NeonViolet.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Progress Velocity Trend",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Live Model Forecast",
                color = com.example.ui.theme.NeonLavender,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(vertical = 8.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val count = points.size
                if (count < 2) return@Canvas

                val stepX = width / (count - 1)
                val maxVal = 80f // scale

                // Draw subtle grid lines
                for (i in 0..4) {
                    val y = height * (i / 4f)
                    drawLine(
                        color = Color(0xFF323A45).copy(alpha = 0.4f),
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1f
                    )
                }

                val plannedPath = Path()
                val actualPath = Path()

                points.forEachIndexed { i, pt ->
                    val x = i * stepX
                    val yPlanned = height - (pt.plannedPercent / maxVal * height)
                    val yActual = height - (pt.actualPercent / maxVal * height)

                    if (i == 0) {
                        plannedPath.moveTo(x, yPlanned)
                        actualPath.moveTo(x, yActual)
                    } else {
                        plannedPath.lineTo(x, yPlanned)
                        actualPath.lineTo(x, yActual)
                    }
                }

                // Draw Planned dashed / faint line
                drawPath(
                    path = plannedPath,
                    color = Color(0xFF94A3B8).copy(alpha = 0.6f),
                    style = Stroke(width = 2.5f, cap = StrokeCap.Round)
                )

                // Draw Actual vibrant line
                drawPath(
                    path = actualPath,
                    brush = Brush.horizontalGradient(
                        listOf(
                            com.example.ui.theme.SafetyOrangeDeep,
                            com.example.ui.theme.SafetyOrange,
                            com.example.ui.theme.SafetyOrangeBright
                        )
                    ),
                    style = Stroke(width = 4.5f, cap = StrokeCap.Round)
                )

                // Draw points on Actual line
                points.forEachIndexed { i, pt ->
                    val x = i * stepX
                    val yActual = height - (pt.actualPercent / maxVal * height)
                    drawCircle(
                        color = Color(0xFF1E2228),
                        radius = 6f,
                        center = Offset(x, yActual)
                    )
                    drawCircle(
                        color = if (i == count - 1) StatusGreen else com.example.ui.theme.SafetyOrange,
                        radius = 4f,
                        center = Offset(x, yActual)
                    )
                }
            }
        }

        // Labels row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            points.forEach { pt ->
                Text(
                    text = pt.label,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}
