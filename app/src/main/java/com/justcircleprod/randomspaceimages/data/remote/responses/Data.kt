package com.justcircleprod.randomspaceimages.data.remote.responses

data class Data(
    val center: String?,
    val location: String?,
    val date_created: String,
    val description: String?,
    val description_508: String?,
    val keywords: List<String>,
    val media_type: String,
    val nasa_id: String,
    val secondary_creator: String?,
    val title: String?,
    val photographer: String?
)