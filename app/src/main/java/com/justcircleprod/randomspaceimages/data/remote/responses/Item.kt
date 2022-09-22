package com.justcircleprod.randomspaceimages.data.remote.responses

data class Item(
    val `data`: List<Data>,
    val href: String,
    val links: List<Link>
)