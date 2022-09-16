package com.justcircleprod.randomnasaimages.ui.bottomNavItem

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.justcircleprod.randomnasaimages.R
import com.justcircleprod.randomnasaimages.ui.screen.Screen

sealed class BottomNavItem(
    val route: String,
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int,
    val selectedContentColor: Color
) {
    class Home(selectedContentColor: Color) :
        BottomNavItem(Screen.Home.route, R.string.home, R.drawable.icon_home, selectedContentColor)

    class Search(selectedContentColor: Color) :
        BottomNavItem(
            Screen.Search.route,
            R.string.search,
            R.drawable.icon_search,
            selectedContentColor
        )

    class Favourites(selectedContentColor: Color) :
        BottomNavItem(
            Screen.Favourites.route,
            R.string.favourites,
            R.drawable.icon_favorite,
            selectedContentColor
        )
}