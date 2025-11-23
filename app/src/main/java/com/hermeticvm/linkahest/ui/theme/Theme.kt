package com.hermeticvm.linkahest.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

private val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF8A2BE2),
    secondary = androidx.compose.ui.graphics.Color(0xFF4CAF50),
    tertiary = androidx.compose.ui.graphics.Color(0xFF03DAC5)
)

private val LightColorScheme = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF6A1B9A),
    secondary = androidx.compose.ui.graphics.Color(0xFF388E3C),
    tertiary = androidx.compose.ui.graphics.Color(0xFF00ACC1)
)

@Composable
fun LinkahestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = AppShapes,
        typography = Typography,
        content = content
    )
}
