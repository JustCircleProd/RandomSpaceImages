package com.justcircleprod.randomspaceimages.ui.apod.apodFavourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
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
import kotlinx.coroutines.CoroutineScope


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun APODFavouritesPage(
    viewModel: APODFavouritesPageViewModel,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    onAPODEntryImageClick: (imageUrl: String, imageUrlHd: String?) -> Unit
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
                        coroutineScope = coroutineScope,
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