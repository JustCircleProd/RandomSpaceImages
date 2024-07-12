package com.justcircleprod.randomspaceimages.ui.apod.apodTabs

import androidx.annotation.StringRes
import com.justcircleprod.randomspaceimages.R

sealed class APODTabItem(@StringRes val titleResId: Int) {
    data object APOD : APODTabItem(titleResId = R.string.apod)
    data object Favourites : APODTabItem(titleResId = R.string.favourites)

    companion object {
        val items get() = listOf(APOD, Favourites)
    }
}
