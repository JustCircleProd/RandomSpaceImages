package com.justcircleprod.randomspaceimages.ui.random.detail

import android.content.Context

sealed class DetailFragmentEvent {
    data object OnFavouriteButtonClick : DetailFragmentEvent()
    data class ShareImage(val context: Context) : DetailFragmentEvent()
    data class SaveImage(val context: Context) : DetailFragmentEvent()
    data object Translate : DetailFragmentEvent()
}
