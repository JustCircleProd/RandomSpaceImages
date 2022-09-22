package com.justcircleprod.randomspaceimages.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.justcircleprod.randomspaceimages.data.models.ImageEntry

@Dao
interface FavouritesDao {
    @Query("SELECT * FROM favourites")
    suspend fun getAll(): List<ImageEntry>

    @Query("SELECT EXISTS(SELECT * FROM favourites WHERE nasaId = :nasaId)")
    fun isAdded(nasaId: String): LiveData<Boolean>

    @Insert
    suspend fun add(imageEntry: ImageEntry)

    @Delete
    suspend fun remove(imageEntry: ImageEntry)
}