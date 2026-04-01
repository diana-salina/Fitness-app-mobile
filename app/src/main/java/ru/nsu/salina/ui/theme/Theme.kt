package ru.nsu.salina.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryPurple,
    surface = SurfaceBlue,
    background = BackgroundWhite,
    onPrimary = BackgroundWhite,
    onSecondary = BackgroundWhite,
    onSurface = TextPrimary,
    onBackground = TextPrimary,
    outline = LightGray,
    error = ErrorRed
)

@Composable
fun HarmonieTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = HarmonieTypography,
        shapes = HarmonieShapes,
        content = content
    )
}
