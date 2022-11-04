package com.justcircleprod.randomspaceimages.ui.bottomNavItem

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.navigation.Screen

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

    class More(selectedContentColor: Color) :
        BottomNavItem(Screen.More.route, R.string.more, R.drawable.icon_more, selectedContentColor)
}