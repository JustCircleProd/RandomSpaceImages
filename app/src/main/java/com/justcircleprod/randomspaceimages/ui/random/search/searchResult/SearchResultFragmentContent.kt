package com.justcircleprod.randomspaceimages.ui.random.search.searchResult

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.domain.model.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.ui.common.BackButton
import com.justcircleprod.randomspaceimages.ui.common.ErrorInfo
import com.justcircleprod.randomspaceimages.ui.common.ErrorInfoCard
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator
import com.justcircleprod.randomspaceimages.ui.random.imageEntryItem.NASALibraryImageEntryItem
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily

@Composable
fun SearchResultScreen(
    viewModel: SearchResultViewModel,
    onImageEntryClick: (nasaLibraryImageEntry: NASALibraryImageEntry) -> Unit,
    onBackButtonClick: () -> Unit
) {
    Scaffold(
        backgroundColor = colorResource(id = R.color.background),
        scaffoldState = rememberScaffoldState()
    )
    { scaffoldPadding ->
        ConstraintLayout(modifier = Modifier.padding(scaffoldPadding)) {
            val (imageList, backButton) = createRefs()
            SearchImageList(
                viewModel = viewModel,
                onImageEntryClicked = onImageEntryClick,
                modifier = Modifier.constrainAs(imageList) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                })

            BackButton(onBackButtonClick, modifier = Modifier
                .constrainAs(backButton) {
                    top.linkTo(parent.top, margin = 6.dp)
                    start.linkTo(parent.start, margin = 8.dp)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchImageList(
    viewModel: SearchResultViewModel,
    onImageEntryClicked: (nasaLibraryImageEntry: NASALibraryImageEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    val images by viewModel.images.collectAsStateWithLifecycle()

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val loadError by viewModel.loadError.collectAsStateWithLifecycle()
    val noResults by viewModel.noResults.collectAsStateWithLifecycle()

    val endReached by viewModel.endReached.collectAsStateWithLifecycle()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            if (!isLoading && !isRefreshing) {
                viewModel.searchImages(refresh = true)
            }
        }
    )

    Box(
        modifier = modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
    ) {
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
                columns = GridCells.Adaptive(dimensionResource(id = R.dimen.random_list_min_grid_cell_size)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.random_list_vertical_arrangement)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.random_list_horizontal_arrangement)),
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

                    NASALibraryImageEntryItem(
                        nasaLibraryImageEntry = images[it]!!,
                        viewModel = viewModel,
                        onImageEntryClick = onImageEntryClicked
                    )
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
                        Spacer(Modifier.height(dimensionResource(id = R.dimen.random_list_vertical_arrangement)))
                    }
                }

                if (endReached || loadError && images.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.random_list_bottom_space)))
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

@Composable
fun NoResults(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.search_no_results),
            color = colorResource(id = R.color.text),
            fontFamily = LatoFontFamily,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.rocket_icon_space_size)))
        Icon(
            painter = painterResource(id = R.drawable.icon_rocket),
            tint = colorResource(id = R.color.icon_tint),
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(id = R.dimen.info_icon_size))
        )
    }
}