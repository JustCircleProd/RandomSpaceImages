package com.justcircleprod.randomspaceimages.ui.random.randomFavouritesPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.ui.common.NoFavourites
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator
import com.justcircleprod.randomspaceimages.ui.random.imageEntryItem.NASALibraryImageEntryItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RandomFavouritesPage(
    viewModel: RandomFavouritesPageViewModel,
    onImageEntryClick: (nasaLibraryImageEntry: NASALibraryImageEntry) -> Unit
) {
    val favourites by viewModel.favourites.observeAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            if (!isLoading && !isRefreshing) {
                viewModel.setFavourites(refresh = true)
            }
        }
    )

    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
    ) {
        if (favourites == null || favourites!!.isEmpty() && !isLoading) {
            NoFavourites()
        }

        if (isLoading) {
            ProgressIndicator()
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(dimensionResource(id = R.dimen.image_list_min_grid_cell_size)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.image_list_vertical_arrangement)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.image_list_horizontal_arrangement)),
            modifier = Modifier.fillMaxSize()
        ) {
            if (favourites != null) {
                items(favourites!!.size) {
                    NASALibraryImageEntryItem(
                        nasaLibraryImageEntry = favourites!![it],
                        viewModel = viewModel,
                        onImageEntryClick = onImageEntryClick
                    )
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(Modifier.height(dimensionResource(id = R.dimen.image_list_bottom_space)))
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            scale = true,
            backgroundColor = colorResource(id = R.color.card_background),
            contentColor = colorResource(id = R.color.primary),
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}