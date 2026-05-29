package com.example.myapplication.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    onPrimary = White,
    primaryContainer = GrayBackground,
    onPrimaryContainer = TextPrimary,
    secondary = TextSecondary,
    onSecondary = White,
    secondaryContainer = GrayBackground,
    onSecondaryContainer = TextPrimary,
    background = GrayBackground,
    onBackground = TextPrimary,
    surface = White,
    onSurface = TextPrimary,
    surfaceVariant = GrayBackground,
    onSurfaceVariant = TextSecondary,
    error = Error,
    onError = White,
    outline = DividerColor
)

@Composable
fun ITDictionaryTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = White.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        content = content
    )
}
