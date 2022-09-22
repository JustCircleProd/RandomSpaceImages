package com.justcircleprod.randomspaceimages.data.repositories.roomRepository

import androidx.lifecycle.LiveData
import com.justcircleprod.randomspaceimages.data.models.ImageEntry

interface RoomRepository {
    suspend fun getAllFavourites(): List<ImageEntry>

    fun isAddedToFavourites(nasaId: String): LiveData<Boolean>

    suspend fun addToFavourites(imageEntry: ImageEntry)

    suspend fun removeFromFavourites(imageEntry: ImageEntry)
}