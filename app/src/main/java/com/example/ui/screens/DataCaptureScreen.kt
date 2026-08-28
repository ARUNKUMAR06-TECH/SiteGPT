package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.ImageDecoder
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.model.SiteActivity
import com.example.model.SiteEvidenceReport
import com.example.model.SpreadsheetRowItem
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
import kotlinx.coroutines.delay

private fun generateSampleInspectionBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(480, 360, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply { isAntiAlias = true }

    // Sky & ground gradient
    paint.shader = LinearGradient(
        0f, 0f, 0f, 360f,
        AndroidColor.rgb(120, 160, 200),
        AndroidColor.rgb(80, 90, 100),
        Shader.TileMode.CLAMP
    )
    canvas.drawRect(0f, 0f, 480f, 360f, paint)
    paint.shader = null

    // Column Base Foundation
    paint.color = AndroidColor.rgb(160, 165, 170)
    canvas.drawRect(160f, 120f, 320f, 360f, paint)

    // Rebar Cage Vertical Bars (Rust/Steel Color)
    paint.color = AndroidColor.rgb(184, 75, 41)
    paint.strokeWidth = 6f
    for (x in 175..305 step 20) {
        canvas.drawLine(x.toFloat(), 50f, x.toFloat(), 340f, paint)
    }

    // Stirrup Ties (Horizontal bands)
    paint.color = AndroidColor.rgb(60, 60, 60)
    paint.strokeWidth = 4f
    for (y in 70..330 step 35) {
        canvas.drawLine(168f, y.toFloat(), 312f, y.toFloat(), paint)
    }

    // Scaffold Frame lines on the side
    paint.color = AndroidColor.rgb(220, 190, 50)
    paint.strokeWidth = 3f
    canvas.drawLine(100f, 80f, 100f, 360f, paint)
    canvas.drawLine(140f, 80f, 140f, 360f, paint)
    canvas.drawLine(100f, 140f, 140f, 140f, paint)
    canvas.drawLine(100f, 220f, 140f, 220f, paint)
    canvas.drawLine(100f, 300f, 140f, 300f, paint)

    // Stamp Text Banner
    paint.color = AndroidColor.argb(190, 15, 23, 42)
    canvas.drawRect(10f, 10f, 470f, 45f, paint)
    paint.color = AndroidColor.rgb(56, 189, 248)
    paint.textSize = 18f
    paint.strokeWidth = 1f
    canvas.drawText("SITEGPT VISION • CH. 12+800 COL-C12 REBAR [80%]", 20f, 34f, paint)

    return bitmap
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DataCaptureScreen(
    state: SiteGptState,
    onPhotoCaptured: (Bitmap?, String?, Boolean) -> Unit,
    onClearPhoto: () -> Unit,
    onVoiceTranscriptChanged: (String) -> Unit,
    onTypedObservationChanged: (String) -> Unit,
    onStartAnalysis: () -> Unit,
    onLoadDemoData: () -> Unit,
    onConfirmSaveReport: () -> Unit,
    onDismissConfirmation: () -> Unit,
    onSelectManualActivity: (String) -> Unit,
    onUpdatePendingResult: (String, String, Int, Int, String, String) -> Unit,
    onSelectEvidenceForDetail: (SiteEvidenceReport?) -> Unit,
    onDismissEvidenceDetail: () -> Unit,
    onSetUploadType: (String) -> Unit,
    onSelectCaptureTab: (Int) -> Unit = {},
    onSetUploadedPdf: (String?, String?) -> Unit = { _, _ -> },
    onLoadSamplePdf: () -> Unit = {},
    onClearPdf: () -> Unit = {},
    onStartPdfAnalysis: () -> Unit = {},
    onSetUploadedSpreadsheet: (String?, String?, List<SpreadsheetRowItem>) -> Unit = { _, _, _ -> },
    onLoadSampleSpreadsheet: () -> Unit = {},
    onClearSpreadsheet: () -> Unit = {},
    onSelectSpreadsheetRow: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Camera Capture Launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            onPhotoCaptured(bitmap, null, false)
        }
    }

    // Camera Permission Launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraLauncher.launch()
        } else {
            Toast.makeText(context, "Camera permission required for site photography", Toast.LENGTH_SHORT).show()
        }
    }

    // Gallery Photo Picker
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
                onPhotoCaptured(bitmap, uri.toString(), false)
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load photo: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // PDF Document Picker
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = uri.lastPathSegment?.substringAfterLast("/") ?: "Uploaded_DPR.pdf"
            onSetUploadedPdf(fileName, uri.toString())
            Toast.makeText(context, "Selected PDF: $fileName", Toast.LENGTH_SHORT).show()
        }
    }

    // Spreadsheet Picker
    val spreadsheetPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val fileName = uri.lastPathSegment?.substringAfterLast("/") ?: "Site_Progress_Data.xlsx"
            onSetUploadedSpreadsheet(fileName, uri.toString(), emptyList())
            Toast.makeText(context, "Selected Spreadsheet: $fileName", Toast.LENGTH_SHORT).show()
        }
    }

    // Voice Recording Timer Simulation
    var isRecording by remember { mutableStateOf(false) }
    var recordDurationSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordDurationSeconds = 0
            while (isRecording) {
                delay(1000)
                recordDurationSeconds++
            }
        }
    }

    val captureTabs = listOf("All-in-One", "📸 Site Photo", "🎙️ Voice Audio", "📄 DPR / PDF", "📊 Excel / CSV")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Header Section ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CyberViolet900.copy(alpha = 0.6f),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(NeonViolet, NeonPink))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Multimodal Data Capture",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Photo • Voice • DPR PDF • Excel Schedule Linking",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Live Status Chip
                    Surface(
                        color = StatusGreen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StatusGreen.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(StatusGreen)
                            )
                            Text(
                                text = "AI Engine Ready",
                                color = StatusGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // One-Tap Demo Prompt Quick Loader
                Button(
                    onClick = { onLoadDemoData() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("load_prompt_demo_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberViolet800
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonLavender.copy(alpha = 0.4f))
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NeonLavender,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "⚡ Load Test Case: Column C12 (80% + 2h Rain)",
                        color = NeonLavender,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // --- Modality Selector Tabs ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            captureTabs.forEachIndexed { index, tabName ->
                val isSelected = state.selectedCaptureTab == index
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onSelectCaptureTab(index) }
                        .testTag("capture_tab_$index"),
                    color = if (isSelected) NeonViolet.copy(alpha = 0.35f) else CyberViolet900.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) NeonLavender else CyberViolet700.copy(alpha = 0.4f)
                    )
                ) {
                    Text(
                        text = tabName,
                        color = if (isSelected) NeonLavender else TextMuted,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // --- Error Banner if any ---
        if (state.captureErrorMessage != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = StatusRed.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StatusRed.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Error, contentDescription = null, tint = StatusRed)
                    Text(
                        text = state.captureErrorMessage,
                        color = StatusRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // =========================================================================
        // MODALITY 1: SITE PHOTO CAPTURE & UPLOAD (Visible in All or Photo tab)
        // =========================================================================
        if (state.selectedCaptureTab == 0 || state.selectedCaptureTab == 1) {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("photo_capture_card"),
                colors = CardDefaults.cardColors(containerColor = CyberViolet900.copy(alpha = 0.45f)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = NeonLavender, modifier = Modifier.size(18.dp))
                            Text("1. Site Photo Evidence", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        if (state.capturedPhotoBitmap != null || state.isDemoPhoto) {
                            Surface(
                                color = StatusGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (state.isDemoPhoto) "DEMO PHOTO" else "PHOTO READY",
                                    color = StatusGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Display Captured Photo or Placeholder Buttons
                    val photoBitmap = state.capturedPhotoBitmap ?: if (state.isDemoPhoto) remember { generateSampleInspectionBitmap() } else null

                    if (photoBitmap != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, NeonLavender.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        ) {
                            Image(
                                bitmap = photoBitmap.asImageBitmap(),
                                contentDescription = "Captured Site Evidence",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // Overlay Actions: Retake & Delete
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    color = Color.Black.copy(alpha = 0.7f),
                                    shape = CircleShape,
                                    modifier = Modifier.clickable {
                                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                            cameraLauncher.launch()
                                        } else {
                                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Retake",
                                        tint = Color.White,
                                        modifier = Modifier.padding(8.dp).size(18.dp)
                                    )
                                }
                                Surface(
                                    color = Color.Black.copy(alpha = 0.7f),
                                    shape = CircleShape,
                                    modifier = Modifier.clickable { onClearPhoto() }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove",
                                        tint = StatusRed,
                                        modifier = Modifier.padding(8.dp).size(18.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        // Capture Options Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                        cameraLauncher.launch()
                                    } else {
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                },
                                modifier = Modifier.weight(1f).height(48.dp).testTag("open_camera_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = CyberViolet800),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonViolet.copy(alpha = 0.5f))
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = NeonLavender, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Camera", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }

                            OutlinedButton(
                                onClick = { galleryLauncher.launch("image/*") },
                                modifier = Modifier.weight(1f).height(48.dp).testTag("upload_gallery_button"),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700)
                            ) {
                                Icon(Icons.Default.Image, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Gallery", color = TextSecondary, fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    val sampleBitmap = generateSampleInspectionBitmap()
                                    onPhotoCaptured(sampleBitmap, "sample://column_c12_inspection.jpg", true)
                                },
                                modifier = Modifier.weight(1.1f).height(48.dp).testTag("sample_photo_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = CyberViolet700.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonPink.copy(alpha = 0.4f))
                            ) {
                                Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = NeonPink, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Sample Photo", color = NeonPink, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // =========================================================================
        // MODALITY 2: VOICE AUDIO & SPEECH-TO-TEXT (Visible in All or Voice tab)
        // =========================================================================
        if (state.selectedCaptureTab == 0 || state.selectedCaptureTab == 2) {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("voice_capture_card"),
                colors = CardDefaults.cardColors(containerColor = CyberViolet900.copy(alpha = 0.45f)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Mic, contentDescription = null, tint = NeonPink, modifier = Modifier.size(18.dp))
                            Text("2. Voice Note & Speech Transcription", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        if (isRecording) {
                            Surface(
                                color = StatusRed.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StatusRed.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "REC: ${String.format("%02d:%02d", recordDurationSeconds / 60, recordDurationSeconds % 60)}",
                                    color = StatusRed,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Recording Trigger Button & Live Visualizer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (isRecording) {
                                    isRecording = false
                                    if (state.voiceTranscript.isBlank()) {
                                        onVoiceTranscriptChanged("Column C12 reinforcement is 80 percent complete. Rain stopped work for two hours.")
                                    }
                                } else {
                                    isRecording = true
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("record_voice_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isRecording) StatusRed else CyberViolet800
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isRecording) StatusRed else NeonPink.copy(alpha = 0.5f)
                            )
                        ) {
                            Icon(
                                imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isRecording) "Stop Recording" else "Record Voice Note",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (state.voiceTranscript.isNotBlank()) {
                            OutlinedButton(
                                onClick = { onVoiceTranscriptChanged("") },
                                modifier = Modifier.height(46.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700)
                            ) {
                                Text("Clear", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                    }

                    // Quick Voice Observation Preset Chips
                    Text("Quick Observations:", color = TextMuted, fontSize = 11.sp)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val presets = listOf(
                            "Column C12 80% complete, 2h rain stoppage",
                            "Pier P12 concrete mixer delayed 35 min",
                            "DBM asphalt laying halted - bitumen shortage",
                            "Box Culvert C1-C4 100% installed"
                        )
                        presets.forEach { preset ->
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onVoiceTranscriptChanged(preset) },
                                color = CyberViolet800.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = preset,
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // Voice Transcription Input Area
                    OutlinedTextField(
                        value = state.voiceTranscript,
                        onValueChange = onVoiceTranscriptChanged,
                        modifier = Modifier.fillMaxWidth().testTag("voice_transcript_field"),
                        placeholder = {
                            Text("Voice transcript or audio notes will appear here. You can also type directly...", color = TextMuted, fontSize = 12.sp)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPink,
                            unfocusedBorderColor = CyberViolet700,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = CyberViolet900,
                            unfocusedContainerColor = CyberViolet900
                        ),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 2,
                        maxLines = 4
                    )
                }
            }
        }

        // =========================================================================
        // MODALITY 3: DPR / PDF DOCUMENT UPLOAD & EXTRACTION (Visible in All or DPR tab)
        // =========================================================================
        if (state.selectedCaptureTab == 0 || state.selectedCaptureTab == 3) {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("pdf_capture_card"),
                colors = CardDefaults.cardColors(containerColor = CyberViolet900.copy(alpha = 0.45f)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = StatusAmber, modifier = Modifier.size(18.dp))
                            Text("3. DPR / PDF Document Extraction", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        if (state.uploadedPdfName != null) {
                            Surface(
                                color = StatusAmber.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "PDF ATTACHED",
                                    color = StatusAmber,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    if (state.uploadedPdfName != null) {
                        // Uploaded PDF Card Preview
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = CyberViolet800.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StatusAmber.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(StatusAmber.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Description, contentDescription = null, tint = StatusAmber, modifier = Modifier.size(20.dp))
                                    }
                                    Column {
                                        Text(
                                            text = state.uploadedPdfName,
                                            color = TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "4 Pages • 1.8 MB • Package B04 Daily Log",
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Button(
                                        onClick = { onStartPdfAnalysis() },
                                        colors = ButtonDefaults.buttonColors(containerColor = StatusAmber.copy(alpha = 0.3f)),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.height(34.dp).testTag("extract_pdf_button")
                                    ) {
                                        Text("Extract", color = StatusAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    IconButton(
                                        onClick = { onClearPdf() },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Remove PDF", tint = TextMuted, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    } else {
                        // PDF Pick & Sample Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { pdfPickerLauncher.launch("application/pdf") },
                                modifier = Modifier.weight(1f).height(46.dp).testTag("upload_pdf_file_button"),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700)
                            ) {
                                Icon(Icons.Default.UploadFile, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Upload PDF", color = TextSecondary, fontSize = 12.sp)
                            }

                            Button(
                                onClick = { onLoadSamplePdf() },
                                modifier = Modifier.weight(1.3f).height(46.dp).testTag("load_sample_pdf_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = CyberViolet800),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StatusAmber.copy(alpha = 0.5f))
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = StatusAmber, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Sample DPR (B04 Pier)", color = StatusAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // =========================================================================
        // MODALITY 4: EXCEL / CSV SPREADSHEET PROGRESS PARSER (Visible in All or Excel tab)
        // =========================================================================
        if (state.selectedCaptureTab == 0 || state.selectedCaptureTab == 4) {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("excel_capture_card"),
                colors = CardDefaults.cardColors(containerColor = CyberViolet900.copy(alpha = 0.45f)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.TableChart, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(18.dp))
                            Text("4. Excel / CSV Progress Table", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        Surface(
                            color = StatusGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${state.spreadsheetRows.size} Rows",
                                color = StatusGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // SpreadSheet Action Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { spreadsheetPickerLauncher.launch("*/*") },
                            modifier = Modifier.weight(1f).height(42.dp).testTag("upload_spreadsheet_button"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700)
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Upload .XLSX / .CSV", color = TextSecondary, fontSize = 11.sp)
                        }

                        Button(
                            onClick = { onLoadSampleSpreadsheet() },
                            modifier = Modifier.weight(1.2f).height(42.dp).testTag("load_sample_spreadsheet_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = CyberViolet800),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StatusGreen.copy(alpha = 0.5f))
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Parse Sample Table", color = StatusGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Spreadsheet Tabular Row Items (Interactive - tap to parse and link schedule)
                    Text("Tap any spreadsheet row below to parse and link to WBS schedule:", color = TextMuted, fontSize = 11.sp)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        state.spreadsheetRows.forEachIndexed { index, row ->
                            val isSelected = state.selectedSpreadsheetRowIndex == index
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onSelectSpreadsheetRow(index) }
                                    .testTag("spreadsheet_row_$index"),
                                color = if (isSelected) StatusGreen.copy(alpha = 0.2f) else CyberViolet800.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) StatusGreen else CyberViolet700.copy(alpha = 0.4f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text(
                                                text = row.activityId,
                                                color = NeonLavender,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "• ${row.activityName}",
                                                color = TextPrimary,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Text(
                                            text = "Planned: ${row.plannedQty} ${row.unit} | Executed: ${row.actualQty} ${row.unit} (${row.actualProgress}%)",
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        if (row.delayHours > 0) {
                                            Surface(
                                                color = StatusRed.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = "-${row.delayHours}h Delay",
                                                    color = StatusRed,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = NeonLavender,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // =========================================================================
        // UNIFIED AI PIPELINE TRIGGER: ANALYZE EVIDENCE & LINK SCHEDULE
        // =========================================================================
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CyberViolet900.copy(alpha = 0.6f),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NeonViolet.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (state.isMultimodalAnalyzing) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LinearProgressIndicator(
                            progress = { state.multimodalProgress },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = NeonLavender,
                            trackColor = CyberViolet800
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = NeonLavender,
                                strokeWidth = 2.dp
                            )
                            Text(
                                text = state.multimodalStepText.ifBlank { "Analyzing multimodal evidence..." },
                                color = NeonLavender,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    Button(
                        onClick = { onStartAnalysis() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("analyze_evidence_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonLavender.copy(alpha = 0.6f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.horizontalGradient(listOf(CyberViolet700, NeonViolet, CyberViolet700))),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Analyze Evidence & Link Schedule",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // =========================================================================
        // RECENT SITE EVIDENCE AUDIT HISTORY
        // =========================================================================
        Text(
            text = "Recent Site Evidence Reports (${state.siteEvidenceHistory.size})",
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            state.siteEvidenceHistory.forEach { report ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectEvidenceForDetail(report) }
                        .testTag("evidence_history_item_${report.id}"),
                    color = CyberViolet900.copy(alpha = 0.45f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = report.activityId,
                                    color = NeonLavender,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "• ${report.activityName}",
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Surface(
                                color = if (report.progressVariance < 0) StatusRed.copy(alpha = 0.2f) else StatusGreen.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "${report.actualProgress}% (${if (report.progressVariance >= 0) "+${report.progressVariance}" else "${report.progressVariance}"}%)",
                                    color = if (report.progressVariance < 0) StatusRed else StatusGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        if (report.observation.isNotBlank()) {
                            Text(
                                text = report.observation,
                                color = TextSecondary,
                                fontSize = 11.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                if (report.source.photo) {
                                    Surface(color = CyberViolet800, shape = RoundedCornerShape(4.dp)) {
                                        Text("📸 Photo", color = NeonLavender, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                    }
                                }
                                if (report.source.voice) {
                                    Surface(color = CyberViolet800, shape = RoundedCornerShape(4.dp)) {
                                        Text("🎙️ Voice", color = NeonPink, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                    }
                                }
                                if (report.source.pdf) {
                                    Surface(color = CyberViolet800, shape = RoundedCornerShape(4.dp)) {
                                        Text("📄 PDF", color = StatusAmber, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                    }
                                }
                                if (report.source.spreadsheet) {
                                    Surface(color = CyberViolet800, shape = RoundedCornerShape(4.dp)) {
                                        Text("📊 Excel", color = StatusGreen, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                    }
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.CloudDone, contentDescription = null, tint = StatusGreen, modifier = Modifier.size(12.dp))
                                Text(
                                    text = "Firestore Synced",
                                    color = StatusGreen,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // =========================================================================
    // AI SCHEDULE-LINKING CONFIRMATION & VERIFICATION DIALOG
    // =========================================================================
    if (state.isConfirmationOpen && state.activeCaptureResult != null) {
        val result = state.activeCaptureResult
        var editableProgress by remember(result) { mutableIntStateOf(result.detectedProgress) }
        var editableDelayHours by remember(result) { mutableIntStateOf(result.delayHours) }
        var editableDelayReason by remember(result) { mutableStateOf(result.delayReason) }
        var editableObservation by remember(result) { mutableStateOf(result.observation) }
        var isActivityDropdownExpanded by remember { mutableStateOf(false) }

        val targetActId = state.manualSelectedActivityId ?: result.matchedActivityId ?: "ACT-024"
        val matchedActivity = state.activities.find { it.activityId == targetActId } ?: state.activities.firstOrNull()

        Dialog(onDismissRequest = { onDismissConfirmation() }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .testTag("verification_dialog"),
                color = CyberViolet900,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonLavender.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Title Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Brush.linearGradient(listOf(NeonViolet, NeonPink))),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                            Column {
                                Text("Review & Link Schedule", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("Human-in-the-Loop Verification", color = TextSecondary, fontSize = 10.sp)
                            }
                        }

                        IconButton(onClick = { onDismissConfirmation() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                        }
                    }

                    // Match Confidence Badge
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = NeonViolet.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonLavender.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("AI Schedule Match Confidence", color = NeonLavender, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("${result.matchConfidence}% Match", color = StatusGreen, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }

                    // Activity Selector Dropdown
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Matched WBS Schedule Activity:", color = TextSecondary, fontSize = 11.sp)
                        Box {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { isActivityDropdownExpanded = true }
                                    .testTag("activity_picker_dropdown"),
                                color = CyberViolet800,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${matchedActivity?.activityId ?: targetActId}: ${matchedActivity?.activityName ?: result.activityName}",
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = NeonLavender)
                                }
                            }

                            DropdownMenu(
                                expanded = isActivityDropdownExpanded,
                                onDismissRequest = { isActivityDropdownExpanded = false },
                                modifier = Modifier.background(CyberViolet900)
                            ) {
                                state.activities.forEach { act ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = "${act.activityId}: ${act.activityName} (${act.packageName})",
                                                color = if (act.activityId == targetActId) NeonLavender else TextPrimary,
                                                fontSize = 12.sp,
                                                fontWeight = if (act.activityId == targetActId) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        onClick = {
                                            onSelectManualActivity(act.activityId)
                                            isActivityDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Progress Comparison
                    val plannedProgress = matchedActivity?.plannedProgress ?: 90
                    val variance = editableProgress - plannedProgress

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = CyberViolet800.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Planned Baseline: $plannedProgress%", color = TextSecondary, fontSize = 11.sp)
                                Text("Detected Actual: $editableProgress%", color = NeonLavender, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("Variance: ${if (variance >= 0) "+$variance" else "$variance"}%", color = if (variance < 0) StatusRed else StatusGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            LinearProgressIndicator(
                                progress = { editableProgress / 100f },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = if (variance < 0) StatusRed else StatusGreen,
                                trackColor = CyberViolet700
                            )
                        }
                    }

                    // Progress & Delay Hours Adjustment
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Actual Progress %", color = TextSecondary, fontSize = 11.sp)
                            OutlinedTextField(
                                value = editableProgress.toString(),
                                onValueChange = { newVal ->
                                    val parsed = newVal.toIntOrNull() ?: 0
                                    editableProgress = parsed.coerceIn(0, 100)
                                    onUpdatePendingResult(
                                        matchedActivity?.activityName ?: result.activityName,
                                        targetActId,
                                        editableProgress,
                                        editableDelayHours,
                                        editableDelayReason,
                                        editableObservation
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonLavender,
                                    unfocusedBorderColor = CyberViolet700,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Delay Hours", color = TextSecondary, fontSize = 11.sp)
                            OutlinedTextField(
                                value = editableDelayHours.toString(),
                                onValueChange = { newVal ->
                                    val parsed = newVal.toIntOrNull() ?: 0
                                    editableDelayHours = parsed.coerceAtLeast(0)
                                    onUpdatePendingResult(
                                        matchedActivity?.activityName ?: result.activityName,
                                        targetActId,
                                        editableProgress,
                                        editableDelayHours,
                                        editableDelayReason,
                                        editableObservation
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = StatusRed,
                                    unfocusedBorderColor = CyberViolet700,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }

                    // Delay Reason Input
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Identified Root Cause / Delay Reason", color = TextSecondary, fontSize = 11.sp)
                        OutlinedTextField(
                            value = editableDelayReason,
                            onValueChange = {
                                editableDelayReason = it
                                onUpdatePendingResult(
                                    matchedActivity?.activityName ?: result.activityName,
                                    targetActId,
                                    editableProgress,
                                    editableDelayHours,
                                    editableDelayReason,
                                    editableObservation
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonLavender,
                                unfocusedBorderColor = CyberViolet700,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            minLines = 1
                        )
                    }

                    // Extracted Features Tags
                    if (result.visibleElements.isNotEmpty() || result.equipment.isNotEmpty()) {
                        Text("Extracted Metadata Tags:", color = TextMuted, fontSize = 10.sp)
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            (result.visibleElements + result.equipment).take(6).forEach { tag ->
                                Surface(
                                    color = CyberViolet800,
                                    shape = RoundedCornerShape(4.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700)
                                ) {
                                    Text(
                                        text = tag,
                                        color = TextSecondary,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Action Buttons: Commit vs Cancel
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onDismissConfirmation() },
                            modifier = Modifier.weight(1f).height(46.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet700)
                        ) {
                            Text("Cancel", color = TextMuted, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { onConfirmSaveReport() },
                            modifier = Modifier
                                .weight(1.6f)
                                .height(46.dp)
                                .testTag("confirm_save_report_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonViolet
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.CloudDone, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Confirm & Commit", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // =========================================================================
    // EVIDENCE DETAIL MODAL
    // =========================================================================
    if (state.isEvidenceDetailModalOpen && state.selectedEvidenceReportForDetail != null) {
        val detail = state.selectedEvidenceReportForDetail
        AlertDialog(
            onDismissRequest = { onDismissEvidenceDetail() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.History, contentDescription = null, tint = NeonLavender)
                    Text("Evidence Report ${detail.id}", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Activity: ${detail.activityId} - ${detail.activityName}", color = NeonLavender, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Captured: ${detail.capturedAt}", color = TextSecondary, fontSize = 11.sp)
                    Text("Progress: ${detail.actualProgress}% (Planned: ${detail.plannedProgress}%, Variance: ${detail.progressVariance}%)", color = TextPrimary, fontSize = 12.sp)
                    if (detail.delayHours > 0) {
                        Text("Delay: ${detail.delayHours} hours (${detail.delayReason})", color = StatusRed, fontSize = 11.sp)
                    }
                    if (detail.observation.isNotBlank()) {
                        Text("Notes: ${detail.observation}", color = TextSecondary, fontSize = 11.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { onDismissEvidenceDetail() }) {
                    Text("Close", color = NeonLavender)
                }
            },
            containerColor = CyberViolet900
        )
    }
}
