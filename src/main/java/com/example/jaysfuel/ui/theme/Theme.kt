package com.example.jaysfuel.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color.Black,
    background = DarkBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    secondary = DarkAccent,
    onSecondary = DarkOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    background = LightBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    secondary = LightAccent,
    onSecondary = Color.Black
)

/**
 * Global Compose theme used by the whole app.
 */
@Composable
fun JaysFuelTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
