package com.justcircleprod.randomspaceimages.domain.repository

import androidx.lifecycle.LiveData
import com.justcircleprod.randomspaceimages.domain.model.NASALibraryImageEntry

interface NASALibraryFavouritesRepository {
    suspend fun getAllNASALibraryFavourites(): List<NASALibraryImageEntry>

    fun isAddedToNASALibraryFavouritesLiveData(nasaId: String): LiveData<Boolean>

    suspend fun isAddedToNASALibraryFavourites(nasaId: String): Boolean

    suspend fun addToNASALibraryFavourites(nasaLibraryImageEntry: NASALibraryImageEntry)

    suspend fun removeFromNASALibraryFavourites(nasaLibraryImageEntry: NASALibraryImageEntry)
}