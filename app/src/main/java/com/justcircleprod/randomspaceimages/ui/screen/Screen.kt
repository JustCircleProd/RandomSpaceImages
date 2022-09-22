package com.justcircleprod.randomspaceimages.ui.screen

sealed class Screen(
    val route: String
) {
    object Home : Screen("home")

    object Favourites : Screen("favourites")

    object Search : Screen("search")

    class Detail(imageEntry: String = "{$IMAGE_ENTRY_ARGUMENT_NAME}") :
        Screen("detail/$imageEntry") {
        companion object {
            const val IMAGE_ENTRY_ARGUMENT_NAME = "image_entry"
        }
    }
}