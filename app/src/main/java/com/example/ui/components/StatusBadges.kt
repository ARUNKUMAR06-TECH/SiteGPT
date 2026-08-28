package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ConfidenceTier
import com.example.model.RiskTier
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.StatusAmber
import com.example.ui.theme.StatusAmberBg
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusGreenBg
import com.example.ui.theme.StatusRed
import com.example.ui.theme.StatusRedBg

@Composable
fun RiskBadge(
    tier: RiskTier,
    modifier: Modifier = Modifier
) {
    val (color, label) = when (tier) {
        RiskTier.ON_TRACK -> Pair(StatusGreen, "ON TRACK")
        RiskTier.AT_RISK -> Pair(StatusAmber, "AT RISK")
        RiskTier.CRITICAL -> Pair(StatusRed, "CRITICAL")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(100.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(100.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(color, CircleShape)
        )
        Text(
            text = " $label",
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun ConfidenceBadge(
    confidence: Int,
    tier: ConfidenceTier,
    modifier: Modifier = Modifier
) {
    val (color, label) = when (tier) {
        ConfidenceTier.HIGH -> Pair(StatusGreen, "$confidence% HIGH")
        ConfidenceTier.MEDIUM -> Pair(StatusAmber, "$confidence% MEDIUM")
        ConfidenceTier.LOW -> Pair(StatusRed, "$confidence% LOW")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(100.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(100.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = "AI Match: $label",
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun VarianceBadge(
    variance: Int,
    modifier: Modifier = Modifier
) {
    val color = when {
        variance >= 0 -> StatusGreen
        variance >= -10 -> StatusAmber
        else -> StatusRed
    }

    val text = if (variance > 0) "+$variance%" else "$variance%"

    Text(
        text = text,
        color = color,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        modifier = modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
            .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}
