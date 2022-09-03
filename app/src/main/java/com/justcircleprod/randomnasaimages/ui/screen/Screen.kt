package com.justcircleprod.randomnasaimages.ui.screen

sealed class Screen(
    val route: String
) {
    object Home : Screen("home")

    object Favourites : Screen("favourites")

    class Detail(imageEntry: String = "{$IMAGE_ENTRY_ARGUMENT_NAME}") :
        Screen("detail/$imageEntry") {
        companion object {
            const val IMAGE_ENTRY_ARGUMENT_NAME = "image_entry"
        }
    }
}