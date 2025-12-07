package com.example.jaysfuel.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    secondary = YellowSecondary,
    onSecondary = Color.Black,
    background = BackgroundLight,
    onBackground = Color(0xFF121212),
    surface = SurfaceLight,
    onSurface = Color(0xFF121212)
)

private val DarkColorScheme = darkColorScheme(
    primary = YellowSecondary,
    onPrimary = Color.Black,
    secondary = BluePrimary,
    onSecondary = Color.White,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White
)

/**
 * App theme wrapper for Jay's Fuel.
 * Uses a darker primary blue on a light grey background in light mode
 * to improve contrast as suggested in feedback.
 */
@Composable
fun JaysFuelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Typography comes from your Type.kt
        // We do not pass a custom Shapes object here, default Material3 shapes are used.
        content = content
    )
}
