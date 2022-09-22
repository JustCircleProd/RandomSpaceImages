package com.justcircleprod.randomspaceimages.data.remote.responses

data class Collection(
    val href: String,
    val items: List<Item>,
    val links: List<LinkX>,
    val metadata: Metadata,
    val version: String
)