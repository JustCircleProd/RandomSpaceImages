package com.justcircleprod.randomspaceimages.data.repositories.roomRepository

import androidx.lifecycle.LiveData
import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry

interface RoomRepository {
    suspend fun getAllFavourites(): List<NASALibraryImageEntry>

    fun isAddedToFavourites(nasaId: String): LiveData<Boolean>

    suspend fun addToFavourites(nasaLibraryImageEntry: NASALibraryImageEntry)

    suspend fun removeFromFavourites(nasaLibraryImageEntry: NASALibraryImageEntry)
}