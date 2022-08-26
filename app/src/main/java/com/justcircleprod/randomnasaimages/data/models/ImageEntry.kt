package com.justcircleprod.randomnasaimages.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageEntry(
    val title: String?,
    val description: String?,
    val creator: String?,
    val dateCreated: String,
    val imageHref: String,
) : Parcelable