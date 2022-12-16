package com.justcircleprod.randomspaceimages.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites_apod")
data class APODEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "copyright") val copyright: String?,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "explanation") val explanation: String,
    @ColumnInfo(name = "hdurl") val hdurl: String,
    @ColumnInfo(name = "media_type") val media_type: String,
    @ColumnInfo(name = "service_version") val service_version: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "url") val url: String
)