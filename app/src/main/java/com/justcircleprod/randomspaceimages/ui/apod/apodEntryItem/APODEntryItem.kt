package com.justcircleprod.randomspaceimages.ui.apod.apodEntryItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.models.APODEntry
import com.justcircleprod.randomspaceimages.data.remote.apod.APODConstants
import com.justcircleprod.randomspaceimages.ui.apod.apodBaseVIewModel.APODBaseViewModel
import com.justcircleprod.randomspaceimages.ui.common.DateHelper
import com.justcircleprod.randomspaceimages.ui.common.ImageActionMenu
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

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
                    APODImage(
                        apodEntry = apodEntry,
                        onImageClick = onImageClick,
                        isClickEnabled = isImageClickEnabled
                    )
                } else {
                    Video(videoUrl = apodEntry.url)
                }

                APODInfo(apodEntry = apodEntry)
            }

            if (isImageClickEnabled.value) {
                val isAddedToFavourites =
                    viewModel.isAddedToFavourites(apodEntry.date).observeAsState()

                ImageActionMenu(
                    scaffoldState = scaffoldState,
                    imageTitle = apodEntry.title,
                    imageHref = apodEntry.hdurl ?: apodEntry.url,
                    isAddedToFavourites = isAddedToFavourites,
                    onFavouriteButtonClick = {
                        if (isAddedToFavourites.value == true) {
                            viewModel.removeFromFavourites(apodEntry)
                        } else {
                            viewModel.addToFavourites(apodEntry)
                        }
                    },
                    modifier = Modifier
                        .constrainAs(actionButtons) {
                            top.linkTo(parent.top, margin = 9.dp)
                            end.linkTo(parent.end)
                        }
                        .padding(horizontal = dimensionResource(id = R.dimen.action_buttons_horizontal_space_size))
                )
            }
        }
    }
}

@Composable
fun APODImage(
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
                ProgressIndicator(
                    cardBackgroundColor = colorResource(id = R.color.apod_item_image_progress_card_background),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.apod_item_image_progress_height))
                        .background(colorResource(id = R.color.card_background))
                )
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
                            onImageClick(apodEntry.hdurl ?: apodEntry.url)
                        }
                    }
            )
        }
    }
}

@Composable
fun Video(videoUrl: String) {
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
                    uriHandler.openUri(videoUrl)
                }

        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_play),
                contentDescription = stringResource(id = R.string.watch_video_in_browser),
                tint = Color.White,
                modifier = Modifier.size(dimensionResource(id = R.dimen.apod_item_play_video_icon_button_size))
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