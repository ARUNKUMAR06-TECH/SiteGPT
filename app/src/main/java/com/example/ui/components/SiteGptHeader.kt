package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.state.SiteGptState
import com.example.ui.theme.CyberViolet700
import com.example.ui.theme.CyberViolet800
import com.example.ui.theme.CyberViolet900
import com.example.ui.theme.NeonLavender
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.StatusRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class NavModuleItem(
    val index: Int,
    val title: String,
    val icon: String,
    val testTag: String
)

@Composable
fun SiteGptHeader(
    state: SiteGptState,
    onTabSelected: (Int) -> Unit,
    onRoleSelected: (String) -> Unit,
    onResetDemo: () -> Unit,
    modifier: Modifier = Modifier
) {
    var roleMenuExpanded by remember { mutableStateOf(false) }
    val roles = listOf("Project Manager", "Site Engineer", "Planning Engineer", "Construction Manager", "Project Controls")

    val modules = listOf(
        NavModuleItem(0, "Dashboard", "📊", "tab_dashboard"),
        NavModuleItem(1, "Data Capture", "📥", "tab_data_capture"),
        NavModuleItem(2, "Schedule & Track", "📋", "tab_schedule"),
        NavModuleItem(3, "Delay & Weather", "⚠️", "tab_delay_weather"),
        NavModuleItem(4, "What-If Simulator", "🔮", "tab_what_if"),
        NavModuleItem(5, "Risk & Recovery", "🛡️", "tab_risk_recovery"),
        NavModuleItem(6, "SiteGPT Copilot", "🤖", "tab_copilot"),
        NavModuleItem(7, "Timeline Gantt", "📅", "tab_timeline"),
        NavModuleItem(8, "Daily Summary", "📝", "tab_daily_summary")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(CyberViolet900)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand & AI indicator
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(NeonViolet, NeonPink)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Construction,
                        contentDescription = "SiteGPT",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "SiteGPT",
                            color = TextPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(NeonViolet.copy(alpha = 0.25f))
                                .border(1.dp, NeonViolet.copy(alpha = 0.5f), RoundedCornerShape(100.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = NeonLavender,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "INTELLIGENCE",
                                    color = NeonLavender,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Text(
                        text = "Chennai Elevated Highway Project (CHE-HW-2026)",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Right controls: Role selector & Reset
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Role Picker Pill
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(CyberViolet700)
                            .border(1.dp, NeonViolet.copy(alpha = 0.4f), RoundedCornerShape(100.dp))
                            .clickable { roleMenuExpanded = true }
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = NeonLavender,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = state.activeRole,
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    DropdownMenu(
                        expanded = roleMenuExpanded,
                        onDismissRequest = { roleMenuExpanded = false },
                        modifier = Modifier.background(CyberViolet800)
                    ) {
                        roles.forEach { role ->
                            DropdownMenuItem(
                                text = { Text(role, color = TextPrimary, fontSize = 12.sp) },
                                onClick = {
                                    onRoleSelected(role)
                                    roleMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))
                IconButton(
                    onClick = onResetDemo,
                    modifier = Modifier.size(32.dp).testTag("header_reset_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Demo",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Horizontal Module Navigation Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            modules.forEach { mod ->
                val isSelected = state.selectedNavTab == mod.index
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(
                            if (isSelected) {
                                Brush.horizontalGradient(listOf(NeonViolet, NeonPink))
                            } else {
                                Brush.horizontalGradient(
                                    listOf(
                                        CyberViolet700.copy(alpha = 0.6f),
                                        CyberViolet800.copy(alpha = 0.6f)
                                    )
                                )
                            }
                        )
                        .border(
                            1.dp,
                            if (isSelected) NeonLavender else CyberViolet700.copy(alpha = 0.6f),
                            RoundedCornerShape(100.dp)
                        )
                        .clickable { onTabSelected(mod.index) }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                        .testTag(mod.testTag),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = mod.icon,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = mod.title,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(CyberViolet700.copy(alpha = 0.6f))
        )
    }
}
