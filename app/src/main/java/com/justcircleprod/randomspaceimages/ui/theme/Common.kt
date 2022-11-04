package com.justcircleprod.randomspaceimages.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf


val LocalCustomColors = staticCompositionLocalOf<CustomColors> {
    error("No colors provided")
}

val MaterialTheme.customColors: CustomColors
    @Composable
    get() = LocalCustomColors.current
