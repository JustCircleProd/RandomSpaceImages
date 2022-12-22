package com.justcircleprod.randomspaceimages.ui.apod.apodEntryItem

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.models.APODEntry
import com.justcircleprod.randomspaceimages.data.remote.apod.APODConstants
import com.justcircleprod.randomspaceimages.ui.apod.apodBaseVIewModel.APODBaseViewModel
import com.justcircleprod.randomspaceimages.ui.common.*
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import kotlinx.coroutines.launch

@Composable
fun APODEntryItem(
    apodEntry: APODEntry,
    viewModel: APODBaseViewModel,
    scaffoldState: ScaffoldState,
    onImageClick: (imageUrl: String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.apod_item_rounded_corner_radius)),
        backgroundColor = colorResource(id = R.color.card_background),
        elevation = dimensionResource(id = R.dimen.card_elevation)
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.apod_item_card_content_bottom_space_size))
        ) {
            val (contentColumn, actionButtons) = createRefs()

            val isImageClickEnabled = remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .constrainAs(contentColumn) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
            ) {

                if (apodEntry.media_type == "image") {
                    Image(
                        apodEntry = apodEntry,
                        onImageClick = onImageClick,
                        isClickEnabled = isImageClickEnabled
                    )
                }

                APODInfo(apodEntry = apodEntry)
            }

            if (isImageClickEnabled.value) {
                ActionButtons(
                    viewModel = viewModel,
                    apodEntry = apodEntry,
                    scaffoldState = scaffoldState,
                    modifier = Modifier
                        .constrainAs(actionButtons) {
                            top.linkTo(parent.top, margin = 9.dp)
                            end.linkTo(parent.end)
                        }
                )
            }
        }
    }
}

@Composable
fun Image(
    apodEntry: APODEntry,
    isClickEnabled: MutableState<Boolean>,
    onImageClick: (imageUrl: String) -> Unit
) {
    Card(
        shape = RectangleShape,
        elevation = 0.dp
    ) {
        Box(Modifier.fillMaxWidth()) {
            if (!isClickEnabled.value) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.apod_item_image_progress_height))
                        .background(colorResource(id = R.color.card_background))
                ) {
                    Card(
                        shape = CircleShape,
                        backgroundColor = colorResource(id = R.color.apod_item_image_progress_card_background),
                        elevation = dimensionResource(id = R.dimen.progress_card_elevation)
                    ) {
                        Box(modifier = Modifier.padding(dimensionResource(id = R.dimen.progress_card_padding))) {
                            CircularProgressIndicator(
                                strokeWidth = dimensionResource(id = R.dimen.progress_indicator_stroke_width),
                                color = colorResource(id = R.color.primary),
                                modifier = Modifier
                                    .size(dimensionResource(id = R.dimen.progress_indicator_size))
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
            }

            GlideImage(
                imageModel = { apodEntry.url },
                imageOptions = ImageOptions(
                    contentDescription = apodEntry.title
                ),
                onImageStateChanged = {
                    when (it) {
                        GlideImageState.None -> {}
                        GlideImageState.Loading -> {}
                        is GlideImageState.Success -> {
                            isClickEnabled.value = true
                        }
                        is GlideImageState.Failure -> {}
                    }
                },
                component = rememberImageComponent {
                    +ShimmerPlugin(
                        baseColor = colorResource(id = R.color.background),
                        highlightColor = colorResource(id = R.color.shimmer_highlights),
                        durationMillis = 1400,
                        dropOff = 0.65f,
                        tilt = 20f
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            bounded = true,
                            color = colorResource(id = R.color.image_ripple)
                        ),
                    ) {
                        if (isClickEnabled.value) {
                            onImageClick(apodEntry.hdurl)
                        }
                    }
            )
        }
    }
}

@Composable
fun APODInfo(apodEntry: APODEntry) {
    SelectionContainer {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.apod_item_card_content_horizontal_space_size))
                .padding(top = dimensionResource(id = R.dimen.apod_item_info_spacer_size))
        ) {
            Text(
                text = apodEntry.title,
                color = colorResource(id = R.color.text),
                fontFamily = LatoFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            val formattedDate =
                DateHelper.fromServerFormatToAppFormat(apodEntry.date, APODConstants.DATE_FORMAT)

            if (formattedDate != null) {
                Text(
                    text = formattedDate,
                    color = colorResource(id = R.color.second_text),
                    fontFamily = LatoFontFamily,
                    fontSize = 15.sp,
                    maxLines = 1
                )
            }

            Spacer(Modifier.height(dimensionResource(id = R.dimen.apod_item_info_spacer_size)))

            Text(
                text = apodEntry.explanation,
                color = colorResource(id = R.color.text),
                fontFamily = LatoFontFamily,
                fontSize = 16.sp
            )

            if (apodEntry.copyright != null) {
                Spacer(Modifier.height(dimensionResource(id = R.dimen.apod_item_info_spacer_size)))

                Text(
                    text = buildAnnotatedString {
                        append(stringResource(id = R.string.copyright))
                        append(" ")
                        append(apodEntry.copyright)
                    },
                    color = colorResource(id = R.color.second_text),
                    fontFamily = LatoFontFamily,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun ActionButtons(
    viewModel: APODBaseViewModel,
    apodEntry: APODEntry,
    scaffoldState: ScaffoldState,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.action_buttons_horizontal_space_size))
    ) {
        // FavouriteButton
        val isAddedToFavourites =
            viewModel.isAddedToFavourites(apodEntry.date).observeAsState()

        // SaveToGalleryButton
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        val savedToGallery = rememberSaveable { mutableStateOf(false) }

        val hasWriteExternalStoragePermission = remember {
            mutableStateOf(
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                } else {
                    true
                }
            )
        }
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                hasWriteExternalStoragePermission.value = isGranted

                if (isGranted) {
                    saveToGallery(
                        context = context,
                        imageTitle = apodEntry.title,
                        imageHref = apodEntry.hdurl,
                        savedToGallery = savedToGallery
                    )
                } else {
                    context.getActivity()?.let {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                it,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        ) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.grant_permission),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        } else {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.permission_is_required),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                }
            }
        )

        ActionMenu {
            SaveToGalleryButton(
                savedToGallery = savedToGallery,
                onClick = {
                    if (hasWriteExternalStoragePermission.value) {
                        if (savedToGallery.value) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.already_saved_to_gallery),
                                    duration = SnackbarDuration.Short
                                )
                            }
                            return@SaveToGalleryButton
                        }

                        saveToGallery(
                            context = context,
                            imageTitle = apodEntry.title,
                            imageHref = apodEntry.hdurl,
                            savedToGallery = savedToGallery
                        )
                    } else {
                        permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
            )

            FavouriteButton(
                isAddedToFavourites = isAddedToFavourites,
                onClick = {
                    if (isAddedToFavourites.value == true) {
                        viewModel.removeFromFavourites(apodEntry)
                    } else {
                        viewModel.addToFavourites(apodEntry)
                    }
                }
            )
        }
    }
}