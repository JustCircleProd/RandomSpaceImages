package com.justcircleprod.randomspaceimages.ui.random.randomTabs

import androidx.annotation.StringRes
import com.justcircleprod.randomspaceimages.R

sealed class RandomTabItem(@StringRes val titleResId: Int) {
    data object Random : RandomTabItem(titleResId = R.string.random)
    data object Favourites : RandomTabItem(titleResId = R.string.favourites)

    companion object {
        val items get() = listOf(Random, Favourites)
    }
}
