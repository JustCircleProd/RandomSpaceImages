package com.justcircleprod.randomspaceimages.ui.random.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.data.remote.nasaLibrary.NASALibraryConstants
import com.justcircleprod.randomspaceimages.ui.common.BackButton
import com.justcircleprod.randomspaceimages.ui.common.DateHelper
import com.justcircleprod.randomspaceimages.ui.common.ImageActionMenu
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

@Composable
fun DetailFragmentContent(
    viewModel: DetailViewModel,
    nasaLibraryImageEntry: NASALibraryImageEntry,
    onBackButtonClick: () -> Unit,
    onImageClick: (imageUrl: String) -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        backgroundColor = colorResource(id = R.color.background),
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    actionColor = colorResource(id = R.color.primary),
                    snackbarData = data,
                    elevation = dimensionResource(id = R.dimen.snackbar_elevation),
                    backgroundColor = colorResource(id = R.color.snackbar_background),
                    contentColor = colorResource(id = R.color.snackbar_text)
                )
            }
        }
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(it)
        ) {
            val (contentColumn, actionButtons) = createRefs()

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.elements_space_size)),
                contentPadding = PaddingValues(bottom = dimensionResource(id = R.dimen.detail_screen_bottom_space)),
                modifier = Modifier
                    .constrainAs(contentColumn) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .fillMaxSize()
            ) {
                item {
                    ImageCard(
                        nasaLibraryImageEntry = nasaLibraryImageEntry,
                        onImageClick = onImageClick
                    )
                }

                if (nasaLibraryImageEntry.title != null || nasaLibraryImageEntry.description != null) {
                    item {
                        DescriptionCard(nasaLibraryImageEntry = nasaLibraryImageEntry)
                    }
                }

                if (nasaLibraryImageEntry.location != null ||
                    nasaLibraryImageEntry.secondaryCreator != null ||
                    nasaLibraryImageEntry.photographer != null
                ) {
                    item {
                        AdditionalInfoCard(nasaLibraryImageEntry = nasaLibraryImageEntry)
                    }
                }
            }

            DetailActionButtons(
                viewModel = viewModel,
                nasaLibraryImageEntry = nasaLibraryImageEntry,
                scaffoldState = scaffoldState,
                onBackButtonClick = onBackButtonClick,
                modifier = Modifier
                    .constrainAs(actionButtons) {
                        top.linkTo(parent.top, margin = 9.dp)
                    }
            )
        }
    }
}


