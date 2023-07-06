package com.justcircleprod.randomspaceimages.data.repository

import com.justcircleprod.randomspaceimages.data.local.favourites.database.AppDatabase
import com.justcircleprod.randomspaceimages.domain.model.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.domain.repository.NASALibraryFavouritesRepository
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class NASALibraryFavouritesRepositoryImpl @Inject constructor(private val appDatabase: AppDatabase) :
    NASALibraryFavouritesRepository {
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
}