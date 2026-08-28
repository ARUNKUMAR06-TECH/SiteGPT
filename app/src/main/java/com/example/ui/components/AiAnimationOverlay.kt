package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BlueElectric
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AiAnimationOverlay(
    currentStep: Int, // 0..5
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        "Receiving Field Data & Telemetry",
        "Extracting Natural Language & Quantities",
        "Identifying Civil Activity Signatures",
        "Searching Chennai Metro Project Schedule",
        "Calculating 6-Factor Weighted Confidence Match",
        "Synthesizing Predictive Action & Recommendations"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_glow")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        com.example.ui.theme.CyberViolet700.copy(alpha = 0.95f),
                        com.example.ui.theme.CyberViolet800.copy(alpha = 0.98f)
                    )
                )
            )
            .border(1.5.dp, com.example.ui.theme.NeonViolet.copy(alpha = pulseGlow), RoundedCornerShape(20.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Memory,
                contentDescription = null,
                tint = com.example.ui.theme.NeonPink,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "ProjectPulse Neural Engine",
                color = com.example.ui.theme.NeonLavender,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                letterSpacing = 0.5.sp
            )
        }

        Text(
            text = "AI schedule-to-field alignment in progress...",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            steps.forEachIndexed { index, stepText ->
                val isDone = index < currentStep
                val isCurrent = index == currentStep
                val isUpcoming = index > currentStep

                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isCurrent) com.example.ui.theme.NeonViolet.copy(alpha = 0.25f) else Color.Transparent,
                                RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when {
                            isDone -> {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Done",
                                    tint = StatusGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            isCurrent -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = com.example.ui.theme.NeonLavender
                                )
                            }
                            else -> {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .background(com.example.ui.theme.CyberViolet600.copy(alpha = 0.6f), CircleShape)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = stepText,
                            fontSize = 13.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else if (isDone) FontWeight.Medium else FontWeight.Normal,
                            color = when {
                                isDone -> TextPrimary
                                isCurrent -> com.example.ui.theme.NeonLavender
                                else -> TextMuted
                            },
                            modifier = Modifier.alpha(if (isUpcoming) 0.5f else 1.0f)
                        )
                    }
                }
            }
        }
    }
}
