package com.justcircleprod.randomspaceimages.ui.random.favouriteImageList

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator
import com.justcircleprod.randomspaceimages.ui.random.imageEntryItem.NASALibraryImageEntryItem
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily

@Composable
fun FavouriteImageList(
    viewModel: FavouriteImageListViewModel,
    onImageEntryClick: (nasaLibraryImageEntry: NASALibraryImageEntry) -> Unit
) {
    val favourites by viewModel.favourites.observeAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.setFavourites(refresh = true) },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                scale = true,
                backgroundColor = colorResource(id = R.color.card_background),
                contentColor = colorResource(id = R.color.primary)
            )
        },
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.elements_space_size))
            .fillMaxSize()
    ) {
        Box {
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
        }
    }
}

@Composable
fun NoFavourites() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.no_favourites),
            color = colorResource(id = R.color.text),
            fontFamily = LatoFontFamily,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.rocket_icon_space_size)))
        Icon(
            painter = painterResource(id = R.drawable.icon_rocket),
            contentDescription = null,
            tint = colorResource(id = R.color.icon_tint),
            modifier = Modifier.size(dimensionResource(id = R.dimen.info_icon_size))
        )
    }
}