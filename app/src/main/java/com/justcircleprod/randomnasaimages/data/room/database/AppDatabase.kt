package com.justcircleprod.randomnasaimages.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.justcircleprod.randomnasaimages.data.models.ImageEntry
import com.justcircleprod.randomnasaimages.data.room.dao.FavouritesDao

@Database(
    version = 1,
    entities = [ImageEntry::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouritesDao(): FavouritesDao
}