package com.justcircleprod.randomspaceimages.data.repositories.roomRepository

import com.justcircleprod.randomspaceimages.data.models.ImageEntry
import com.justcircleprod.randomspaceimages.data.room.database.AppDatabase
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultRoomRepository @Inject constructor(private val appDatabase: AppDatabase) :
    RoomRepository {
    override suspend fun getAllFavourites(): List<ImageEntry> =
        appDatabase.favouritesDao().getAll()

    override fun isAddedToFavourites(nasaId: String) =
        appDatabase.favouritesDao().isAdded(nasaId)

    override suspend fun addToFavourites(imageEntry: ImageEntry) {
        appDatabase.favouritesDao().add(imageEntry)
    }

    override suspend fun removeFromFavourites(imageEntry: ImageEntry) {
        appDatabase.favouritesDao().remove(imageEntry)
    }
}