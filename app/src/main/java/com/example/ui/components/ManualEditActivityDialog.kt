package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.window.Dialog
import com.example.model.ActivityStatus
import com.example.model.SiteActivity
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
fun ManualEditActivityDialog(
    activity: SiteActivity,
    onDismiss: () -> Unit,
    onSave: (String, Double, Int, Int, ActivityStatus) -> Unit
) {
    var editQty by remember { mutableStateOf(activity.actualQuantity.toString()) }
    var editManpower by remember { mutableStateOf(activity.manpower.toString()) }
    var editProgress by remember { mutableStateOf(activity.actualProgress.toString()) }
    var selectedStatus by remember { mutableStateOf(activity.status) }

    val statusList = listOf(
        ActivityStatus.ON_TRACK,
        ActivityStatus.AT_RISK,
        ActivityStatus.DELAYED,
        ActivityStatus.CRITICAL,
        ActivityStatus.COMPLETED
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("manual_edit_activity_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CyberViolet800),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonViolet.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(CyberViolet700.copy(alpha = 0.5f), CyberViolet800)
                        )
                    )
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = NeonLavender,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Edit Activity Telemetry",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${activity.activityId} • ${activity.activityName}",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Executed Qty Input
                OutlinedTextField(
                    value = editQty,
                    onValueChange = { editQty = it },
                    label = { Text("Executed Quantity (${activity.unit})", color = TextSecondary, fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = NeonLavender,
                        unfocusedBorderColor = CyberViolet700
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Progress % Input
                OutlinedTextField(
                    value = editProgress,
                    onValueChange = { editProgress = it },
                    label = { Text("Actual Progress %", color = TextSecondary, fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = NeonLavender,
                        unfocusedBorderColor = CyberViolet700
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Manpower Input
                OutlinedTextField(
                    value = editManpower,
                    onValueChange = { editManpower = it },
                    label = { Text("Active Manpower Count", color = TextSecondary, fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = NeonLavender,
                        unfocusedBorderColor = CyberViolet700
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Status Chips
                Text(text = "Engineering Health Status:", color = TextSecondary, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    statusList.take(3).forEach { st ->
                        val isSelected = selectedStatus == st
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NeonViolet else CyberViolet900)
                                .clickable { selectedStatus = st }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = st.shortLabel,
                                color = if (isSelected) Color.White else TextMuted,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Save Button
                Button(
                    onClick = {
                        val qty = editQty.toDoubleOrNull() ?: activity.actualQuantity
                        val mp = editManpower.toIntOrNull() ?: activity.manpower
                        val prg = editProgress.toIntOrNull() ?: activity.actualProgress
                        onSave(activity.activityId, qty, mp, prg, selectedStatus)
                    },
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(listOf(NeonViolet, NeonPink)),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Save & Re-calculate Schedule",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
