package com.justcircleprod.randomnasaimages.data.remote.responses

data class Item(
    val `data`: List<Data>,
    val href: String,
    val links: List<Link>
)