package com.justcircleprod.randomspaceimages.ui.detailImage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.models.ImageEntry
import com.justcircleprod.randomspaceimages.data.remote.RemoteConstants
import com.justcircleprod.randomspaceimages.ui.common.BackButton
import com.justcircleprod.randomspaceimages.ui.theme.IconButtonShadow
import com.justcircleprod.randomspaceimages.ui.theme.customColors
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DetailImageScreen(
    navController: NavHostController,
    imageEntry: ImageEntry?,
    viewModel: DetailViewModel
) {
    ConstraintLayout {
        val (contentColumn, actionButtons) = createRefs()

        LazyColumn(
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
                ImageCard(imageEntry = imageEntry!!)
            }

            item {
                if (imageEntry!!.title != null || imageEntry.description != null) {
                    Spacer(Modifier.height(dimensionResource(id = R.dimen.elements_space_size)))
                    DescriptionCard(imageEntry = imageEntry)
                }
            }

            item {

                if (imageEntry!!.location != null ||
                    imageEntry.secondaryCreator != null ||
                    imageEntry.photographer != null
                ) {
                    Spacer(Modifier.height(dimensionResource(id = R.dimen.elements_space_size)))
                    AdditionalInfoCard(imageEntry = imageEntry)
                }
            }

            item {
                Spacer(Modifier.height(dimensionResource(id = R.dimen.elements_space_size)))
            }
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
fun ImageCard(imageEntry: ImageEntry) {
    Card(
        shape = RoundedCornerShape(
            bottomStart = dimensionResource(id = R.dimen.main_rounded_corner_radius),
            bottomEnd = dimensionResource(id = R.dimen.main_rounded_corner_radius)
        ),
        backgroundColor = MaterialTheme.customColors.cardBackground,
        elevation = dimensionResource(id = R.dimen.card_elevation)
    ) {
        ConstraintLayout(Modifier.fillMaxWidth()) {
            val (imageView, enableImageInteractionButton, centerAndDateView) = createRefs()

            // Disabling interaction with the image is necessary for scrolling on the screen to work,
            // since scrolling does not work when interaction is enabled.
            var isImageInteractionEnabled by remember { mutableStateOf(false) }

            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(imageView) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                factory = { context ->
                    PhotoView(context).also {
                        Glide.with(context).load(imageEntry.imageHref).into(it)
                    }
                },
                update = {
                    it.isEnabled = isImageInteractionEnabled
                }
            )

            IconButton(
                onClick = { isImageInteractionEnabled = !isImageInteractionEnabled },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (!isImageInteractionEnabled) {
                            Color.Black.copy(0.45f)
                        } else {
                            MaterialTheme.colors.secondary.copy(0.45f)
                        }
                    )
                    .size(dimensionResource(id = R.dimen.interaction_icon_button_size))
                    .constrainAs(enableImageInteractionButton) {
                        bottom.linkTo(imageView.bottom, margin = 10.dp)
                        end.linkTo(imageView.end, margin = 10.dp)
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_interaction),
                    contentDescription = stringResource(id = R.string.interaction_content_description),
                    tint = Color.White,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.interaction_icon_size))
                )
            }

            CenterAndDateInfo(
                imageEntry = imageEntry,
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
fun CenterAndDateInfo(imageEntry: ImageEntry, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(top = dimensionResource(id = R.dimen.center_and_date_info_padding_top))
            .padding(horizontal = dimensionResource(id = R.dimen.card_content_horizontal_space))
            .padding(bottom = dimensionResource(id = R.dimen.center_and_date_info_padding_bottom))
            .fillMaxWidth()
    ) {
        if (imageEntry.center != null) {
            Text(
                text = imageEntry.center,
                color = MaterialTheme.customColors.text,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )
        }

        val date = SimpleDateFormat(RemoteConstants.NASA_LIBRARY_DATE_CREATED_FORMAT, Locale.US)
            .parse(imageEntry.dateCreated)

        if (date != null) {
            val strDate =
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
            Text(
                text = strDate,
                color = MaterialTheme.customColors.text,
                fontSize = 15.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun DescriptionCard(imageEntry: ImageEntry) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.main_rounded_corner_radius)),
        backgroundColor = MaterialTheme.customColors.cardBackground,
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
                    Text(
                        text = imageEntry.title,
                        color = MaterialTheme.customColors.text,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (imageEntry.description != null) {
                    Spacer(Modifier.height(dimensionResource(id = R.dimen.description_top_space_size)))
                    Text(
                        text = imageEntry.description,
                        color = MaterialTheme.customColors.text,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AdditionalInfoCard(imageEntry: ImageEntry) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.main_rounded_corner_radius)),
        backgroundColor = MaterialTheme.customColors.cardBackground,
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
                    color = MaterialTheme.customColors.text,
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
                        color = MaterialTheme.customColors.text,
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
                        color = MaterialTheme.customColors.text,
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
                        color = MaterialTheme.customColors.text,
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
        val onBackButtonClick = {
            navController.popBackStack()
            Unit
        }

        BackButton(onBackButtonClick)
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
            contentDescription = if (isAdded == true) {
                stringResource(id = R.string.remove_from_favourite_content_description)
            } else {
                stringResource(id = R.string.add_to_favourite_content_description)
            },
            tint = if (isAdded == true) MaterialTheme.colors.secondary else Color.White,
            modifier = Modifier.size(dimensionResource(id = R.dimen.detail_image_favourite_button_icon_size))
        )
    }
}