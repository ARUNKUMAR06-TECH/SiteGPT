package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ConfidenceTier
import com.example.model.ExtractedFieldData
import com.example.model.ScoredMatch
import com.example.state.AppUiState
import com.example.ui.components.AiAnimationOverlay
import com.example.ui.components.ConfidenceBadge
import com.example.ui.components.EditDataDialog
import com.example.ui.components.ExplainabilityBottomSheet
import com.example.ui.components.SuccessScheduleDialog
import com.example.ui.theme.BlueElectric
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.StatusAmber
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun FieldCaptureScreen(
    state: AppUiState,
    onLocationChange: (String) -> Unit,
    onObservationChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onUnitChange: (String) -> Unit,
    onWorkforceChange: (String) -> Unit,
    onTriggerPhotoCapture: () -> Unit,
    onLoadDemoReport: () -> Unit,
    onAnalyzeFieldUpdate: () -> Unit,
    onSelectMatch: (ScoredMatch) -> Unit,
    onShowExplainability: (Boolean) -> Unit,
    onShowEditDialog: (Boolean) -> Unit,
    onApplyEditedData: (Double, Int, String) -> Unit,
    onApproveMatch: (String) -> Unit,
    onDismissSuccessDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    var unitMenuExpanded by remember { mutableStateOf(false) }
    val unitsList = listOf("m³", "kg", "m²", "Nos", "%")

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                FieldCaptureHeader()
            }

            // Value Loop Bar
            item {
                ValueLoopBar()
            }

            // Quick Demo Load Button (Prominent Glowing Pill)
            item {
                DemoAutoFillButton(onLoadDemo = onLoadDemoReport)
            }

            // Main Field Input Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.CyberViolet800),
                    border = androidx.compose.foundation.BorderStroke(
                        1.2.dp,
                        com.example.ui.theme.NeonViolet.copy(alpha = 0.35f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        com.example.ui.theme.CyberViolet700.copy(alpha = 0.4f),
                                        com.example.ui.theme.CyberViolet800
                                    )
                                )
                            )
                    ) {
                        // Gloss Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(com.example.ui.theme.CardGlossHeaderGradient)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "FIELD DATA ENTRY",
                                color = com.example.ui.theme.NeonLavender,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "SITE SENSOR / LOG",
                                color = com.example.ui.theme.NeonViolet,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Daily Site Observation & Metrics",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            // Fixed Project Field
                            OutlinedTextField(
                                value = state.inputProject,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Project Hierarchy") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SurfaceBorder,
                                    unfocusedBorderColor = SurfaceBorder,
                                    unfocusedTextColor = TextSecondary,
                                    focusedTextColor = TextSecondary
                                )
                            )

                            // Location Input
                            OutlinedTextField(
                                value = state.inputLocation,
                                onValueChange = onLocationChange,
                                label = { Text("Location Tag (e.g. F102, Pier P12)") },
                                placeholder = { Text("F102", color = TextMuted) },
                                modifier = Modifier.fillMaxWidth().testTag("location_input"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = com.example.ui.theme.NeonLavender,
                                    focusedLabelColor = com.example.ui.theme.NeonLavender,
                                    unfocusedTextColor = TextPrimary,
                                    focusedTextColor = TextPrimary
                                )
                            )

                            // Multiline Observation
                            OutlinedTextField(
                                value = state.inputObservation,
                                onValueChange = onObservationChange,
                                label = { Text("Field Observation / Daily Log") },
                                placeholder = { Text("Describe what happened at the site today...", color = TextMuted) },
                                minLines = 3,
                                maxLines = 5,
                                modifier = Modifier.fillMaxWidth().testTag("observation_input"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = com.example.ui.theme.NeonLavender,
                                    focusedLabelColor = com.example.ui.theme.NeonLavender,
                                    unfocusedTextColor = TextPrimary,
                                    focusedTextColor = TextPrimary
                                )
                            )

                            // Quantity and Unit Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = state.inputQuantity,
                                    onValueChange = onQuantityChange,
                                    label = { Text("Quantity") },
                                    placeholder = { Text("18", color = TextMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.weight(1.3f).testTag("quantity_input"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = com.example.ui.theme.NeonLavender,
                                        focusedLabelColor = com.example.ui.theme.NeonLavender,
                                        unfocusedTextColor = TextPrimary,
                                        focusedTextColor = TextPrimary
                                    )
                                )

                                // Unit selector
                                Box(modifier = Modifier.weight(1f)) {
                                    OutlinedTextField(
                                        value = state.inputUnit,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Unit") },
                                        trailingIcon = {
                                            IconButton(onClick = { unitMenuExpanded = true }) {
                                                Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null, tint = CyanAccent)
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth().clickable { unitMenuExpanded = true },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = com.example.ui.theme.NeonLavender,
                                            focusedLabelColor = com.example.ui.theme.NeonLavender,
                                            unfocusedTextColor = TextPrimary,
                                            focusedTextColor = TextPrimary
                                        )
                                    )
                                    DropdownMenu(
                                        expanded = unitMenuExpanded,
                                        onDismissRequest = { unitMenuExpanded = false },
                                        modifier = Modifier.background(com.example.ui.theme.CyberViolet800)
                                    ) {
                                        unitsList.forEach { unit ->
                                            DropdownMenuItem(
                                                text = { Text(unit, color = TextPrimary) },
                                                onClick = {
                                                    onUnitChange(unit)
                                                    unitMenuExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            // Workforce input
                            OutlinedTextField(
                                value = state.inputWorkforce,
                                onValueChange = onWorkforceChange,
                                label = { Text("Active Workforce (Headcount)") },
                                placeholder = { Text("14", color = TextMuted) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth().testTag("workforce_input"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = com.example.ui.theme.NeonLavender,
                                    focusedLabelColor = com.example.ui.theme.NeonLavender,
                                    unfocusedTextColor = TextPrimary,
                                    focusedTextColor = TextPrimary
                                )
                            )

                            // Site Photo Upload / Evidence Button
                            SitePhotoSection(
                                hasPhoto = state.hasSitePhoto,
                                onTriggerCapture = onTriggerPhotoCapture
                            )
                        }
                    }
                }
            }

            // Analyze CTA Button (Glowing Gradient Pill)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(100.dp))
                        .background(com.example.ui.theme.NeonButtonGradient)
                        .clickable(enabled = !state.isAnalyzing) { onAnalyzeFieldUpdate() }
                        .testTag("analyze_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "🤖 ANALYZE FIELD UPDATE",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            // AI Processing Animation checklist (3.5 sec)
            if (state.isAnalyzing) {
                item {
                    AiAnimationOverlay(currentStep = state.analysisChecklistStep)
                }
            }

            // AI Extraction Results Card
            if (state.hasAnalysisResult && state.extractedData != null) {
                item {
                    AiExtractionCard(data = state.extractedData)
                }

                // AI Ranked Matches Section
                item {
                    AiRankedMatchesSection(
                        matches = state.rankedMatches,
                        selectedMatch = state.selectedMatchForApproval,
                        onSelectMatch = onSelectMatch,
                        onShowExplainability = { onShowExplainability(true) },
                        onShowEditDialog = { onShowEditDialog(true) },
                        onApprove = { onApproveMatch("Approved") }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }

        // Explainability Bottom Sheet
        if (state.showExplainabilitySheet && state.selectedMatchForApproval != null) {
            ExplainabilityBottomSheet(
                match = state.selectedMatchForApproval,
                onDismiss = { onShowExplainability(false) }
            )
        }

        // Edit Data Dialog
        if (state.showEditDataDialog && state.extractedData != null) {
            EditDataDialog(
                initialData = state.extractedData,
                onDismiss = { onShowEditDialog(false) },
                onSave = onApplyEditedData
            )
        }

        // Schedule Update Success Dialog
        if (state.showSuccessDialog && state.successUpdateData != null) {
            SuccessScheduleDialog(
                data = state.successUpdateData,
                onDismiss = onDismissSuccessDialog
            )
        }
    }
}

@Composable
private fun FieldCaptureHeader() {
    Column {
        Text(
            text = "Daily Field Progress",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "Site intelligence capture & schedule alignment engine",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun ValueLoopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp, horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val stages = listOf("Plan", "Capture", "Understand", "Link", "Verify", "Update", "Predict", "Act")
            stages.forEachIndexed { index, stage ->
                val isActive = stage == "Capture" || stage == "Understand" || stage == "Link"
                Text(
                    text = stage,
                    fontSize = 10.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    color = if (isActive) CyanAccent else TextMuted
                )
                if (index < stages.size - 1) {
                    Text(text = "→", fontSize = 10.sp, color = TextMuted)
                }
            }
        }
    }
}

@Composable
private fun DemoAutoFillButton(onLoadDemo: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        com.example.ui.theme.CyberViolet600,
                        com.example.ui.theme.NeonViolet,
                        com.example.ui.theme.NeonPink
                    )
                )
            )
            .clickable { onLoadDemo() }
            .testTag("load_demo_button"),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "⚡ LOAD DEMO REPORT (F102 / 18 m³ / 14 workers)",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 0.4.sp
            )
        }
    }
}

