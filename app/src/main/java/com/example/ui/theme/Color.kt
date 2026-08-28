package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// =========================================================================
// CHARCOAL + SAFETY ORANGE + OFF WHITE INDUSTRIAL PALETTE
// Rugged, industrial, trustworthy engineering aesthetic
// =========================================================================

// Charcoal & Gunmetal Industrial System
val CharcoalDark = Color(0xFF121417)          // Base deep charcoal canvas
val Charcoal900 = Color(0xFF16191E)           // Deep charcoal background
val Charcoal800 = Color(0xFF1E2228)           // Primary card container
val Charcoal700 = Color(0xFF262C34)           // Elevated container / surface
val Charcoal600 = Color(0xFF323A45)           // Headers & chip containers
val Charcoal500 = Color(0xFF424B58)           // Subtle borders & dividers
val Charcoal400 = Color(0xFF5A6575)           // Slate gray secondary borders
val Charcoal300 = Color(0xFF7A8699)           // Industrial steel

// Safety Orange Industrial Accent System (High-Vis Engineering)
val SafetyOrange = Color(0xFFFF5722)          // Iconic Safety Orange / Blaze
val SafetyOrangeBright = Color(0xFFFF6D00)    // High-visibility vibrant orange
val SafetyOrangeDeep = Color(0xFFE64A19)      // Deep industrial burnt orange
val SafetyOrangeGlow = Color(0xFFFF8A50)      // Warm orange highlight
val SafetyOrangeLight = Color(0xFFFFCCBC)     // Soft orange container tint
val SafetyOrangeBg = Color(0xFF2E150B)        // Subtle dark orange surface tint
val SafetyOrangeMuted = Color(0xFFBF360C)     // Rugged dark safety orange

// Off White & Slate High-Contrast Typography
val OffWhitePrimary = Color(0xFFF8FAFC)       // Crisp clean off-white
val OffWhiteSecondary = Color(0xFFE2E8F0)     // Soft off-white secondary
val OffWhiteMuted = Color(0xFF94A3B8)         // Industrial cool slate muted text
val OffWhiteCard = Color(0xFFF1F5F9)          // Off-white card surface

// Legacy / System Aliases (Seamlessly remapped to Charcoal + Safety Orange + Off White)
val CyberVioletDark = CharcoalDark
val CyberViolet900 = Charcoal900
val CyberViolet800 = Charcoal800
val CyberViolet700 = Charcoal700
val CyberViolet600 = Charcoal600
val CyberViolet500 = Charcoal500
val CyberViolet400 = Charcoal400
val CyberViolet300 = Charcoal300

val Navy900 = Charcoal900
val Navy800 = Charcoal800
val Navy700 = Charcoal700
val Navy600 = Charcoal600
val Navy500 = Charcoal500
val Navy400 = Charcoal400

// Glowing Accents mapped to Safety Orange & Off White
val NeonViolet = SafetyOrange
val NeonPurple = SafetyOrangeDeep
val NeonPink = SafetyOrangeBright
val NeonLavender = OffWhiteSecondary
val NeonCyan = SafetyOrangeGlow
val CyanAccent = SafetyOrange
val CyanGlow = SafetyOrangeGlow
val BlueElectric = SafetyOrange
val IndigoAccent = SafetyOrangeBright

// Engineering Status Colors with High Contrast
val StatusGreen = Color(0xFF10B981)           // Emerald (on-track / verified)
val StatusGreenBg = Color(0xFF063327)
val StatusAmber = Color(0xFFF59E0B)           // Amber (caution / at-risk)
val StatusAmberBg = Color(0xFF381E04)
val StatusRed = Color(0xFFEF4444)             // Red (critical delay)
val StatusRedBg = Color(0xFF3B0D0D)

// Surfaces & Industrial Borders
val SurfaceDark = CharcoalDark
val SurfaceCard = Charcoal800
val SurfaceCardElevated = Charcoal700
val SurfaceCardGloss = Charcoal600
val SurfaceBorder = Charcoal600
val SurfaceBorderHighlight = SafetyOrange

// Typography Colors
val TextPrimary = OffWhitePrimary
val TextSecondary = OffWhiteSecondary
val TextMuted = OffWhiteMuted

// Gradient Brushes
val CyberBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF1E2228),
        Color(0xFF16191E),
        Color(0xFF121417)
    )
)

val CardGlossHeaderGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2C333D),
        Color(0xFF20252C)
    )
)

val NeonButtonGradient = Brush.horizontalGradient(
    colors = listOf(
        SafetyOrangeDeep,
        SafetyOrange,
        SafetyOrangeBright
    )
)

val NeonPillGradient = Brush.horizontalGradient(
    colors = listOf(
        SafetyOrangeDeep,
        SafetyOrange
    )
)


