package com.justcircleprod.randomspaceimages.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favourites")
data class NASALibraryImageEntry(
    @PrimaryKey @ColumnInfo(name = "nasaId") val nasaId: String,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "center") val center: String?,
    @ColumnInfo(name = "location") val location: String?,
    @ColumnInfo(name = "dateCreated") val dateCreated: String,
    @ColumnInfo(name = "imageHref") val imageHref: String,
    @ColumnInfo(name = "secondaryCreator") val secondaryCreator: String?,
    @ColumnInfo(name = "photographer") val photographer: String?
) : Parcelable