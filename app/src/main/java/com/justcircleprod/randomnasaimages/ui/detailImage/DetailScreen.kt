package com.justcircleprod.randomnasaimages.ui.detailImage

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.justcircleprod.randomnasaimages.R
import com.justcircleprod.randomnasaimages.data.models.ImageEntry
import com.justcircleprod.randomnasaimages.data.remote.RemoteConstants
import com.justcircleprod.randomnasaimages.ui.common.BackButton
import com.justcircleprod.randomnasaimages.ui.theme.IconButtonShadow
import com.justcircleprod.randomnasaimages.ui.theme.Red
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DetailImageScreen(navController: NavHostController, imageEntry: ImageEntry?) {
    val viewModel: DetailViewModel = hiltViewModel()
    val scrollState = rememberScrollState()

    ConstraintLayout {
        val (contentColumn, actionButtons) = createRefs()

        Column(
            modifier = Modifier
                .constrainAs(contentColumn) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            ImageCard(imageEntry = imageEntry!!, scrollState = scrollState)

            if (imageEntry.title != null || imageEntry.description != null) {
                Spacer(Modifier.height(dimensionResource(id = R.dimen.elements_space_size)))
                DescriptionCard(imageEntry = imageEntry)
            }

            if (imageEntry.location != null ||
                imageEntry.secondaryCreator != null ||
                imageEntry.photographer != null
            ) {
                Spacer(Modifier.height(dimensionResource(id = R.dimen.elements_space_size)))
                AdditionalInfoCard(imageEntry = imageEntry)
            }

            Spacer(Modifier.height(dimensionResource(id = R.dimen.bottom_space)))
        }

        ActionButtons(
            navController = navController,
            viewModel = viewModel,
            imageEntry = imageEntry!!,
            modifier = Modifier
                .constrainAs(actionButtons) {
                    top.linkTo(parent.top, margin = 6.dp)
                }
        )
    }
}

@Composable
fun ImageCard(imageEntry: ImageEntry, scrollState: ScrollableState) {
    Card(
        shape = RoundedCornerShape(
            bottomStart = dimensionResource(id = R.dimen.main_rounded_corner_radius),
            bottomEnd = dimensionResource(id = R.dimen.main_rounded_corner_radius)
        ),
        backgroundColor = colorResource(id = R.color.card_background_color),
        elevation = dimensionResource(id = R.dimen.card_elevation)
    ) {
        Column(Modifier.fillMaxWidth()) {
            ZoomableImage(imageEntry = imageEntry, scrollState = scrollState)
            CenterAndDateInfo(imageEntry = imageEntry)
        }
    }
}

@Composable
fun ZoomableImage(imageEntry: ImageEntry, scrollState: ScrollableState) {
    val coroutineScope = rememberCoroutineScope()

    val localDensity = LocalDensity.current
    var width by remember { mutableStateOf(0f) }
    var height by remember { mutableStateOf(0f) }

    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(1f) }
    var offsetY by remember { mutableStateOf(1f) }

    GlideImage(
        imageModel = imageEntry.imageHref,
        contentDescription = imageEntry.title,
        alignment = Alignment.TopCenter,
        shimmerParams = ShimmerParams(
            baseColor = MaterialTheme.colors.background,
            highlightColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray,
            durationMillis = 1400,
            dropOff = 0.65f,
            tilt = 20f
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                width = with(localDensity) { coordinates.size.width.toDp().value }
                height = with(localDensity) { coordinates.size.height.toDp().value }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (scale > 1f) {
                            scale = 1f
                            offsetX = 1f
                            offsetY = 1f
                        } else {
                            scale = 3f
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        awaitFirstDown()
                        do {
                            val event = awaitPointerEvent()
                            scale = (scale * event.calculateZoom()).coerceIn(1f..3f)

                            if (scale > 1) {
                                coroutineScope.launch {
                                    scrollState.setScrolling(false)
                                }

                                val offset = event.calculatePan()

                                offsetX = (offsetX + offset.x)
                                    .coerceIn(-(width * scale)..width * scale)

                                offsetY = (offsetY + offset.y)
                                    .coerceIn(-(height * scale)..height * scale)


                                coroutineScope.launch {
                                    scrollState.setScrolling(true)
                                }
                            } else {
                                scale = 1f
                                offsetX = 1f
                                offsetY = 1f
                            }
                        } while (event.changes.any { it.pressed })
                    }
                }
            }
            .graphicsLayer {
                scaleX = scale.coerceIn(1f..3f)
                scaleY = scale.coerceIn(1f..3f)

                translationX = offsetX
                translationY = offsetY
            }
    )
}

