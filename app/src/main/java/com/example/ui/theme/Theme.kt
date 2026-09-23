package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = UniIndigoLight,
    onPrimary = Color(0xFF0F172A),
    primaryContainer = UniIndigoDark,
    onPrimaryContainer = UniIndigoContainer,
    secondary = UniTealLight,
    onSecondary = Color(0xFF0C4A6E),
    secondaryContainer = UniTealDark,
    onSecondaryContainer = UniTealContainer,
    tertiary = UniAmberTertiary,
    onTertiary = Color.Black,
    background = UniBgDark,
    onBackground = UniTextPrimaryDark,
    surface = UniSurfaceDark,
    onSurface = UniTextPrimaryDark,
    surfaceVariant = UniSurfaceVariantDark,
    onSurfaceVariant = UniTextSecondaryDark,
    outline = UniBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = UniIndigoPrimary,
    onPrimary = Color.White,
    primaryContainer = UniIndigoContainer,
    onPrimaryContainer = UniIndigoOnContainer,
    secondary = UniTealSecondary,
    onSecondary = Color.White,
    secondaryContainer = UniTealContainer,
    onSecondaryContainer = Color(0xFF075985),
    tertiary = UniAmberTertiary,
    onTertiary = Color.White,
    background = UniBgLight,
    onBackground = UniTextPrimaryLight,
    surface = UniSurfaceLight,
    onSurface = UniTextPrimaryLight,
    surfaceVariant = UniSurfaceVariantLight,
    onSurfaceVariant = UniTextSecondaryLight,
    outline = UniBorderLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our brand colors for cohesive UniLoop identity
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
