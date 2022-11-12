package com.justcircleprod.randomspaceimages.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.remote.RemoteConstants.NASA_LIBRARY_YEAR_END
import com.justcircleprod.randomspaceimages.data.remote.RemoteConstants.NASA_LIBRARY_YEAR_START
import com.justcircleprod.randomspaceimages.ui.search.searchBar.SearchBar
import com.justcircleprod.randomspaceimages.ui.search.searchCards.SearchCardTitle
import com.justcircleprod.randomspaceimages.ui.search.searchCards.SuggestionCard
import com.justcircleprod.randomspaceimages.ui.search.searchCards.YearRangeCard
import com.justcircleprod.randomspaceimages.ui.search.searchCards.suggestions.solarSystem

@Composable
fun SearchFragmentContent(onSearchCallback: (q: String, yearStart: Int, yearEnd: Int) -> Unit) {
    val searchText = rememberSaveable { mutableStateOf("") }
    val yearStart = rememberSaveable { mutableStateOf(NASA_LIBRARY_YEAR_START) }
    val yearEnd = rememberSaveable { mutableStateOf(NASA_LIBRARY_YEAR_END) }

    val focusManager = LocalFocusManager.current

    val onSearch = {
        focusManager.clearFocus()

        onSearchCallback(
            searchText.value,
            yearStart.value,
            yearEnd.value
        )
    }

    val onSuggestionCardClick: (String) -> Unit = { suggestionText ->
        searchText.value = suggestionText
        focusManager.clearFocus()

        onSearchCallback(
            searchText.value,
            yearStart.value,
            yearEnd.value
        )
    }

    val solarSystemSuggestions = remember { solarSystem }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(dimensionResource(id = R.dimen.image_list_min_grid_cell_size)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.search_screen_arrangement)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.search_screen_arrangement)),
        contentPadding = PaddingValues(bottom = dimensionResource(id = R.dimen.search_screen_bottom_space_size)),
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.search_screen_horizontal_space_size))
            .fillMaxSize()
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            SearchBar(searchText = searchText, onSearch = onSearch)
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            SearchCardTitle(titleText = stringResource(id = R.string.year_range))
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            YearRangeCard(
                yearStart = yearStart,
                yearEnd = yearEnd
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            SearchCardTitle(titleText = stringResource(id = R.string.solar_system))
        }

        items(solarSystemSuggestions.size) {
            SuggestionCard(
                suggestionStringRes = solarSystemSuggestions[it].first,
                SuggestionImageRes = solarSystemSuggestions[it].second,
                onClick = onSuggestionCardClick
            )
        }
    }
}