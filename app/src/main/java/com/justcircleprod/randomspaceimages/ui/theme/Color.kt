package com.justcircleprod.randomspaceimages.ui.theme

import androidx.compose.ui.graphics.Color

val BlueLight = Color(0xFF60CBFF)
val BlueDark = Color(0xFF3664F4)

val Red = Color(0xFFE85353)

val ErrorCardBackground = Color(0xFFEFCED5)
val IconButtonShadow = Color(0xFF2E2E2E)

val LightNavigationBar = Color(0xFFDCE1E4)
val LightBackground = Color(0xFFEBEFF2)

val LightText = Color(0xFF313131)

val LightCardBackground = Color(0xFFFFFFFF)

val LightBottomNavigationBackground = Color(0xFFDDDDDD)
val LightBottomNavigationUnselected = Color(0xFF585858)

val LightFavouriteListButtonNeutral = Color.DarkGray

val LightShimmerHighlight = Color.LightGray

val LightSearchBarBackground = Color(0xFFDDDDDD)
val LightSearchBarPlaceholder = Color(0xFF585858)
val LightSearchBarIcon = Color(0xFF656565)
val LightSearchBarText = Color(0xFF1D1D1D)


val DarkNavigationBar = Color(0xFF1F1F1F)
val DarkBackground = Color(0xFF252525)

val DarkText = Color(0xFFE5E5E5)

val DarkCardBackground = Color(0xFF373737)

val DarkBottomNavigationBackground = Color(0xFF4A4A4A)
val DarkBottomNavigationUnselected = Color(0xFFBDBFBE)

val DarkFavouriteListButtonNeutral = Color.LightGray

val DarkShimmerHighlight = Color.DarkGray

val DarkSearchBarBackground = Color(0xFF4A4A4A)
val DarkSearchBarPlaceholder = Color(0xFFB7B7B7)
val DarkSearchBarIcon = Color(0xFFACACAC)
val DarkSearchBarText = Color(0xFFE8E8E8)


data class CustomColors(
    val text: Color,
    val errorCardBackground: Color,
    val iconButtonShadow: Color,
    val cardBackground: Color,
    val bottomNavigationBackground: Color,
    val bottomNavigationUnselected: Color,
    val favouriteListButtonNeutral: Color,
    val shimmerHighlight: Color,
    val searchBarBackground: Color,
    val searchBarPlaceholder: Color,
    val searchBarIcon: Color,
    val searchBarText: Color,
)