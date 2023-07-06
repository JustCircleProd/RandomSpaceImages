package com.justcircleprod.randomspaceimages.domain.repository

import androidx.lifecycle.LiveData
import com.justcircleprod.randomspaceimages.domain.model.APODEntry

interface APODFavouritesRepository {
    suspend fun getAllAPODFavourites(): List<APODEntry>

    fun isAddedToAPODFavourites(date: String): LiveData<Boolean>

    suspend fun addToAPODFavourites(apodEntry: APODEntry)

    suspend fun removeFromAPODFavourites(apodEntry: APODEntry)
}