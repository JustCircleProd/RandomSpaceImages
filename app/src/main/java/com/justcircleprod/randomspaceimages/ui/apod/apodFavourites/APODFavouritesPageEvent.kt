package com.justcircleprod.randomspaceimages.ui.apod.apodFavourites

import android.content.Context
import com.justcircleprod.randomspaceimages.domain.model.APODEntry


sealed class APODFavouritesPageEvent {
    data class SetFavourites(val refresh: Boolean = false) : APODFavouritesPageEvent()
    data class OnFavouriteButtonClick(val apodEntry: APODEntry) : APODFavouritesPageEvent()
    data class SaveImage(
        val context: Context,
        val title: String,
        val url: String,
        val hdUrl: String?
    ) : APODFavouritesPageEvent()

    data class ShareImage(
        val context: Context,
        val title: String,
        val url: String,
        val hdUrl: String?
    ) : APODFavouritesPageEvent()

    data class ShareVideo(val context: Context, val title: String, val url: String) :
        APODFavouritesPageEvent()

    data class Translate(val apodIndex: Int) : APODFavouritesPageEvent()
}
