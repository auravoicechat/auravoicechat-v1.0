package com.aura.voicechat.ui.theme

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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Aura Voice Chat Theme
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Theme: Purple (#c9a8f1) → White gradient
 * Dark Canvas: #12141a
 * Accent Magenta: #d958ff, Accent Cyan: #35e8ff
 */

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = DarkCanvas,
    primaryContainer = Purple40,
    onPrimaryContainer = Purple80,
    secondary = AccentMagenta,
    onSecondary = DarkCanvas,
    secondaryContainer = Purple40,
    onSecondaryContainer = AccentMagenta,
    tertiary = AccentCyan,
    onTertiary = DarkCanvas,
    tertiaryContainer = DarkCard,
    onTertiaryContainer = AccentCyan,
    error = ErrorRed,
    errorContainer = ErrorRed.copy(alpha = 0.3f),
    onError = TextPrimary,
    onErrorContainer = ErrorRed,
    background = DarkCanvas,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondary,
    outline = Purple40,
    inverseOnSurface = DarkCanvas,
    inverseSurface = TextPrimary,
    inversePrimary = Purple40
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = LightBackground,
    primaryContainer = Purple80,
    onPrimaryContainer = Purple20,
    secondary = AccentMagenta,
    onSecondary = LightBackground,
    secondaryContainer = AccentMagenta.copy(alpha = 0.2f),
    onSecondaryContainer = Purple40,
    tertiary = AccentCyan,
    onTertiary = DarkCanvas,
    tertiaryContainer = AccentCyan.copy(alpha = 0.2f),
    onTertiaryContainer = DarkCanvas,
    error = ErrorRed,
    errorContainer = ErrorRed.copy(alpha = 0.1f),
    onError = LightBackground,
    onErrorContainer = ErrorRed,
    background = LightBackground,
    onBackground = TextOnLight,
    surface = LightSurface,
    onSurface = TextOnLight,
    surfaceVariant = Purple80.copy(alpha = 0.3f),
    onSurfaceVariant = TextOnLight,
    outline = Purple40,
    inverseOnSurface = LightBackground,
    inverseSurface = TextOnLight,
    inversePrimary = Purple80
)

@Composable
fun AuraVoiceChatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
