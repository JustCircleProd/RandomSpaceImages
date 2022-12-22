package com.justcircleprod.randomspaceimages.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.justcircleprod.randomspaceimages.data.models.APODEntry

@Dao
interface APODFavouritesDao {
    @Query("SELECT * FROM apod_favourites")
    suspend fun getAll(): List<APODEntry>

    @Query("SELECT EXISTS(SELECT * FROM apod_favourites WHERE date = :date)")
    fun isAdded(date: String): LiveData<Boolean>

    @Insert
    suspend fun add(apodEntry: APODEntry)

    @Delete
    suspend fun remove(apodEntry: APODEntry)
}