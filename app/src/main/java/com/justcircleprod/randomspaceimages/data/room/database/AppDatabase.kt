package com.justcircleprod.randomspaceimages.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.justcircleprod.randomspaceimages.data.models.APODEntry
import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.data.room.dao.APODFavouritesDao
import com.justcircleprod.randomspaceimages.data.room.dao.NASALibraryFavouritesDao
import com.justcircleprod.randomspaceimages.data.room.migrations.MIGRATION_1_2
import com.justcircleprod.randomspaceimages.data.room.migrations.MIGRATION_2_3

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