package com.justcircleprod.randomspaceimages.ui.apod.apodPage

import android.content.Context
import com.justcircleprod.randomspaceimages.domain.model.APODEntry


sealed class APODPageEvent {
    data class LoadTodayAPOD(val refresh: Boolean) : APODPageEvent()
    data object LoadMoreAPODs : APODPageEvent()
    data class OnDatePicked(val dateInMills: Long) : APODPageEvent()
    data object OnCancelDateButtonClick : APODPageEvent()
    data class OnFavouriteButtonClick(val apodEntry: APODEntry) : APODPageEvent()
    data class SaveImage(
        val context: Context,
        val title: String,
        val url: String,
        val hdUrl: String?
    ) : APODPageEvent()

    data class ShareImage(
        val context: Context,
        val title: String,
        val url: String,
        val hdUrl: String?
    ) : APODPageEvent()

    data class ShareVideo(val context: Context, val title: String, val url: String) :
        APODPageEvent()

    data class Translate(val apodIndex: Int) : APODPageEvent()
}
