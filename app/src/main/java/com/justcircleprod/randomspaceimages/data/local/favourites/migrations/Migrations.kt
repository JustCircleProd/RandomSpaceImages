package com.justcircleprod.randomspaceimages.data.local.favourites.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        with(db) {
            execSQL("ALTER TABLE favourites RENAME to nasa_library_favourites")

            execSQL(
                "CREATE TABLE IF NOT EXISTS apod_favourites(" +
                        "copyright TEXT," +
                        "date TEXT NOT NULL," +
                        "explanation TEXT NOT NULL," +
                        "hdurl TEXT," +
                        "media_type TEXT NOT NULL," +
                        "service_version TEXT NOT NULL," +
                        "title TEXT NOT NULL," +
                        "url TEXT NOT NULL," +
                        "PRIMARY KEY (date))"
            )
        }
    }
}

val MIGRATION_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        with(db) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS apod_favourites_backup(" +
                        "copyright TEXT," +
                        "date TEXT NOT NULL," +
                        "explanation TEXT NOT NULL," +
                        "hdurl TEXT," +
                        "media_type TEXT NOT NULL," +
                        "service_version TEXT NOT NULL," +
                        "title TEXT NOT NULL," +
                        "url TEXT NOT NULL," +
                        "PRIMARY KEY (date))"
            )
            execSQL("INSERT INTO apod_favourites_backup SELECT * FROM apod_favourites")
            execSQL("DROP TABLE apod_favourites")
            execSQL("ALTER TABLE apod_favourites_backup RENAME to apod_favourites")
        }
    }
}