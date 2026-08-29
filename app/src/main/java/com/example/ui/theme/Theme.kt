package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VortexCyberColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF0284C7),
    onPrimaryContainer = Color.White,
    secondary = NeonMagenta,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF9F1239),
    onSecondaryContainer = Color.White,
    tertiary = NeonEmerald,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF047857),
    onTertiaryContainer = Color.White,
    background = DarkVoidBg,
    onBackground = TextPrimaryDark,
    surface = DarkSurfaceGlass,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceVariantGlass,
    onSurfaceVariant = TextSecondaryDark,
    outline = GlassBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = VortexCyberColorScheme,
        typography = Typography,
        content = content
    )
}
