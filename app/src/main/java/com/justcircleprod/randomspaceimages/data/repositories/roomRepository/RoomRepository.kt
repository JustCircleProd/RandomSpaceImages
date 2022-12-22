package com.justcircleprod.randomspaceimages.data.repositories.roomRepository

import androidx.lifecycle.LiveData
import com.justcircleprod.randomspaceimages.data.models.APODEntry
import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry

interface RoomRepository {
    suspend fun getAllNASALibraryFavourites(): List<NASALibraryImageEntry>

    fun isAddedToNASALibraryFavourites(nasaId: String): LiveData<Boolean>

    suspend fun addToNASALibraryFavourites(nasaLibraryImageEntry: NASALibraryImageEntry)

    suspend fun removeFromNASALibraryFavourites(nasaLibraryImageEntry: NASALibraryImageEntry)

    suspend fun getAllAPODFavourites(): List<APODEntry>

    fun isAddedToAPODFavourites(date: String): LiveData<Boolean>

    suspend fun addToAPODFavourites(apodEntry: APODEntry)

    suspend fun removeFromAPODFavourites(apodEntry: APODEntry)
}