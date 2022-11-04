package com.justcircleprod.randomspaceimages.ui.search.searchResult

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.common.*
import com.justcircleprod.randomspaceimages.ui.theme.customColors

@Composable
fun SearchResultScreen(navController: NavHostController, viewModel: SearchResultViewModel) {
    ConstraintLayout {
        val (imageList, backButton) = createRefs()
        SearchImageList(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier.constrainAs(imageList) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            })

        val onBackButtonClick = {
            navController.popBackStack()
            Unit
        }

        BackButton(onBackButtonClick, modifier = Modifier
            .constrainAs(backButton) {
                top.linkTo(parent.top, margin = 6.dp)
                start.linkTo(parent.start, margin = 8.dp)
            }
        )
    }
}

@Composable
fun SearchImageList(
    navController: NavHostController,
    viewModel: SearchResultViewModel,
    modifier: Modifier = Modifier
) {
    val images by viewModel.images.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val loadError by viewModel.loadError.collectAsState()
    val noResults by viewModel.noResults.collectAsState()

    val endReached by viewModel.endReached.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        swipeEnabled = images.isNotEmpty() || loadError,
        onRefresh = { viewModel.searchImages(refresh = true) },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                scale = true,
                backgroundColor = MaterialTheme.customColors.cardBackground,
                contentColor = MaterialTheme.colors.primary
            )
        },
        modifier = modifier.fillMaxSize()
    ) {
        Box {
            if (loadError && images.isEmpty() && !isLoading) {
                ErrorInfo(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.elements_space_size)))
            }

            if (noResults && !isLoading && !isRefreshing) {
                NoResults(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.elements_space_size)))
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
                    if (images.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Spacer(modifier = Modifier.height(0.dp))
                        }
                    }

                    items(images.size) {
                        if (it >= images.size - 1 && !endReached && !isLoading) {
                            viewModel.loadImages()
                        }

                        ImageEntryItem(
                            imageEntry = images[it]!!,
                            navController = navController,
                            viewModel = viewModel
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
                            Spacer(Modifier.height(dimensionResource(id = R.dimen.image_list_vertical_arrangement)))
                        }
                    }

                    if (endReached || loadError && images.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.image_list_bottom_space)))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoResults(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.search_no_results),
            color = MaterialTheme.customColors.text,
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