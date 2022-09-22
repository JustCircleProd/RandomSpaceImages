package com.justcircleprod.randomspaceimages.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import com.justcircleprod.randomspaceimages.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.justcircleprod.randomspaceimages.ui.common.*

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val viewModel: HomeViewModel = hiltViewModel()

    ImageList(
        navController = navController,
        viewModel = viewModel
    )
}

@Composable
fun ImageList(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    val images by viewModel.images.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val loadError by viewModel.loadError.collectAsState()

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val endReached by viewModel.endReached.collectAsState()


    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.loadImages(refresh = true) },
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
            if (loadError && images.isEmpty() && !isLoading) {
                ErrorInfo()
            }

            if (isLoading && images.isEmpty()) {
                ProgressIndicator()
            }

            Column(modifier = Modifier.fillMaxSize()) {
                if (loadError && images.isNotEmpty()) {
                    ErrorInfoCard()
                }

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(dimensionResource(id = R.dimen.min_grid_cell_size)),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(images.size) {
                        if (it >= images.size - 1 && !endReached && !isLoading) {
                            viewModel.loadImages()
                        }

                        if (images[it] != null) {
                            ImageItem(
                                imageEntry = images[it]!!,
                                navController = navController,
                                viewModel = viewModel
                            )
                        } else {
                            Ad()
                        }
                    }

                    if (isLoading && images.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            ProgressIndicator()
                        }
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_space)))
                        }
                    }

                    if (endReached || loadError && images.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_space)))
                        }
                    }
                }
            }
        }
    }
}