package com.justcircleprod.randomspaceimages.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.justcircleprod.randomspaceimages.R

val DefaultFontFamily = FontFamily(
    Font(R.font.lato_regular),
    Font(R.font.lato_bold, FontWeight.SemiBold),
    Font(R.font.lato_black, FontWeight.Bold)
)

val Typography = Typography(
    defaultFontFamily = DefaultFontFamily
)