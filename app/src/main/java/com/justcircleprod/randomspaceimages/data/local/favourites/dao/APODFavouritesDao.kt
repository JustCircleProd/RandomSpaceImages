package com.justcircleprod.randomspaceimages.data.local.favourites.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.justcircleprod.randomspaceimages.domain.model.APODEntry

@Dao
interface APODFavouritesDao {
    @Query("SELECT * FROM apod_favourites")
    suspend fun getAll(): List<APODEntry>

    @Query("SELECT EXISTS(SELECT * FROM apod_favourites WHERE date = :date)")
    fun isAddedLiveData(date: String): LiveData<Boolean>

    @Query("SELECT EXISTS(SELECT * FROM apod_favourites WHERE date = :date)")
    suspend fun isAdded(date: String): Boolean

    @Insert
    suspend fun add(apodEntry: APODEntry)

    @Delete
    suspend fun remove(apodEntry: APODEntry)
}