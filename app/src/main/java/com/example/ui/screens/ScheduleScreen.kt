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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.model.ActivityStatus
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
fun ScheduleScreen(
    state: SiteGptState,
    onPackageFilter: (String) -> Unit,
    onStatusFilter: (String) -> Unit,
    onSearchQuery: (String) -> Unit,
    onExplainActivity: (SiteActivity) -> Unit,
    onEditActivity: (SiteActivity) -> Unit,
    modifier: Modifier = Modifier
) {
    val packages = listOf("ALL", "Bridge Package B04", "Road Package R01", "Bridge Package B01", "Drainage Package D02")
    val statuses = listOf("ALL", "Critical", "Delayed", "At Risk", "On Track", "Completed")

    val filteredActivities = state.activities.filter { act ->
        val matchPkg = state.selectedPackageFilter == "ALL" || act.packageName == state.selectedPackageFilter
        val matchStatus = state.selectedStatusFilter == "ALL" || act.status.label.equals(state.selectedStatusFilter, ignoreCase = true)
        val matchSearch = state.searchQuery.isBlank() ||
                act.activityName.contains(state.searchQuery, ignoreCase = true) ||
                act.activityId.contains(state.searchQuery, ignoreCase = true) ||
                act.location.contains(state.searchQuery, ignoreCase = true)
        matchPkg && matchStatus && matchSearch
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = onSearchQuery,
            placeholder = { Text("Search by activity, ID, or location (e.g. Pier P12)...", color = TextMuted, fontSize = 11.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = NeonLavender,
                    modifier = Modifier.size(18.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("schedule_search_input"),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = NeonLavender,
                unfocusedBorderColor = CyberViolet700,
                focusedContainerColor = CyberViolet800,
                unfocusedContainerColor = CyberViolet800
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Package Filter Horizontal Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            packages.forEach { pkg ->
                val isSelected = state.selectedPackageFilter == pkg
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(if (isSelected) NeonViolet else CyberViolet800)
                        .border(
                            1.dp,
                            if (isSelected) NeonLavender else CyberViolet700,
                            RoundedCornerShape(100.dp)
                        )
                        .clickable { onPackageFilter(pkg) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("filter_pkg_${pkg.replace(" ", "_")}")
                ) {
                    Text(
                        text = if (pkg == "ALL") "All Packages" else pkg,
                        color = if (isSelected) Color.White else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Status Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            statuses.forEach { st ->
                val isSelected = state.selectedStatusFilter == st
                val color = when (st) {
                    "Critical" -> StatusRed
                    "Delayed" -> StatusRed
                    "At Risk" -> StatusAmber
                    "On Track" -> NeonViolet
                    "Completed" -> StatusGreen
                    else -> NeonLavender
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(if (isSelected) color.copy(alpha = 0.3f) else CyberViolet900)
                        .border(
                            1.dp,
                            if (isSelected) color else CyberViolet700,
                            RoundedCornerShape(100.dp)
                        )
                        .clickable { onStatusFilter(st) }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .testTag("filter_status_$st")
                ) {
                    Text(
                        text = st,
                        color = if (isSelected) color else TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Activity Count Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hierarchical WBS Activities (${filteredActivities.size})",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Levels L1 → L6",
                color = TextSecondary,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Activity List
        LazyColumn(
            modifier = Modifier.fillMaxSize().testTag("schedule_activity_list"),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredActivities, key = { it.activityId }) { activity ->
                ActivityScheduleCard(
                    activity = activity,
                    onExplain = { onExplainActivity(activity) },
                    onEdit = { onEditActivity(activity) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun ActivityScheduleCard(
    activity: SiteActivity,
    onExplain: () -> Unit,
    onEdit: () -> Unit
) {
    val statusColor = when (activity.status) {
        ActivityStatus.COMPLETED -> StatusGreen
        ActivityStatus.ON_TRACK -> NeonViolet
        ActivityStatus.AT_RISK -> StatusAmber
        ActivityStatus.DELAYED -> StatusRed
        ActivityStatus.CRITICAL -> StatusRed
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("activity_card_${activity.activityId}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CyberViolet800),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (activity.isCriticalPath) StatusRed.copy(alpha = 0.5f) else CyberViolet700
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(CyberViolet700.copy(alpha = 0.35f), CyberViolet800)
                    )
                )
                .padding(14.dp)
        ) {
            // Level & Hierarchy Breadcrumb
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(NeonViolet.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = activity.hierarchyLevel,
                            color = NeonLavender,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = activity.activityId,
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (activity.isCriticalPath) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(StatusRed.copy(alpha = 0.2f))
                                .border(1.dp, StatusRed.copy(alpha = 0.5f), RoundedCornerShape(100.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "CRITICAL PATH",
                                color = StatusRed,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(statusColor.copy(alpha = 0.2f))
                            .border(1.dp, statusColor.copy(alpha = 0.5f), RoundedCornerShape(100.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = activity.status.shortLabel,
                            color = statusColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Activity Name & Location
            Text(
                text = activity.activityName,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "${activity.packageName} • ${activity.location}",
                color = TextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Bar & Quantities
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progress: ${activity.actualProgress}% (Planned: ${activity.plannedProgress}%)",
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Variance: ${if (activity.variance >= 0) "+${activity.variance}%" else "${activity.variance}%"}",
                    color = if (activity.variance >= 0) StatusGreen else StatusRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Dual bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(CyberViolet900)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(activity.plannedProgress / 100f)
                        .height(8.dp)
                        .background(NeonViolet.copy(alpha = 0.3f))
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(activity.actualProgress / 100f)
                        .height(8.dp)
                        .background(statusColor)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quantity & Dates Metadata
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Qty: ${activity.actualQuantity}/${activity.plannedQuantity} ${activity.unit}",
                    color = TextSecondary,
                    fontSize = 10.sp
                )
                Text(
                    text = "Forecast: ${activity.forecastEnd} ${if (activity.delayDays > 0) "(+${activity.delayDays}d delay)" else ""}",
                    color = if (activity.delayDays > 0) StatusRed else TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = if (activity.delayDays > 0) FontWeight.Bold else FontWeight.Normal
                )
            }

            // Reason / Root Cause Callout
            if (activity.delayReason.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberViolet900.copy(alpha = 0.8f))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "⚠️ Root Cause: ${activity.delayReason}",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons: Explain AI & Edit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberViolet700)
                        .clickable { onExplain() }
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NeonLavender,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Explain AI",
                        color = NeonLavender,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberViolet700)
                        .clickable { onEdit() }
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Edit Mapping",
                        color = TextPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