@Composable
private fun SitePhotoSection(
    hasPhoto: Boolean,
    onTriggerCapture: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = onTriggerCapture,
            modifier = Modifier.fillMaxWidth().height(46.dp).testTag("photo_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = com.example.ui.theme.NeonLavender),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (hasPhoto) StatusGreen else com.example.ui.theme.NeonViolet
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (hasPhoto) Icons.Default.CheckCircle else Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = if (hasPhoto) StatusGreen else com.example.ui.theme.NeonLavender,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (hasPhoto) "📷 Photo Attached & AI Vision Ready" else "📷 Capture / Upload Site Photo",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }

        // Static AI Vision Evidence card (Prompt requirement)
        if (hasPhoto) {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.CyberViolet800),
                border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.NeonViolet.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = com.example.ui.theme.NeonLavender,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "AI Vision Evidence — Demo Vision Core",
                                color = com.example.ui.theme.NeonLavender,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(StatusGreen.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Verified", color = StatusGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Image(
                        painter = painterResource(id = R.drawable.img_site_evidence),
                        contentDescription = "Site evidence photo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "\"Concrete pouring activity detected. Formwork visible.\"",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun AiExtractionCard(data: ExtractedFieldData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.CyberViolet800),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, com.example.ui.theme.NeonViolet.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            com.example.ui.theme.CyberViolet700.copy(alpha = 0.4f),
                            com.example.ui.theme.CyberViolet800
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = com.example.ui.theme.NeonLavender,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AI Natural Language & Metrics Extraction",
                    color = com.example.ui.theme.NeonLavender,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Extraction Grid Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExtractionGridItem("Activity", data.activityType, modifier = Modifier.weight(1.5f))
                ExtractionGridItem("Location", data.location, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExtractionGridItem("Quantity", "${data.quantity} ${data.unit}", modifier = Modifier.weight(1f))
                ExtractionGridItem("Workforce", "${data.workforce} workers", modifier = Modifier.weight(1f))
                ExtractionGridItem("Date", data.dateText, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ExtractionGridItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(com.example.ui.theme.CyberViolet700, RoundedCornerShape(10.dp))
            .border(1.dp, com.example.ui.theme.NeonViolet.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column {
            Text(text = label, color = com.example.ui.theme.NeonLavender.copy(alpha = 0.7f), fontSize = 10.sp)
            Text(
                text = value,
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun AiRankedMatchesSection(
    matches: List<ScoredMatch>,
    selectedMatch: ScoredMatch?,
    onSelectMatch: (ScoredMatch) -> Unit,
    onShowExplainability: () -> Unit,
    onShowEditDialog: () -> Unit,
    onApprove: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ranked Schedule Matches",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Deterministic Weighted Alignment",
                color = TextMuted,
                fontSize = 11.sp
            )
        }

        matches.forEach { match ->
            val isSelected = selectedMatch?.activity?.id == match.activity.id
            MatchCardItem(
                match = match,
                isSelected = isSelected,
                onSelect = { onSelectMatch(match) },
                onShowExplainability = onShowExplainability
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Explainability Trigger Button
        OutlinedButton(
            onClick = onShowExplainability,
            modifier = Modifier.fillMaxWidth().height(44.dp).testTag("why_this_match_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = com.example.ui.theme.NeonLavender),
            border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.NeonViolet.copy(alpha = 0.6f))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = com.example.ui.theme.NeonLavender, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("🔍 \"Why this match?\" — View Mathematical Breakdown", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Human In The Loop Decision Actions
        Text(
            text = "Human-in-the-Loop Verification (Mandatory Sign-off)",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Edit Extracted Data
            OutlinedButton(
                onClick = onShowEditDialog,
                modifier = Modifier.weight(1f).height(46.dp).testTag("edit_data_button"),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit Data", color = TextPrimary, fontSize = 12.sp)
                }
            }

            // Reject & Pick Another
            OutlinedButton(
                onClick = { /* already able to pick candidates by tapping list */ },
                modifier = Modifier.weight(1f).height(46.dp),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StatusRed.copy(alpha = 0.5f))
            ) {
                Text("Select Other", color = StatusRed, fontSize = 12.sp)
            }
        }

        // Primary Approve Match Button (Glowing Pill Button)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            StatusGreen,
                            Color(0xFF10B981),
                            com.example.ui.theme.NeonViolet
                        )
                    )
                )
                .clickable { onApprove() }
                .testTag("approve_match_button"),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Approve Match & Update Schedule",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun MatchCardItem(
    match: ScoredMatch,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onShowExplainability: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("match_item_${match.activity.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) com.example.ui.theme.CyberViolet700 else com.example.ui.theme.CyberViolet800
        ),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) com.example.ui.theme.NeonViolet else com.example.ui.theme.SurfaceBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = isSelected,
                        onClick = onSelect,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = com.example.ui.theme.NeonLavender,
                            unselectedColor = TextMuted
                        )
                    )
                    Column {
                        Text(
                            text = match.activity.id,
                            color = com.example.ui.theme.NeonLavender,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = match.rankBadge,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                ConfidenceBadge(
                    confidence = match.overallConfidence,
                    tier = match.confidenceTier
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = match.activity.name,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 36.dp)
            )

            Text(
                text = "Location: ${match.activity.location} • Phase: ${match.activity.phase}",
                color = TextMuted,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 36.dp, top = 2.dp)
            )

            Text(
                text = match.explanation,
                color = TextSecondary,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 36.dp, top = 4.dp)
            )
        }
    }
}