@Composable
fun ImageCard(
    nasaLibraryImageEntry: NASALibraryImageEntry,
    onImageClick: (imageUrl: String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(
            bottomStart = dimensionResource(id = R.dimen.main_rounded_corner_radius),
            bottomEnd = dimensionResource(id = R.dimen.main_rounded_corner_radius)
        ),
        backgroundColor = colorResource(id = R.color.card_background),
        elevation = dimensionResource(id = R.dimen.card_elevation)
    ) {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (imageView, centerAndDateView) = createRefs()

            var isClickEnabled by remember { mutableStateOf(false) }

            GlideImage(
                imageModel = { nasaLibraryImageEntry.imageHref },
                imageOptions = ImageOptions(
                    contentDescription = nasaLibraryImageEntry.title
                ),
                onImageStateChanged = {
                    when (it) {
                        GlideImageState.None -> {}
                        GlideImageState.Loading -> {}
                        is GlideImageState.Success -> {
                            isClickEnabled = true
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
                    .constrainAs(imageView) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            bounded = true,
                            color = colorResource(id = R.color.image_ripple)
                        ),
                    ) {
                        if (isClickEnabled) {
                            onImageClick(nasaLibraryImageEntry.imageHref)
                        }
                    }
            )

            CenterAndDateInfo(
                nasaLibraryImageEntry = nasaLibraryImageEntry,
                modifier = Modifier.constrainAs(centerAndDateView) {
                    top.linkTo(imageView.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@Composable
fun CenterAndDateInfo(nasaLibraryImageEntry: NASALibraryImageEntry, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(top = dimensionResource(id = R.dimen.center_and_date_info_padding_top))
            .padding(horizontal = dimensionResource(id = R.dimen.card_content_horizontal_space))
            .padding(bottom = dimensionResource(id = R.dimen.center_and_date_info_padding_bottom))
            .fillMaxWidth()
    ) {
        if (nasaLibraryImageEntry.center != null) {
            Text(
                text = nasaLibraryImageEntry.center,
                color = colorResource(id = R.color.text),
                fontFamily = LatoFontFamily,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )
        }

        val date = DateHelper.fromServerFormatToAppFormat(
            nasaLibraryImageEntry.dateCreated,
            NASALibraryConstants.DATE_CREATED_FORMAT
        )

        if (date != null) {
            Text(
                text = date,
                color = colorResource(id = R.color.text),
                fontFamily = LatoFontFamily,
                fontSize = 15.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun DescriptionCard(nasaLibraryImageEntry: NASALibraryImageEntry) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.main_rounded_corner_radius)),
        backgroundColor = colorResource(id = R.color.card_background),
        elevation = dimensionResource(id = R.dimen.card_elevation)
    ) {
        SelectionContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.card_content_horizontal_space))
                    .padding(vertical = dimensionResource(id = R.dimen.detail_card_content_vertical_space))
            ) {
                if (nasaLibraryImageEntry.title != null) {
                    Text(
                        text = nasaLibraryImageEntry.title,
                        color = colorResource(id = R.color.text),
                        fontFamily = LatoFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (nasaLibraryImageEntry.description != null) {
                    Spacer(Modifier.height(dimensionResource(id = R.dimen.description_top_space_size)))
                    Text(
                        text = nasaLibraryImageEntry.description,
                        color = colorResource(id = R.color.text),
                        fontFamily = LatoFontFamily,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AdditionalInfoCard(nasaLibraryImageEntry: NASALibraryImageEntry) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.main_rounded_corner_radius)),
        backgroundColor = colorResource(id = R.color.card_background),
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
                    text = stringResource(id = R.string.additional_information),
                    color = colorResource(id = R.color.text),
                    fontFamily = LatoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.elements_space_size)))

                if (nasaLibraryImageEntry.location != null) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(stringResource(id = R.string.location))
                            }
                            append(" ")
                            append(nasaLibraryImageEntry.location)
                        },
                        color = colorResource(id = R.color.text),
                        fontFamily = LatoFontFamily,
                        fontSize = 15.sp
                    )
                }

                if (nasaLibraryImageEntry.secondaryCreator != null) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(stringResource(id = R.string.secondary_creator))
                            }
                            append(" ")
                            append(nasaLibraryImageEntry.secondaryCreator)
                        },
                        color = colorResource(id = R.color.text),
                        fontFamily = LatoFontFamily,
                        fontSize = 15.sp
                    )
                }

                if (nasaLibraryImageEntry.photographer != null) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                append(stringResource(id = R.string.photographer))
                            }
                            append(" ")
                            append(nasaLibraryImageEntry.photographer)
                        },
                        color = colorResource(id = R.color.text),
                        fontFamily = LatoFontFamily,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DetailActionButtons(
    viewModel: DetailViewModel,
    nasaLibraryImageEntry: NASALibraryImageEntry,
    scaffoldState: ScaffoldState,
    onBackButtonClick: () -> Unit,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.action_buttons_horizontal_space_size))
    ) {
        BackButton(onBackButtonClick)

        val isAddedToFavourites =
            viewModel.isAddedToFavourites(nasaLibraryImageEntry.nasaId).observeAsState()

        ImageActionMenu(
            scaffoldState = scaffoldState,
            imageTitle = nasaLibraryImageEntry.title,
            imageHref = nasaLibraryImageEntry.imageHref,
            isAddedToFavourites = isAddedToFavourites,
            onFavouriteButtonClick = {
                if (isAddedToFavourites.value == true) {
                    viewModel.removeFromFavourites(nasaLibraryImageEntry)
                } else {
                    viewModel.addToFavourites(nasaLibraryImageEntry)
                }
            }
        )
    }
}