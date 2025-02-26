package com.justcircleprod.randomspaceimages.ui.apod.apodPage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.apod.apodEntryItem.APODEntryItem
import com.justcircleprod.randomspaceimages.ui.apod.apodPagesSnackbarState.APODPagesSnackBarState
import com.justcircleprod.randomspaceimages.ui.common.ErrorInfo
import com.justcircleprod.randomspaceimages.ui.common.ErrorInfoCard
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun APODPage(
    viewModel: APODPageViewModel,
    onAPODEntryImageClick: (imageUrl: String, imageUrlHd: String?) -> Unit,
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
        val apodsStates by viewModel.apodsStates.collectAsStateWithLifecycle()

        val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
        val loadError by viewModel.loadError.collectAsStateWithLifecycle()

        val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

        val endReached by viewModel.endReached.collectAsStateWithLifecycle()

        val pickedDate by viewModel.pickedDateInMills.collectAsStateWithLifecycle()

        val pullRefreshState = rememberPullRefreshState(
            refreshing = isRefreshing,
            onRefresh = {
                if (!isLoading && !isRefreshing && pickedDate == null) {
                    viewModel.onEvent(APODPageEvent.LoadTodayAPOD(refresh = true))
                }
            }
        )

        Box(
            modifier = Modifier
                .pullRefresh(pullRefreshState)
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            if (loadError && apodsStates.isEmpty() && !isLoading) {
                ErrorInfo(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.elements_space_size)))
            }

            if (isLoading && apodsStates.isEmpty()) {
                ProgressIndicator(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.elements_space_size)))
            }

            Column(modifier = Modifier.fillMaxSize()) {
                if (loadError && apodsStates.isNotEmpty()) {
                    ErrorInfoCard()
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.apod_list_vertical_arragment)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(bottom = dimensionResource(id = R.dimen.apod_screen_bottom_space)),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(apodsStates.size) {
                        if (apodsStates.size != 1 && it >= apodsStates.size - 1 && !isLoading && !endReached) {
                            viewModel.onEvent(APODPageEvent.LoadMoreAPODs)
                        }

                        val apodEntry = viewModel.apodList[it]

                        APODEntryItem(
                            apodEntry = apodEntry,
                            apodStates = apodsStates[it],
                            context = context,
                            viewModel = viewModel,
                            onImageClick = onAPODEntryImageClick,
                            onOpenVideoError = {
                                viewModel.snackBarState.value =
                                    APODPagesSnackBarState.FailedToOpenVideo
                            },
                            onFavouriteButtonClick = {
                                viewModel.onEvent(APODPageEvent.OnFavouriteButtonClick(apodEntry))
                            },
                            onSaveImageButtonClick = {
                                viewModel.onEvent(
                                    APODPageEvent.SaveImage(
                                        context = context,
                                        title = apodEntry.title,
                                        url = apodEntry.url,
                                        hdUrl = apodEntry.hdurl
                                    )
                                )
                            },
                            onShareImageButtonClick = {
                                viewModel.onEvent(
                                    APODPageEvent.ShareImage(
                                        context = context,
                                        title = apodEntry.title,
                                        url = apodEntry.url,
                                        hdUrl = apodEntry.hdurl
                                    )
                                )
                            },
                            onShareVideoButtonClick = {
                                viewModel.onEvent(
                                    APODPageEvent.ShareVideo(
                                        context = context,
                                        title = apodEntry.title,
                                        url = apodEntry.url
                                    )
                                )
                            },
                            onTranslateButtonClick = {
                                viewModel.onEvent(APODPageEvent.Translate(apodIndex = it))
                            }
                        )
                    }

                    if (apodsStates.size == 1 && !isLoading && !endReached && pickedDate == null) {
                        item {
                            Spacer(
                                Modifier
                                    .fillMaxWidth()
                                    .height(dimensionResource(id = R.dimen.apod_screen_load_more_button_and_progress_top_space_size))
                            )

                            LoadMoreButton(onClick = {
                                viewModel.onEvent(APODPageEvent.LoadMoreAPODs)
                            })
                        }
                    }

                    if (isLoading && apodsStates.isNotEmpty()) {
                        item {
                            ProgressIndicator(
                                modifier = Modifier
                                    .padding(top = dimensionResource(id = R.dimen.apod_screen_load_more_button_and_progress_top_space_size))
                            )
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