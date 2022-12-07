package com.justcircleprod.randomspaceimages.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.data.room.dao.NASALibraryFavouritesDao

@Database(
    version = 1,
    entities = [NASALibraryImageEntry::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouritesDao(): NASALibraryFavouritesDao
}