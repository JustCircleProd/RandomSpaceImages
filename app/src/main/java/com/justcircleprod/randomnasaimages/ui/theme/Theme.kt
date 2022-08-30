package com.justcircleprod.randomnasaimages.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = BlueLight,
    primaryVariant = BlueDark,
    secondary = Red,
    background = DarkBackground,
    surface = DarkBackground
)

private val LightColorPalette = lightColors(
    primary = BlueDark,
    primaryVariant = BlueLight,
    secondary = Red,
    background = LightBackground,
    surface = LightBackground
)

@Composable
fun RandomNASAImagesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}