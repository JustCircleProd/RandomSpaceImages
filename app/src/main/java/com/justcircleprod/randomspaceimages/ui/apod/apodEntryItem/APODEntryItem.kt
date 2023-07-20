package com.justcircleprod.randomspaceimages.ui.apod.apodEntryItem

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.domain.model.APODEntry
import com.justcircleprod.randomspaceimages.ui.apod.apodBaseVIewModel.APODBaseViewModel
import com.justcircleprod.randomspaceimages.ui.apod.apodPagesSnackbarState.APODPagesSnackBarState
import com.justcircleprod.randomspaceimages.ui.common.ImageActionButtons
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator
import com.justcircleprod.randomspaceimages.ui.common.VideoActionButtons
import com.justcircleprod.randomspaceimages.ui.extensions.getActivity
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import java.util.Locale

@Composable
fun APODEntryItem(
    apodEntry: APODEntry,
    apodStates: APODStates,
    viewModel: APODBaseViewModel,
    context: Context,
    onImageClick: (imageUrl: String, imageUrlHd: String?) -> Unit,
    onOpenVideoError: () -> Unit,
    onFavouriteButtonClick: () -> Unit,
    onSaveImageButtonClick: () -> Unit,
    onShareImageButtonClick: () -> Unit,
    onShareVideoButtonClick: () -> Unit,
    onTranslateButtonClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.apod_item_rounded_corner_radius)),
        backgroundColor = colorResource(id = R.color.card_background),
        elevation = dimensionResource(id = R.dimen.card_elevation)
    ) {
        val isImageClickEnabled = remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.apod_item_card_content_bottom_space_size))
        ) {
            if (apodEntry.media_type == "image") {
                APODImage(
                    title = apodEntry.title,
                    url = apodEntry.url,
                    hdurl = apodEntry.hdurl,
                    onImageClick = onImageClick,
                    isClickEnabled = isImageClickEnabled
                )
            } else {
                Video(
                    videoUrl = apodEntry.url,
                    onOpenError = onOpenVideoError
                )
            }

            APODInfo(apodStates)

            ActionButtons(
                apodEntry = apodEntry,
                apodStates = apodStates,
                context = context,
                viewModel = viewModel,
                onFavouriteButtonClick = onFavouriteButtonClick,
                onSaveImageButtonClick = onSaveImageButtonClick,
                onShareImageButtonClick = onShareImageButtonClick,
                onShareVideoButtonClick = onShareVideoButtonClick,
                onTranslateButtonClick = onTranslateButtonClick
            )
        }
    }
}

@Composable
private fun APODImage(
    title: String,
    url: String,
    hdurl: String?,
    isClickEnabled: MutableState<Boolean>,
    onImageClick: (imageUrl: String, imageUrlHd: String?) -> Unit
) {
    Card(
        shape = RectangleShape,
        elevation = 0.dp
    ) {
        Box(Modifier.fillMaxWidth()) {
            if (!isClickEnabled.value) {
                ProgressIndicator(
                    cardBackgroundColor = colorResource(id = R.color.apod_item_image_progress_card_background),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.apod_item_image_progress_height))
                        .background(colorResource(id = R.color.card_background))
                )
            }

            GlideImage(
                imageModel = { url },
                imageOptions = ImageOptions(
                    contentDescription = title
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
                            onImageClick(url, hdurl)
                        }
                    }
            )
        }
    }
}

@Composable
private fun Video(videoUrl: String, onOpenError: () -> Unit) {
    val uriHandler = LocalUriHandler.current

    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.video_placeholder),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Black.copy(0.8f))
                .size(dimensionResource(id = R.dimen.apod_item_play_video_button_size))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        bounded = true,
                        color = colorResource(id = R.color.ripple)
                    ),
                ) {
                    try {
                        uriHandler.openUri(videoUrl)
                    } catch (e: Exception) {
                        onOpenError()
                    }
                }

        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_play),
                contentDescription = stringResource(id = R.string.open_video_in_another_app),
                tint = Color.White,
                modifier = Modifier.size(dimensionResource(id = R.dimen.apod_item_play_video_icon_button_size))
            )
        }
    }
}

