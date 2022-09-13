package com.justcircleprod.randomnasaimages.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.justcircleprod.randomnasaimages.R
import com.justcircleprod.randomnasaimages.ui.common.Ad
import com.justcircleprod.randomnasaimages.ui.common.ImageItem
import com.justcircleprod.randomnasaimages.ui.common.ProgressIndicator
import com.justcircleprod.randomnasaimages.ui.theme.ErrorCardBackground

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

    Column {
        if (loadError && images.isNotEmpty() && !isLoading) {
            ErrorInfoCard()
        }

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
            }
        ) {
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

    if (isLoading && images.isEmpty()) {
        ProgressIndicator()
    }

    if (loadError && images.isEmpty()) {
        ErrorInfo()
    }
}

@Composable
fun ErrorInfo() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.error_info_text),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.rocket_icon_space_size)))
        Icon(
            imageVector = Icons.Default.RocketLaunch,
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(id = R.dimen.info_icon_size))
        )
    }
}

@Composable
fun ErrorInfoCard() {
    Card(
        backgroundColor = ErrorCardBackground,
        shape = RectangleShape,
        elevation = dimensionResource(id = R.dimen.card_elevation),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.error_info_card_horizontal_space_size))
                .padding(vertical = dimensionResource(id = R.dimen.error_info_card_vertical_space_size)),
        ) {

            Text(
                text = stringResource(id = R.string.error_info_card_text),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.rocket_launch_icon_space_size)))

            Icon(
                imageVector = Icons.Default.RocketLaunch,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.error_info_card_icon_size))
            )
        }
    }
}