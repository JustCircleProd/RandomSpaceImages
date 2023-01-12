package com.justcircleprod.randomspaceimages.ui.apod.apodFavourites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
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
import com.justcircleprod.randomspaceimages.ui.apod.apodEntryItem.APODEntryItem
import com.justcircleprod.randomspaceimages.ui.common.NoFavourites
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun APODFavouritesPage(
    viewModel: APODFavouritesPageViewModel,
    onAPODEntryImageClick: (imageUrl: String, imageUrlHd: String?) -> Unit
) {
    val favourites by viewModel.favourites.observeAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        backgroundColor = colorResource(id = R.color.background),
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.apod_snackbar_host_bottom_space_size))
            ) { data ->
                Snackbar(
                    actionColor = colorResource(id = R.color.primary),
                    snackbarData = data,
                    elevation = dimensionResource(id = R.dimen.snackbar_elevation),
                    backgroundColor = colorResource(id = R.color.snackbar_background),
                    contentColor = colorResource(id = R.color.snackbar_text)
                )
            }
        }
    ) { scaffoldPadding ->
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
                .padding(scaffoldPadding)
        ) {
            if (favourites == null || favourites!!.isEmpty() && !isLoading) {
                NoFavourites()
            }

            if (isLoading) {
                ProgressIndicator()
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.apod_list_vertical_arragment)),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = dimensionResource(id = R.dimen.apod_screen_bottom_space)),
                modifier = Modifier.fillMaxSize()
            ) {
                if (favourites != null) {
                    items(favourites!!.size) {
                        APODEntryItem(
                            apodEntry = favourites!![it],
                            viewModel = viewModel,
                            scaffoldState = scaffoldState,
                            onImageClick = onAPODEntryImageClick
                        )
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
}