package com.justcircleprod.randomspaceimages.ui.random.randomtTabs

import androidx.annotation.StringRes
import com.justcircleprod.randomspaceimages.R

sealed class RandomTabItem(@StringRes val titleResId: Int) {
    object Random : RandomTabItem(titleResId = R.string.random)
    object Favourites : RandomTabItem(titleResId = R.string.favourites)

    companion object {
        val items get() = listOf(Random, Favourites)
    }
}
