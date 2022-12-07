package com.justcircleprod.randomspaceimages.ui.random.tabs

import androidx.annotation.StringRes
import com.justcircleprod.randomspaceimages.R

sealed class TabItem(@StringRes val titleResId: Int) {
    object Random : TabItem(titleResId = R.string.random)
    object Favourites : TabItem(titleResId = R.string.favourites)

    companion object {
        val items get() = listOf(Random, Favourites)
    }
}
