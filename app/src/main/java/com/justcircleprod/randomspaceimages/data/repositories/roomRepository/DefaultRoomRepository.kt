package com.justcircleprod.randomspaceimages.data.repositories.roomRepository

import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.data.room.database.AppDatabase
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultRoomRepository @Inject constructor(private val appDatabase: AppDatabase) :
    RoomRepository {
    override suspend fun getAllFavourites(): List<NASALibraryImageEntry> =
        appDatabase.favouritesDao().getAll()

    override fun isAddedToFavourites(nasaId: String) =
        appDatabase.favouritesDao().isAdded(nasaId)

    override suspend fun addToFavourites(nasaLibraryImageEntry: NASALibraryImageEntry) {
        appDatabase.favouritesDao().add(nasaLibraryImageEntry)
    }

    override suspend fun removeFromFavourites(nasaLibraryImageEntry: NASALibraryImageEntry) {
        appDatabase.favouritesDao().remove(nasaLibraryImageEntry)
    }
}