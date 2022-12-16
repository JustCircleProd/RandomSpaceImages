package com.justcircleprod.randomspaceimages.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL("ALTER TABLE favourites RENAME to favourites_nasa_library")
        }
    }
}