private suspend fun ScrollableState.setScrolling(value: Boolean) {
    scroll(scrollPriority = MutatePriority.PreventUserInput) {
        when (value) {
            true -> Unit
            else -> awaitCancellation()
        }
    }
}

@Composable
fun CenterAndDateInfo(imageEntry: ImageEntry) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.center_and_date_info_padding_top))
            .padding(horizontal = dimensionResource(id = R.dimen.card_content_horizontal_space))
            .padding(bottom = dimensionResource(id = R.dimen.center_and_date_info_padding_bottom))
    ) {
        if (imageEntry.center != null) {
            Text(
                text = imageEntry.center,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )
        }

        val date = SimpleDateFormat(RemoteConstants.NASA_LIBRARY_DATE_CREATED_FORMAT, Locale.US)
            .parse(imageEntry.dateCreated)

        if (date != null) {
            val strDate =
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
            Text(text = strDate, fontSize = 15.sp, maxLines = 1)
        }
    }
}

@Composable
fun DescriptionCard(imageEntry: ImageEntry) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.main_rounded_corner_radius)),
        backgroundColor = colorResource(id = R.color.card_background_color),
        elevation = dimensionResource(id = R.dimen.card_elevation)
    ) {
        SelectionContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.card_content_horizontal_space))
                    .padding(vertical = dimensionResource(id = R.dimen.detail_card_content_vertical_space))
            ) {

                if (imageEntry.title != null) {
                    Text(text = imageEntry.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                if (imageEntry.description != null) {
                    Spacer(Modifier.height(dimensionResource(id = R.dimen.description_top_space_size)))
                    Text(text = imageEntry.description, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun AdditionalInfoCard(imageEntry: ImageEntry) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.main_rounded_corner_radius)),
        backgroundColor = colorResource(id = R.color.card_background_color),
        elevation = dimensionResource(id = R.dimen.card_elevation)
    ) {
        SelectionContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.card_content_horizontal_space))
                    .padding(vertical = dimensionResource(id = R.dimen.detail_card_content_vertical_space)),
            ) {
                Text(
                    stringResource(id = R.string.additional_information),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.elements_space_size)))

                if (imageEntry.location != null) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(stringResource(id = R.string.location))
                            }
                            append(" ")
                            append(imageEntry.location)
                        },
                        fontSize = 15.sp
                    )
                }

                if (imageEntry.secondaryCreator != null) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(stringResource(id = R.string.secondary_creator))
                            }
                            append(" ")
                            append(imageEntry.secondaryCreator)
                        },
                        fontSize = 15.sp
                    )
                }

                if (imageEntry.photographer != null) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(stringResource(id = R.string.photographer))
                            }
                            append(" ")
                            append(imageEntry.photographer)
                        },
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButtons(
    navController: NavController,
    viewModel: DetailViewModel,
    imageEntry: ImageEntry,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = dimensionResource(id = R.dimen.action_buttons_start_space_size))
            .padding(end = dimensionResource(id = R.dimen.action_buttons_end_space_size))
    ) {
        BackButton(navController = navController, modifier = Modifier)
        FavouriteButton(imageEntry = imageEntry, viewModel = viewModel)
    }
}

@Composable
fun FavouriteButton(
    imageEntry: ImageEntry,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val isAdded by viewModel.isAddedToFavourites(imageEntry.nasaId).observeAsState()

    IconButton(
        onClick = {
            if (isAdded == true) {
                viewModel.removeFromFavourites(imageEntry)
            } else {
                viewModel.addToFavourites(imageEntry)
            }
        },
        modifier = Modifier.size(dimensionResource(id = R.dimen.detail_image_favourite_button_size))
    ) {
        Icon(
            imageVector = if (isAdded == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.detail_image_favourite_button_icon_size))
                .offset(x = 1.dp, y = 1.dp)
                .alpha(0.4f)
                .blur(dimensionResource(id = R.dimen.action_button_icon_blur)),
            tint = IconButtonShadow
        )
        Icon(
            imageVector = if (isAdded == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = stringResource(id = R.string.favourite_button),
            tint = if (isAdded == true) Red else Color.White,
            modifier = Modifier.size(dimensionResource(id = R.dimen.detail_image_favourite_button_icon_size))
        )
    }
}