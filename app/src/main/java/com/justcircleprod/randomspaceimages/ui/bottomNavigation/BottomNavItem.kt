package com.justcircleprod.randomspaceimages.ui.bottomNavigation

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.local.settings.DataStoreConstants

sealed class BottomNavItem(
    @IdRes val navigationId: Int,
    @IdRes val actionId: Int,
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int
) {
    data object Random :
        BottomNavItem(
            R.id.navigation_random,
            R.id.to_random,
            R.string.random,
            R.drawable.icon_random
        )

    data object APOD : BottomNavItem(
        R.id.navigation_apod,
        R.id.to_apod,
        R.string.apod,
        R.drawable.icon_apod
    )

    data object More :
        BottomNavItem(R.id.navigation_more, R.id.to_more, R.string.more, R.drawable.icon_more)

    companion object {
        fun getItems(startScreen: String?) =
            when (startScreen) {
                DataStoreConstants.APOD_SCREEN, null -> {
                    listOf(APOD, Random, More)
                }
                else -> {
                    listOf(Random, APOD, More)
                }
            }
    }
}