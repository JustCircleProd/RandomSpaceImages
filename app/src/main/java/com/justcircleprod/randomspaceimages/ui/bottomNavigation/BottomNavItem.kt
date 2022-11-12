package com.justcircleprod.randomspaceimages.ui.bottomNavigation

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.justcircleprod.randomspaceimages.R

sealed class BottomNavItem(
    @IdRes val navigationId: Int,
    @IdRes val actionId: Int,
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int
) {
    object Home :
        BottomNavItem(R.id.navigation_home, R.id.to_home, R.string.home, R.drawable.icon_home)

    object Search : BottomNavItem(
        R.id.navigation_search,
        R.id.to_search,
        R.string.search,
        R.drawable.icon_search
    )

    object More :
        BottomNavItem(R.id.navigation_more, R.id.to_more, R.string.more, R.drawable.icon_more)

    companion object {
        val items get() = listOf(Home, Search, More)
    }
}