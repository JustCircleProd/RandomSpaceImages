package com.justcircleprod.randomspaceimages.ui.navigation

sealed class Screen(
    val route: String
) {
    object Home : Screen("home")

    object Search : Screen("search")

    class SearchResult(
        yearStart: Int? = null,
        yearEnd: Int? = null,
        q: String? = null,
    ) : Screen("search_result/${yearStart ?: "{$YEAR_START_ARGUMENT_NAME}"}/${yearEnd ?: "{$YEAR_END_ARGUMENT_NAME}"}?q=${q ?: "{$Q_ARGUMENT_NAME}"}/") {
        companion object {
            const val YEAR_START_ARGUMENT_NAME = "yearStart"
            const val YEAR_END_ARGUMENT_NAME = "yearEnd"
            const val Q_ARGUMENT_NAME = "q"
        }
    }

    class Detail(imageEntry: String? = null) :
        Screen("detail/${imageEntry ?: "{$IMAGE_ENTRY_ARGUMENT_NAME}"}") {
        companion object {
            const val IMAGE_ENTRY_ARGUMENT_NAME = "image_entry"
        }
    }

    object More : Screen("more")
}