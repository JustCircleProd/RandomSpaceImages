package com.justcircleprod.randomspaceimages.data.local.favourites.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.justcircleprod.randomspaceimages.domain.model.NASALibraryImageEntry

@Dao
interface NASALibraryFavouritesDao {
    @Query("SELECT * FROM nasa_library_favourites")
    suspend fun getAll(): List<NASALibraryImageEntry>

    @Query("SELECT EXISTS(SELECT * FROM nasa_library_favourites WHERE nasaId = :nasaId)")
    fun isAddedLiveData(nasaId: String): LiveData<Boolean>

    @Query("SELECT EXISTS(SELECT * FROM nasa_library_favourites WHERE nasaId = :nasaId)")
    suspend fun isAdded(nasaId: String): Boolean

    @Insert
    suspend fun add(nasaLibraryImageEntry: NASALibraryImageEntry)

    @Delete
    suspend fun remove(nasaLibraryImageEntry: NASALibraryImageEntry)
}