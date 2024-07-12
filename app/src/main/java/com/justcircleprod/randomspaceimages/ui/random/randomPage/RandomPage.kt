package com.justcircleprod.randomspaceimages.ui.random.randomPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.domain.model.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.ui.common.ErrorInfo
import com.justcircleprod.randomspaceimages.ui.common.ErrorInfoCard
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator
import com.justcircleprod.randomspaceimages.ui.random.imageEntryItem.NASALibraryImageEntryItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RandomPage(
    viewModel: RandomPageViewModel,
    onImageEntryClick: (nasaLibraryImageEntry: NASALibraryImageEntry) -> Unit
) {
    val images by viewModel.images.collectAsStateWithLifecycle()

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val loadError by viewModel.loadError.collectAsStateWithLifecycle()

    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val endReached by viewModel.endReached.collectAsStateWithLifecycle()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            if (!isLoading && !isRefreshing) {
                viewModel.loadImages(refresh = true)
            }
        }
    )

    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
    ) {
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
                columns = GridCells.Adaptive(dimensionResource(id = R.dimen.random_list_min_grid_cell_size)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.random_list_vertical_arrangement)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.random_list_horizontal_arrangement)),
                contentPadding = PaddingValues(bottom = dimensionResource(id = R.dimen.random_list_bottom_space)),
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
                }

                if (isLoading && images.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ProgressIndicator()
                    }
                }

                if (loadError && images.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(Modifier.height(dimensionResource(id = R.dimen.random_list_bottom_space)))
                    }
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