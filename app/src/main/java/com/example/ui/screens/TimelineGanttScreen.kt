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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Warning
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
import com.example.model.SiteActivity
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
fun TimelineGanttScreen(
    state: SiteGptState,
    modifier: Modifier = Modifier
) {
    var selectedActivityId by remember { mutableStateOf<String?>("ACT-B04-01") }

    val months = listOf("JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Section Header
        Column {
            Text(
                text = "CPM Schedule Timeline & Gantt View",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Planned Baseline vs Actual Progress vs Delay Forecast on Critical Path network.",
                color = TextSecondary,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Gantt Legend
        Card(
            modifier = Modifier.fillMaxWidth().testTag("gantt_legend_card"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CyberViolet800),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GanttLegendItem(label = "Completed", color = StatusGreen)
                GanttLegendItem(label = "Planned Base", color = NeonViolet.copy(alpha = 0.5f))
                GanttLegendItem(label = "Critical Path", color = StatusRed)
                GanttLegendItem(label = "Forecast Delay", color = StatusRed.copy(alpha = 0.5f))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Month Header Strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(CyberViolet900)
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Box(modifier = Modifier.width(130.dp)) {
                Text(text = "ACTIVITY / WBS", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                months.forEach { m ->
                    Text(text = m, color = if (m == "AUG") NeonLavender else TextMuted, fontSize = 9.sp, fontWeight = if (m == "AUG") FontWeight.Black else FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Gantt Rows List
        LazyColumn(
            modifier = Modifier.fillMaxSize().testTag("gantt_activity_list"),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.activities, key = { it.activityId }) { act ->
                GanttActivityRow(
                    activity = act,
                    isSelected = selectedActivityId == act.activityId,
                    onClick = { selectedActivityId = act.activityId }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun GanttLegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, color = TextSecondary, fontSize = 9.sp)
    }
}

@Composable
fun GanttActivityRow(
    activity: SiteActivity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val barColor = when {
        activity.actualProgress == 100 -> StatusGreen
        activity.isCriticalPath -> StatusRed
        activity.actualProgress > 0 -> NeonViolet
        else -> TextMuted
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("gantt_row_${activity.activityId}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) CyberViolet700 else CyberViolet800
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) NeonLavender else if (activity.isCriticalPath) StatusRed.copy(alpha = 0.4f) else Color.Transparent
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Activity Name & Package
                Column(modifier = Modifier.width(130.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (activity.isCriticalPath) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(StatusRed, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = activity.activityId,
                            color = if (activity.isCriticalPath) StatusRed else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Text(
                        text = activity.activityName,
                        color = TextSecondary,
                        fontSize = 9.sp,
                        maxLines = 1
                    )
                }

                // Gantt Visual Bar Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(CyberViolet900)
                ) {
                    // Position mapping calculation (sample relative timeline positions)
                    val (startFraction, widthFraction) = when (activity.activityId) {
                        "ACT-R01-01" -> Pair(0.0f, 0.25f)
                        "ACT-B01-01" -> Pair(0.15f, 0.25f)
                        "ACT-D02-01" -> Pair(0.05f, 0.22f)
                        "ACT-B04-02" -> Pair(0.25f, 0.22f)
                        "ACT-B04-01" -> Pair(0.32f, 0.35f)
                        "ACT-B04-03" -> Pair(0.42f, 0.32f)
                        "ACT-B04-04" -> Pair(0.55f, 0.38f)
                        "ACT-B01-02" -> Pair(0.30f, 0.25f)
                        "ACT-B01-03" -> Pair(0.22f, 0.30f)
                        "ACT-R01-02" -> Pair(0.22f, 0.30f)
                        "ACT-R01-03" -> Pair(0.40f, 0.32f)
                        "ACT-D02-02" -> Pair(0.25f, 0.40f)
                        else -> Pair(0.3f, 0.3f)
                    }

                    // Background planned slot
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(startFraction + widthFraction)
                            .padding(start = (startFraction * 180).dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(NeonViolet.copy(alpha = 0.25f))
                    )

                    // Actual Progress Fill
                    val actualWidth = widthFraction * (activity.actualProgress / 100f)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth((startFraction + actualWidth).coerceAtMost(1f))
                            .padding(start = (startFraction * 180).dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(barColor)
                    )

                    // Delay extension ghost
                    if (activity.delayDays > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth((startFraction + widthFraction + 0.08f).coerceAtMost(1f))
                                .padding(start = ((startFraction + widthFraction) * 180).dp)
                                .height(20.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(StatusRed.copy(alpha = 0.5f))
                        )
                    }
                }
            }

            // Expanded Detail on Click
            if (isSelected) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(CyberViolet900)
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Start: ${activity.actualStart} • Forecast: ${activity.forecastEnd}",
                            color = TextSecondary,
                            fontSize = 9.sp
                        )
                        Text(
                            text = "Progress: ${activity.actualProgress}% • Qty: ${activity.actualQuantity} ${activity.unit}",
                            color = NeonLavender,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
