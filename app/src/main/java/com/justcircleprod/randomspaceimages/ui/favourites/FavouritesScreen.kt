package com.justcircleprod.randomspaceimages.ui.favourites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.justcircleprod.randomspaceimages.ui.common.ImageItem
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator

@Composable
fun FavouritesScreen(navController: NavHostController) {
    val viewModel: FavouritesViewModel = hiltViewModel()

    LaunchedEffect(true) {
        viewModel.setFavourites()
    }

    ImageList(navController = navController, viewModel = viewModel)
}

@Composable
fun ImageList(
    navController: NavHostController,
    viewModel: FavouritesViewModel
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
                backgroundColor = colorResource(id = R.color.card_background_color),
                contentColor = MaterialTheme.colors.primary
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            if (favourites == null || favourites!!.isEmpty() && !isLoading) {
                NoFavourites()
            }

            if (isLoading) {
                ProgressIndicator()
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(dimensionResource(id = R.dimen.min_grid_cell_size)),
                modifier = Modifier.fillMaxSize()
            ) {
                if (favourites != null) {
                    items(favourites!!.size) {
                        ImageItem(
                            imageEntry = favourites!![it],
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_space)))
                    }
                }
            }
        }
    }
}

@Composable
fun NoFavourites() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.no_favourites),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.rocket_icon_space_size)))
        Icon(
            painter = painterResource(id = R.drawable.icon_rocket),
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(id = R.dimen.info_icon_size))
        )
    }
}