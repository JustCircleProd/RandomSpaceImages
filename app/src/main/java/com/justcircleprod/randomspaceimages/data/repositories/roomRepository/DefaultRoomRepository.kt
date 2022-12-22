package com.justcircleprod.randomspaceimages.data.repositories.roomRepository

import androidx.lifecycle.LiveData
import com.justcircleprod.randomspaceimages.data.models.APODEntry
import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.data.room.database.AppDatabase
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultRoomRepository @Inject constructor(private val appDatabase: AppDatabase) :
    RoomRepository {
    override suspend fun getAllNASALibraryFavourites(): List<NASALibraryImageEntry> =
        appDatabase.nasaLibraryFavouritesDao().getAll()

    override fun isAddedToNASALibraryFavourites(nasaId: String) =
        appDatabase.nasaLibraryFavouritesDao().isAdded(nasaId)

    override suspend fun addToNASALibraryFavourites(nasaLibraryImageEntry: NASALibraryImageEntry) {
        appDatabase.nasaLibraryFavouritesDao().add(nasaLibraryImageEntry)
    }

    override suspend fun removeFromNASALibraryFavourites(nasaLibraryImageEntry: NASALibraryImageEntry) {
        appDatabase.nasaLibraryFavouritesDao().remove(nasaLibraryImageEntry)
    }

    override suspend fun getAllAPODFavourites(): List<APODEntry> =
        appDatabase.apodFavouritesDao().getAll()

    override fun isAddedToAPODFavourites(date: String): LiveData<Boolean> =
        appDatabase.apodFavouritesDao().isAdded(date)

    override suspend fun addToAPODFavourites(apodEntry: APODEntry) {
        appDatabase.apodFavouritesDao().add(apodEntry)
    }

    override suspend fun removeFromAPODFavourites(apodEntry: APODEntry) {
        appDatabase.apodFavouritesDao().remove(apodEntry)
    }
}