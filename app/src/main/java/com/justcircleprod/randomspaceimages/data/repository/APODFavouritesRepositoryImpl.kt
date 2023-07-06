package com.justcircleprod.randomspaceimages.data.repository

import androidx.lifecycle.LiveData
import com.justcircleprod.randomspaceimages.data.local.favourites.database.AppDatabase
import com.justcircleprod.randomspaceimages.domain.model.APODEntry
import com.justcircleprod.randomspaceimages.domain.repository.APODFavouritesRepository
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class APODFavouritesRepositoryImpl @Inject constructor(private val appDatabase: AppDatabase) :
    APODFavouritesRepository {
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