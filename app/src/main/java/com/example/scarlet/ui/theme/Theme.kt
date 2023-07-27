package com.example.scarlet.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Primary40,
    onPrimary = Color.White,

    secondary = Secondary50,
    onSecondary = Color.White,

    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,

    background = Color.White,
    onBackground = Color.Black,

    surface = Secondary5,
    onSurface = Color.Black,
    surfaceVariant = Secondary10,
    onSurfaceVariant = Color.Black,

    outline = Primary40,
    outlineVariant = Secondary90
)

private val DarkColorScheme = darkColorScheme(
    primary = Primary50,
    onPrimary = Color.White,

    secondary = Secondary80,
    onSecondary = Color.White,

    error = Red40,
    onError = Color.White,
    errorContainer = Red10,
    onErrorContainer = Red90,

    background = Secondary90,
    onBackground = Color.White,

    surface = Secondary85,
    onSurface = Color.White,
    surfaceVariant = Secondary80,
    onSurfaceVariant = Color.White,

    outline = Primary50,
    outlineVariant = Color.White
)

@Composable
fun ScarletTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}