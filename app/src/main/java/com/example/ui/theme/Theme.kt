package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val IrisDarkColorScheme = darkColorScheme(
    primary = RainbowPink,
    onPrimary = Color(0xFF0F121B),
    secondary = RainbowBlue,
    onSecondary = Color(0xFF0F121B),
    tertiary = RainbowMint,
    onTertiary = Color(0xFF0F121B),
    background = IrisBgDark,
    onBackground = TextPrimary,
    surface = IrisSurface,
    onSurface = TextPrimary,
    surfaceVariant = IrisCardBg,
    onSurfaceVariant = TextSecondary,
    outline = IrisBorderSubtle,
    outlineVariant = Color(0xFF1E2435)
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = IrisDarkColorScheme,
        typography = IrispopTypography,
        content = content
    )
}
