package com.justcircleprod.randomspaceimages.ui.apod.apodFavourites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.apod.apodEntryItem.APODEntryItem
import com.justcircleprod.randomspaceimages.ui.apod.apodPagesSnackbarState.APODPagesSnackBarState
import com.justcircleprod.randomspaceimages.ui.common.NoFavourites
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun APODFavouritesPage(
    viewModel: APODFavouritesPageViewModel,
    onAPODEntryImageClick: (imageUrl: String, imageUrlHd: String?) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()

    LaunchedEffect(snackBarState) {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()

            when (snackBarState) {
                APODPagesSnackBarState.SavingImage -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.saving_the_image),
                        actionLabel = context.getString(R.string.dismiss),
                        duration = SnackbarDuration.Indefinite
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {
                            viewModel.snackBarState.value = null
                        }
                    }
                }

                APODPagesSnackBarState.FailedToSaveImage -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.failed_to_save),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                APODPagesSnackBarState.SuccessfullySavedImage -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.successfully_saved_to_gallery),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                APODPagesSnackBarState.GrantPermission -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.grant_permission),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                APODPagesSnackBarState.PermissionIsRequired -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.permission_is_required),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                APODPagesSnackBarState.PreparingToShare -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.preparing_to_share),
                        actionLabel = context.getString(R.string.dismiss),
                        duration = SnackbarDuration.Indefinite
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {
                            coroutineScope.launch {
                                viewModel.snackBarState.value = null
                            }
                        }
                    }
                }

                APODPagesSnackBarState.FailedToShare -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        context.getString(R.string.failed_to_share),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                APODPagesSnackBarState.DownloadingLanguageModel -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.downloading_language_model),
                        actionLabel = context.getString(R.string.dismiss),
                        duration = SnackbarDuration.Indefinite
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {
                            coroutineScope.launch {
                                viewModel.snackBarState.value = null
                            }
                        }
                    }
                }

                APODPagesSnackBarState.FailedToDownloadLanguageModel -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        context.getString(R.string.failed_to_download_language_model),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                APODPagesSnackBarState.Translating -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.translating),
                        actionLabel = context.getString(R.string.dismiss),
                        duration = SnackbarDuration.Indefinite
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {
                            viewModel.snackBarState.value = null
                        }
                    }
                }

                APODPagesSnackBarState.FailedToTranslate -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        context.getString(R.string.failed_to_translate),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                APODPagesSnackBarState.FailedToOpenVideo -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        context.getString(R.string.failed_to_open_video),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                null -> {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                }
            }
        }
    }

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
        val apodStates by viewModel.apodsStates.collectAsStateWithLifecycle()

        val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
        val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshing,
            onRefresh = {
                if (!isLoading && !isRefreshing) {
                    viewModel.onEvent(APODFavouritesPageEvent.SetFavourites(refresh = true))
                }
            }
        )

        Box(
            modifier = Modifier
                .pullRefresh(pullRefreshState)
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            if (apodStates.isEmpty() && !isLoading) {
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
                items(apodStates.size) {
                    val apodEntry = viewModel.favourites[it]

                    APODEntryItem(
                        apodEntry = apodEntry,
                        apodStates = apodStates[it],
                        context = context,
                        viewModel = viewModel,
                        onImageClick = onAPODEntryImageClick,
                        onOpenVideoError = {
                            viewModel.snackBarState.value = APODPagesSnackBarState.FailedToOpenVideo
                        },
                        onFavouriteButtonClick = {
                            viewModel.onEvent(
                                APODFavouritesPageEvent.OnFavouriteButtonClick(
                                    apodEntry
                                )
                            )
                        },
                        onSaveImageButtonClick = {
                            viewModel.onEvent(
                                APODFavouritesPageEvent.SaveImage(
                                    context = context,
                                    title = apodEntry.title,
                                    url = apodEntry.url,
                                    hdUrl = apodEntry.hdurl
                                )
                            )
                        },
                        onShareImageButtonClick = {
                            viewModel.onEvent(
                                APODFavouritesPageEvent.ShareImage(
                                    context = context,
                                    title = apodEntry.title,
                                    url = apodEntry.url,
                                    hdUrl = apodEntry.hdurl
                                )
                            )
                        },
                        onShareVideoButtonClick = {
                            viewModel.onEvent(
                                APODFavouritesPageEvent.ShareVideo(
                                    context = context,
                                    title = apodEntry.title,
                                    url = apodEntry.url
                                )
                            )
                        },
                        onTranslateButtonClick = {
                            viewModel.onEvent(APODFavouritesPageEvent.Translate(apodIndex = it))
                        }
                    )
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