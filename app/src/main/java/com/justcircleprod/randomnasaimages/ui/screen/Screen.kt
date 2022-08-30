package com.justcircleprod.randomnasaimages.ui.screen

sealed class Screen(
    val route: String
) {
    object Home : Screen("home")

    object Favourites : Screen("favourites")

    class DetailImage(imageEntry: String = "{image_entry}") :
        Screen("detail_image/$imageEntry") {
        companion object {
            const val IMAGE_ENTRY_ARGUMENT_NAME = "image_entry"
        }
    }
}