@Composable
private fun APODInfo(apodStates: APODStates) {
    val title by apodStates.title.collectAsStateWithLifecycle()
    val date by apodStates.date.collectAsStateWithLifecycle()
    val explanation by apodStates.explanation.collectAsStateWithLifecycle()
    val copyright by apodStates.copyright.collectAsStateWithLifecycle()

    SelectionContainer {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.apod_item_card_content_horizontal_space_size))
                .padding(top = dimensionResource(id = R.dimen.apod_item_info_spacer_size))
        ) {
            Text(
                text = title,
                color = colorResource(id = R.color.text),
                fontFamily = LatoFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = date,
                color = colorResource(id = R.color.second_text),
                fontFamily = LatoFontFamily,
                fontSize = 15.sp,
                maxLines = 1
            )

            Spacer(Modifier.height(dimensionResource(id = R.dimen.apod_item_info_spacer_size)))

            Text(
                text = explanation,
                color = colorResource(id = R.color.text),
                fontFamily = LatoFontFamily,
                fontSize = 16.sp
            )

            if (copyright != null) {
                Spacer(Modifier.height(dimensionResource(id = R.dimen.apod_item_info_spacer_size)))

                Text(
                    text = copyright!!,
                    color = colorResource(id = R.color.second_text),
                    fontFamily = LatoFontFamily,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun ActionButtons(
    apodEntry: APODEntry,
    apodStates: APODStates,
    context: Context,
    viewModel: APODBaseViewModel,
    onFavouriteButtonClick: () -> Unit,
    onSaveImageButtonClick: () -> Unit,
    onShareImageButtonClick: () -> Unit,
    onShareVideoButtonClick: () -> Unit,
    onTranslateButtonClick: () -> Unit,
) {
    val isAddedToFavourites =
        viewModel.isAddedToFavouritesLiveData(apodEntry.date).observeAsState()

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
                onSaveImageButtonClick()
                return@rememberLauncherForActivityResult
            }

            context.getActivity()?.let {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    viewModel.snackBarState.value = APODPagesSnackBarState.GrantPermission
                } else {
                    viewModel.snackBarState.value = APODPagesSnackBarState.PermissionIsRequired
                }
            }
        }
    )

    val translating = apodStates.translating.collectAsStateWithLifecycle()
    val translated = apodStates.translated.collectAsStateWithLifecycle()

    val withTranslateButton = Locale.getDefault().language != "en"

    when (apodEntry.media_type) {
        "image" -> {
            ImageActionButtons(
                savingImageToGallery = viewModel.savingToGallery,
                onSaveButtonClick = {
                    if (hasWriteExternalStoragePermission.value) {
                        onSaveImageButtonClick()
                    } else {
                        permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                },
                sharingImage = viewModel.sharingImage,
                onShareButtonClick = onShareImageButtonClick,
                isAddedToFavourites = isAddedToFavourites,
                onFavouriteButtonClick = onFavouriteButtonClick,
                withTranslateButton = withTranslateButton,
                translating = translating.value,
                translated = translated.value,
                onTranslateButtonClick = onTranslateButtonClick,
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.apod_screen_action_button_top_space_size))
                    .padding(horizontal = dimensionResource(id = R.dimen.apod_screen_action_button_horizontal_space_size))
            )
        }

        "video" -> {
            VideoActionButtons(
                isAddedToFavourites = isAddedToFavourites,
                onFavouriteButtonClick = onFavouriteButtonClick,
                onShareButtonClick = onShareVideoButtonClick,
                withTranslateButton = withTranslateButton,
                translating = translating.value,
                translated = translated.value,
                onTranslateButtonClick = onTranslateButtonClick,
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.apod_screen_action_button_top_space_size))
                    .padding(horizontal = dimensionResource(id = R.dimen.apod_screen_action_button_horizontal_space_size))
            )
        }
    }
}