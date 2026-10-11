package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Irispop Dark Backgrounds & Surfaces
val IrisBgDark = Color(0xFF07090E)
val IrisCardBg = Color(0xFF141824)
val IrisSurface = Color(0xFF0F121B)
val IrisSurfaceElevated = Color(0xFF181D2C)
val IrisBorderSubtle = Color(0xFF23293D)

// Rainbow Spectrum Colors (Pastel Holographic)
val RainbowPink = Color(0xFFFFAFBD)
val RainbowPeach = Color(0xFFFFC3A0)
val RainbowYellow = Color(0xFFFFEAA7)
val RainbowMint = Color(0xFFA8FF78)
val RainbowCyan = Color(0xFF78FFD6)
val RainbowBlue = Color(0xFFA1C4FD)
val RainbowPurple = Color(0xFFE0C3FC)
val RainbowRose = Color(0xFFFF9A9E)
val RainbowLavender = Color(0xFFFECFEF)

// UI Accent Colors
val AccentCyan = Color(0xFF38EF7D)
val AccentCyanGlow = Color(0xFF11998E)
val AccentPink = Color(0xFFFF6584)
val AccentRose = Color(0xFFF43F5E)
val AccentAmber = Color(0xFFFBBF24)
val AccentPurple = Color(0xFFA855F7)

// Spotify Brand Colors
val SpotifyGreen = Color(0xFF1DB954)
val SpotifyGreenDark = Color(0xFF169C46)
val SpotifyDark = Color(0xFF121212)
val SpotifyCardBg = Color(0xFF181818)

val TextPrimary = Color(0xFFF3F4F6)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

// Iris Rainbow Brushes
val IrisRainbowBrush = Brush.linearGradient(
    colors = listOf(
        RainbowPink,
        RainbowPeach,
        RainbowYellow,
        RainbowMint,
        RainbowCyan,
        RainbowBlue,
        RainbowPurple
    )
)

val IrisBorderRainbowBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFBC2EB),
        Color(0xFFA6C1EE),
        Color(0xFF84FAB0),
        Color(0xFFFCCB90),
        Color(0xFFD57EEB)
    )
)

val IrisButtonRainbowBrush = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFB199),
        Color(0xFFFF0844),
        Color(0xFFFBC2EB),
        Color(0xFFA6C1EE),
        Color(0xFF84FAB0)
    )
)
