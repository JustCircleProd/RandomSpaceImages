package com.justcircleprod.randomspaceimages.ui.bottomNavigation

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.dataStore.DataStoreConstants

sealed class BottomNavItem(
    @IdRes val navigationId: Int,
    @IdRes val actionId: Int,
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int
) {
    object Random :
        BottomNavItem(
            R.id.navigation_random,
            R.id.to_random,
            R.string.random,
            R.drawable.icon_random
        )

    object APOD : BottomNavItem(
        R.id.navigation_apod,
        R.id.to_apod,
        R.string.apod,
        R.drawable.icon_apod
    )

    object More :
        BottomNavItem(R.id.navigation_more, R.id.to_more, R.string.more, R.drawable.icon_more)

    companion object {
        fun getItems(startScreen: String?) =
            if (startScreen == DataStoreConstants.RANDOM_SCREEN) {
                listOf(Random, APOD, More)
            } else {
                listOf(APOD, Random, More)
            }
    }
}