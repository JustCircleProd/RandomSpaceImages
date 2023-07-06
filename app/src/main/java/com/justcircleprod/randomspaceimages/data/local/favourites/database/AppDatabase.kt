package com.justcircleprod.randomspaceimages.data.local.favourites.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.justcircleprod.randomspaceimages.data.local.favourites.dao.APODFavouritesDao
import com.justcircleprod.randomspaceimages.data.local.favourites.dao.NASALibraryFavouritesDao
import com.justcircleprod.randomspaceimages.data.local.favourites.migrations.MIGRATION_1_2
import com.justcircleprod.randomspaceimages.data.local.favourites.migrations.MIGRATION_2_3
import com.justcircleprod.randomspaceimages.domain.model.APODEntry
import com.justcircleprod.randomspaceimages.domain.model.NASALibraryImageEntry

@Database(
    version = 3,
    entities = [NASALibraryImageEntry::class, APODEntry::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun nasaLibraryFavouritesDao(): NASALibraryFavouritesDao

    abstract fun apodFavouritesDao(): APODFavouritesDao

    companion object {
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "RandomNASAImagesDatabase.db")
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
    }
}