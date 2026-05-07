package com.hermeticvm.linkahest.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(30.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD8B8FF),
    onPrimary = Color(0xFF3E006C),
    primaryContainer = Color(0xFF5A168B),
    onPrimaryContainer = Color(0xFFF1DBFF),
    secondary = Color(0xFF7DDA92),
    onSecondary = Color(0xFF003915),
    secondaryContainer = Color(0xFF145127),
    onSecondaryContainer = Color(0xFF98F7AC),
    tertiary = Color(0xFF79D7D0),
    onTertiary = Color(0xFF003735),
    tertiaryContainer = Color(0xFF00504D),
    onTertiaryContainer = Color(0xFF9AF3EB),
    surface = Color(0xFF17121B),
    surfaceVariant = Color(0xFF4B4451),
    background = Color(0xFF120D16),
    outline = Color(0xFF998DA0)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6F22A3),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF1DBFF),
    onPrimaryContainer = Color(0xFF28004A),
    secondary = Color(0xFF316B3E),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB4F1BE),
    onSecondaryContainer = Color(0xFF002109),
    tertiary = Color(0xFF006A66),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF9AF3EB),
    onTertiaryContainer = Color(0xFF00201F),
    surface = Color(0xFFFFF7FF),
    surfaceVariant = Color(0xFFECE0EE),
    background = Color(0xFFFFF7FF),
    outline = Color(0xFF7C707F)
)

@Composable
fun LinkahestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> dynamicColorScheme(darkTheme)
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

@Composable
private fun dynamicColorScheme(darkTheme: Boolean): ColorScheme {
    val context = LocalContext.current
    return if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
}
