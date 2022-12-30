package com.justcircleprod.randomspaceimages.ui.apod.apodPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.apod.apodEntryItem.APODEntryItem
import com.justcircleprod.randomspaceimages.ui.common.ErrorInfo
import com.justcircleprod.randomspaceimages.ui.common.ErrorInfoCard
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun APODPage(
    viewModel: APODPageViewModel,
    onAPODEntryImageClick: (imageUrl: String) -> Unit
) {
    val apodList by viewModel.apodList.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val loadError by viewModel.loadError.collectAsState()

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val endReached by viewModel.endReached.collectAsState()

    val isDatePicked by viewModel.isDatePicked.collectAsState()

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
                if (!isLoading && !isRefreshing && !isDatePicked) {
                    viewModel.loadTodayAPOD(refresh = true)
                }
            }
        )

        Box(
            modifier = Modifier
                .pullRefresh(pullRefreshState)
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            if (loadError && apodList.isEmpty() && !isLoading) {
                ErrorInfo(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.elements_space_size)))
            }

            if (isLoading && apodList.isEmpty()) {
                ProgressIndicator(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.elements_space_size)))
            }

            Column(modifier = Modifier.fillMaxSize()) {
                if (loadError && apodList.isNotEmpty()) {
                    ErrorInfoCard()
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.apod_list_vertical_arragment)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(bottom = dimensionResource(id = R.dimen.apod_screen_bottom_space)),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(apodList.size) {
                        if (apodList.size != 1 && it >= apodList.size - 1 && !isLoading && !endReached) {
                            viewModel.loadMoreAPODs()
                        }

                        APODEntryItem(
                            apodEntry = apodList[it],
                            viewModel = viewModel,
                            scaffoldState = scaffoldState,
                            onImageClick = onAPODEntryImageClick
                        )
                    }

                    if (apodList.size == 1 && !isLoading && !endReached && !isDatePicked) {
                        item {
                            LoadMoreButton(onClick = {
                                viewModel.loadMoreAPODs()
                            })
                        }
                    }

                    if (isLoading && apodList.isNotEmpty()) {
                        item {
                            ProgressIndicator()
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
}

@Composable
fun LoadMoreButton(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.button_rounded_corner_radius)))
            .background(colorResource(id = R.color.primary))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    color = colorResource(id = R.color.ripple)
                ),
            ) {
                onClick()
            }
            .padding(vertical = dimensionResource(id = R.dimen.button_padding_vertical))
            .padding(horizontal = dimensionResource(id = R.dimen.button_padding_horizontal))
    ) {
        Text(
            text = stringResource(id = R.string.load_more),
            color = colorResource(id = R.color.button_text),
            fontFamily = LatoFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}