package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SafetyOrange,
    onPrimary = CharcoalDark,
    primaryContainer = Charcoal700,
    onPrimaryContainer = SafetyOrangeBright,
    secondary = SafetyOrangeBright,
    onSecondary = CharcoalDark,
    secondaryContainer = Charcoal800,
    onSecondaryContainer = OffWhiteSecondary,
    tertiary = SafetyOrangeGlow,
    onTertiary = CharcoalDark,
    background = CharcoalDark,
    onBackground = OffWhitePrimary,
    surface = CharcoalDark,
    onSurface = OffWhitePrimary,
    surfaceVariant = Charcoal800,
    onSurfaceVariant = OffWhiteSecondary,
    outline = SurfaceBorder,
    error = StatusRed,
    onError = Color.White,
    errorContainer = StatusRedBg,
    onErrorContainer = Color(0xFFFECACA)
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
