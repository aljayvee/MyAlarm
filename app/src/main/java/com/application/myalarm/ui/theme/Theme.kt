package com.application.myalarm.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SnoozeOffColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = TextOnOrange,
    primaryContainer = OrangeLight,
    onPrimaryContainer = OrangeDark,
    secondary = OrangeAccent,
    onSecondary = TextOnOrange,
    secondaryContainer = OrangeLight,
    onSecondaryContainer = OrangeDark,
    tertiary = GreenSuccess,
    onTertiary = TextOnOrange,
    tertiaryContainer = GreenLight,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = CardBackground,
    onSurfaceVariant = TextSecondary,
    outline = DividerColor,
    outlineVariant = DividerColor,
    error = RedError,
    onError = TextOnOrange
)

@Composable
fun MyAlarmTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SnoozeOffColorScheme,
        typography = Typography,
        content = content
    )
}