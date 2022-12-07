package com.justcircleprod.randomspaceimages.data.remote.nasaLibrary.responses

data class Item(
    val `data`: List<Data>,
    val href: String,
    val links: List<Link>
)