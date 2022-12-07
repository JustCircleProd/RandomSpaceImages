package com.justcircleprod.randomspaceimages.ui.random.randomImageList

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.ui.common.ErrorInfo
import com.justcircleprod.randomspaceimages.ui.common.ErrorInfoCard
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator
import com.justcircleprod.randomspaceimages.ui.random.imageEntryItem.NASALibraryImageEntryItem

@Composable
fun RandomImageList(
    viewModel: RandomImageListViewModel,
    onImageEntryClick: (nasaLibraryImageEntry: NASALibraryImageEntry) -> Unit
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
                backgroundColor = colorResource(id = R.color.card_background),
                contentColor = colorResource(id = R.color.primary)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Box {
            if (loadError && images.isEmpty() && !isLoading) {
                ErrorInfo(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.elements_space_size)))
            }

            if (isLoading && images.isEmpty()) {
                ProgressIndicator(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.elements_space_size)))
            }

            Column(modifier = Modifier.fillMaxSize()) {
                if (loadError && images.isNotEmpty()) {
                    ErrorInfoCard()
                    Spacer(Modifier.height(dimensionResource(id = R.dimen.elements_space_size)))
                }

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(dimensionResource(id = R.dimen.image_list_min_grid_cell_size)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.image_list_vertical_arrangement)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.image_list_horizontal_arrangement)),
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(id = R.dimen.elements_space_size))
                        .fillMaxSize()
                ) {
                    items(images.size) {
                        if (it >= images.size - 1 && !endReached && !isLoading) {
                            viewModel.loadImages()
                        }

                        NASALibraryImageEntryItem(
                            nasaLibraryImageEntry = images[it]!!,
                            viewModel = viewModel,
                            onImageEntryClick = onImageEntryClick
                        )

                        /*if (images[it] != null) {
                    ImageItem(
                        imageEntry = images[it]!!,
                        navController = navController,
                        viewModel = viewModel
                    )
                } else {
                    Ad()
                }*/
                    }

                    if (isLoading && images.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            ProgressIndicator(
                                modifier = Modifier.padding(
                                    horizontal = dimensionResource(
                                        id = R.dimen.elements_space_size
                                    )
                                )
                            )
                        }
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Spacer(Modifier.height(dimensionResource(id = R.dimen.image_list_bottom_space)))
                        }
                    }

                    if (endReached || loadError && images.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Spacer(Modifier.height(dimensionResource(id = R.dimen.image_list_bottom_space)))
                        }
                    }
                }
            }
        }
    }
}