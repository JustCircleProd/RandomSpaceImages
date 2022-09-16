package com.justcircleprod.randomnasaimages.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.justcircleprod.randomnasaimages.R
import com.justcircleprod.randomnasaimages.ui.common.*
import com.justcircleprod.randomnasaimages.ui.theme.DefaultFontFamily

@Composable
fun SearchScreen(
    navController: NavHostController
) {
    val viewModel: SearchViewModel = hiltViewModel()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            viewModel = viewModel,
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .padding(bottom = 6.dp)
                .padding(top = 10.dp)
        )

        ImageList(navController = navController, viewModel = viewModel)
    }
}

@Composable
fun SearchBar(
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var searchText by rememberSaveable { mutableStateOf("") }

    BasicTextField(
        value = searchText,
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        maxLines = 1,
        textStyle = TextStyle(
            fontFamily = DefaultFontFamily,
            fontSize = 15.sp,
            color = colorResource(id = R.color.search_bar_text_color)
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(searchText, viewModel, focusManager)
            }
        ),
        onValueChange = {
            searchText = it
        },
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.search_bar_rounded_corner_radius)))
            .background(
                colorResource(
                    id = R.color.search_bar_background_color
                )
            )
            .padding(vertical = dimensionResource(id = R.dimen.search_bar_vertical_padding))
            .padding(
                start = dimensionResource(id = R.dimen.search_bar_start_padding),
                end = dimensionResource(id = R.dimen.search_bar_end_padding)
            )
    ) { innerTextField ->
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (contentBox, trailingIconButton) = createRefs()

            Box(
                modifier = Modifier.constrainAs(contentBox) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(trailingIconButton.start)
                }
            ) {
                innerTextField()

                if (searchText.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.search_bar_placeholder),
                        fontSize = 15.sp,
                        color = colorResource(id = R.color.search_bar_placeholder_color)
                    )
                }
            }

            IconButton(
                onClick = {
                    onSearch(searchText, viewModel, focusManager)
                },
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.search_bar_icon_button_width))
                    .height(dimensionResource(id = R.dimen.search_bar_icon_button_height))
                    .constrainAs(trailingIconButton) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_search),
                    contentDescription = null,
                    tint = colorResource(id = R.color.search_bar_trailing_icon_tint),
                    modifier = Modifier.size(dimensionResource(id = R.dimen.search_bar_icon_size))
                )
            }

            createHorizontalChain(
                contentBox,
                trailingIconButton,
                chainStyle = ChainStyle.SpreadInside
            )
        }

    }
}

private fun onSearch(text: String, viewModel: SearchViewModel, focusManager: FocusManager) {
    if (text.isNotBlank()) {
        viewModel.q = text
        viewModel.searchImages()
        focusManager.clearFocus()
    }
}

@Composable
fun ImageList(
    navController: NavHostController,
    viewModel: SearchViewModel
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

            if (noResults && !isLoading && !isRefreshing) {
                NoResults()
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

@Composable
fun NoResults() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.search_no_results),
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