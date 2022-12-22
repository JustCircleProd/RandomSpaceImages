package com.justcircleprod.randomspaceimages.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.justcircleprod.randomspaceimages.data.models.APODEntry
import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.data.room.dao.APODFavouritesDao
import com.justcircleprod.randomspaceimages.data.room.dao.NASALibraryFavouritesDao

@Database(
    version = 2,
    entities = [NASALibraryImageEntry::class, APODEntry::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun nasaLibraryFavouritesDao(): NASALibraryFavouritesDao

    abstract fun apodFavouritesDao(): APODFavouritesDao
}