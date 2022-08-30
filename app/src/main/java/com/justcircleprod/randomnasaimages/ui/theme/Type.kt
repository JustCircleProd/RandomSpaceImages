package com.justcircleprod.randomnasaimages.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.justcircleprod.randomnasaimages.R

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = FontFamily(
        Font(R.font.lato_regular),
        Font(R.font.lato_bold, FontWeight.SemiBold),
        Font(R.font.lato_black, FontWeight.Bold)
    )
)