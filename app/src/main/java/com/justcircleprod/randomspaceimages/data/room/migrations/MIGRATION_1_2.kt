package com.justcircleprod.randomspaceimages.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL("ALTER TABLE favourites RENAME to nasa_library_favourites")

            execSQL(
                "CREATE TABLE IF NOT EXISTS apod_favourites(" +
                        "copyright TEXT," +
                        "date TEXT NOT NULL," +
                        "explanation TEXT NOT NULL," +
                        "hdurl TEXT NOT NULL," +
                        "media_type TEXT NOT NULL," +
                        "service_version TEXT NOT NULL," +
                        "title TEXT NOT NULL," +
                        "url TEXT NOT NULL," +
                        "PRIMARY KEY (date))"
            )
        }
    }
}