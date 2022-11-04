package com.justcircleprod.randomspaceimages.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = BlueLight,
    primaryVariant = BlueDark,
    secondary = Red,
    surface = DarkBackground,
    background = DarkBackground,
    onPrimary = Color.Black,
    onSecondary = Color.Black
)

private val DarkCustomColors = CustomColors(
    text = DarkText,
    errorCardBackground = ErrorCardBackground,
    iconButtonShadow = IconButtonShadow,
    cardBackground = DarkCardBackground,
    bottomNavigationBackground = DarkBottomNavigationBackground,
    bottomNavigationUnselected = DarkBottomNavigationUnselected,
    favouriteListButtonNeutral = DarkFavouriteListButtonNeutral,
    shimmerHighlight = DarkShimmerHighlight,
    searchBarBackground = DarkSearchBarBackground,
    searchBarPlaceholder = DarkSearchBarPlaceholder,
    searchBarIcon = DarkSearchBarIcon,
    searchBarText = DarkSearchBarText
)

private val LightColorPalette = lightColors(
    primary = BlueDark,
    primaryVariant = BlueLight,
    secondary = Red,
    background = LightBackground,
    onPrimary = Color.White,
    onSecondary = Color.Black,
)

private val LightCustomColors = CustomColors(
    text = LightText,
    errorCardBackground = ErrorCardBackground,
    iconButtonShadow = IconButtonShadow,
    cardBackground = LightCardBackground,
    bottomNavigationBackground = LightBottomNavigationBackground,
    bottomNavigationUnselected = LightBottomNavigationUnselected,
    favouriteListButtonNeutral = LightFavouriteListButtonNeutral,
    shimmerHighlight = LightShimmerHighlight,
    searchBarBackground = LightSearchBarBackground,
    searchBarPlaceholder = LightSearchBarPlaceholder,
    searchBarIcon = LightSearchBarIcon,
    searchBarText = LightSearchBarText
)

@Composable
fun RandomSpaceImagesTheme(
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

@Composable
fun CustomColorsProvider(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val customColors = if (darkTheme) {
        DarkCustomColors
    } else {
        LightCustomColors
    }

    CompositionLocalProvider(
        LocalCustomColors provides customColors,
        content = content
    )
}