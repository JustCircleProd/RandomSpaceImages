package com.justcircleprod.randomnasaimages.ui.bottomNavItem

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.justcircleprod.randomnasaimages.R
import com.justcircleprod.randomnasaimages.ui.screen.Screen

sealed class BottomNavItem(
    val route: String,
    @StringRes val titleResId: Int,
    val icon: ImageVector,
    val selectedContentColor: Color
) {
    class Home(selectedContentColor: Color) :
        BottomNavItem(Screen.Home.route, R.string.home, Icons.Default.Home, selectedContentColor)

    class Favourites(selectedContentColor: Color) :
        BottomNavItem(
            Screen.Favourites.route,
            R.string.favourites,
            Icons.Default.Favorite,
            selectedContentColor
        )
}