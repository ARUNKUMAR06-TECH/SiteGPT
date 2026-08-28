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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AuditLogRecord
import com.example.model.FieldReportRecord
import com.example.model.UserRole
import com.example.state.AppUiState
import com.example.ui.theme.BlueElectric
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ProfileScreen(
    state: AppUiState,
    onRoleSelect: (UserRole) -> Unit,
    onResetDemo: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSectionIndex by remember { mutableStateOf(0) } // 0: Role, 1: Field Reports, 2: Audit Logs

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            ProfileHeader(currentRole = state.currentRole)
        }

        // Section Tabs: Role Switcher / Field Reports / Audit Log
        item {
            TabRow(
                selectedTabIndex = selectedSectionIndex,
                containerColor = com.example.ui.theme.CyberViolet800,
                contentColor = com.example.ui.theme.NeonLavender,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSectionIndex]),
                        color = com.example.ui.theme.NeonLavender,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, com.example.ui.theme.NeonViolet.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            ) {
                Tab(
                    selected = selectedSectionIndex == 0,
                    onClick = { selectedSectionIndex = 0 },
                    text = { Text("Role Switcher", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedSectionIndex == 1,
                    onClick = { selectedSectionIndex = 1 },
                    text = { Text("Field Reports (${state.fieldReports.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedSectionIndex == 2,
                    onClick = { selectedSectionIndex = 2 },
                    text = { Text("Audit Log (${state.auditLogs.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        when (selectedSectionIndex) {
            0 -> {
                // Role Switcher Cards
                item {
                    Text(
                        text = "Active Persona & Access Profile",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(UserRole.entries, key = { it.name }) { role ->
                    RoleCard(
                        role = role,
                        isSelected = state.currentRole == role,
                        onSelect = { onRoleSelect(role) }
                    )
                }

                // Architecture value loop
                item {
                    ValueLoopCard()
                }

                // Future Roadmap Note
                item {
                    FutureRoadmapCard()
                }

                // Reset Demo
                item {
                    OutlinedButton(
                        onClick = onResetDemo,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = com.example.ui.theme.NeonLavender),
                        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.NeonViolet.copy(alpha = 0.5f))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = com.example.ui.theme.NeonLavender, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Reset Demo Baseline State", color = TextPrimary, fontSize = 13.sp)
                        }
                    }
                }
            }
            1 -> {
                // Field Reports List
                item {
                    Text(
                        text = "My Field Reports (Submitted & Linked)",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(state.fieldReports, key = { it.id }) { report ->
                    FieldReportItem(report = report)
                }
            }
            2 -> {
                // Audit Log List
                item {
                    Text(
                        text = "AI Decision History & Schedule Audit Trail",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(state.auditLogs, key = { it.id }) { log ->
                    AuditLogItem(log = log)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProfileHeader(currentRole: UserRole) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.CyberViolet800),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, com.example.ui.theme.NeonViolet.copy(alpha = 0.45f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            com.example.ui.theme.CyberViolet700.copy(alpha = 0.45f),
                            com.example.ui.theme.CyberViolet800
                        )
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                com.example.ui.theme.NeonViolet,
                                com.example.ui.theme.NeonPink
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Engineering, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "ProjectPulse AI Intelligence Hub",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Role: ${currentRole.title} (${currentRole.badgeLabel})",
                    color = com.example.ui.theme.NeonLavender,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun RoleCard(
    role: UserRole,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) com.example.ui.theme.CyberViolet700 else com.example.ui.theme.CyberViolet800
        ),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) com.example.ui.theme.NeonViolet else com.example.ui.theme.SurfaceBorder
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(if (isSelected) com.example.ui.theme.NeonPink else TextMuted, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = role.title,
                        color = if (isSelected) com.example.ui.theme.NeonLavender else TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .background(com.example.ui.theme.CyberViolet600, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(text = role.badgeLabel, color = com.example.ui.theme.NeonLavender, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = role.description,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                modifier = Modifier.padding(start = 18.dp)
            )
        }
    }
}

@Composable
private fun ValueLoopCard() {
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
            Text(
                text = "Core Value Loop",
                color = com.example.ui.theme.NeonLavender,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Plan → Capture → Understand → Link → Verify → Update → Predict → Act",
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Not generic construction management software, but an AI intelligence layer linking master project schedules to ground execution.",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
private fun FutureRoadmapCard() {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = null, tint = com.example.ui.theme.NeonPink, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Future Production Roadmap",
                    color = com.example.ui.theme.NeonPink,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "• Native Primavera P6 (.xer) and MS Project (.xml) bidirectional sync\n• BIM 4D IFC digital twins & LiDAR drone volumetric scan integration\n• WhatsApp & Telegram automated multi-language field log audio bots\n• SAP / Oracle ERP material supply chain auto-reconciliation",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun FieldReportItem(report: FieldReportRecord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.CyberViolet800),
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            com.example.ui.theme.CyberViolet700.copy(alpha = 0.25f),
                            com.example.ui.theme.CyberViolet800
                        )
                    )
                )
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = report.id, color = com.example.ui.theme.NeonLavender, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(text = report.timestamp, color = TextMuted, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "\"${report.observation}\"",
                color = TextPrimary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Linked: ${report.linkedActivityId} (${report.linkedActivityName})",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .background(StatusGreen.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = "${report.confidence}% ${report.approvalStatus}", color = StatusGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AuditLogItem(log: AuditLogRecord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.CyberViolet800),
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.SurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            com.example.ui.theme.CyberViolet700.copy(alpha = 0.25f),
                            com.example.ui.theme.CyberViolet800
                        )
                    )
                )
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = log.id, color = com.example.ui.theme.NeonLavender, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(text = "${log.userRole} • ${log.timestamp}", color = TextMuted, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = log.observationSnippet,
                color = TextPrimary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Activity: ${log.finalActivityId} (Confidence: ${log.confidence}%)",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Text(
                    text = "Decision: ${log.decision}",
                    color = StatusGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
