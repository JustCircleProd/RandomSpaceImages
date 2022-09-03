package com.justcircleprod.randomnasaimages.ui.common

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.justcircleprod.randomnasaimages.R
import com.justcircleprod.randomnasaimages.data.models.ImageEntry
import com.justcircleprod.randomnasaimages.ui.baseViewModel.BaseViewModel
import com.justcircleprod.randomnasaimages.ui.screen.Screen
import com.justcircleprod.randomnasaimages.ui.theme.Red
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ProgressIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            shape = CircleShape,
            backgroundColor = colorResource(id = R.color.card_background_color),
            elevation = dimensionResource(id = R.dimen.progress_card_elevation)
        ) {
            Box(modifier = Modifier.padding(dimensionResource(id = R.dimen.progress_card_padding))) {
                CircularProgressIndicator(
                    strokeWidth = dimensionResource(id = R.dimen.progress_indicator_stroke_width),
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.progress_indicator_size))
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun ImageItem(
    imageEntry: ImageEntry,
    navController: NavHostController,
    viewModel: BaseViewModel
) {
    val isClickEnabled = remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .padding(horizontal = dimensionResource(id = R.dimen.elements_space_size))
        .padding(top = dimensionResource(id = R.dimen.elements_space_size))
        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.main_rounded_corner_radius)))
        .clickable {
            if (isClickEnabled.value) {
                navigateToDetailScreen(navController, imageEntry)
            }
        }
    ) {
        Image(imageEntry, isClickEnabled)
        ImageExtra(imageEntry, viewModel)
    }
}

private fun navigateToDetailScreen(navController: NavHostController, imageEntry: ImageEntry) {
    val json = Uri.encode(Gson().toJson(imageEntry))
    navController.navigate(Screen.Detail(json).route)
}

@Composable
fun Image(
    imageEntry: ImageEntry,
    isClickEnabled: MutableState<Boolean>
) {
    GlideImage(
        imageModel = imageEntry.imageHref,
        requestListener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                isClickEnabled.value = false
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                isClickEnabled.value = true
                return true
            }
        },
        contentDescription = imageEntry.title,
        contentScale = ContentScale.Crop,
        shimmerParams = ShimmerParams(
            baseColor = MaterialTheme.colors.background,
            highlightColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray,
            durationMillis = 1400,
            dropOff = 0.65f,
            tilt = 20f
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.main_rounded_corner_radius)))
            .aspectRatio(1f)
    )
}

@Composable
fun ImageExtra(imageEntry: ImageEntry, viewModel: BaseViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.image_list_item_title_padding_top))
            .padding(start = dimensionResource(id = R.dimen.elements_space_size))
            .padding(bottom = dimensionResource(id = R.dimen.elements_space_size))
    ) {
        if (imageEntry.title != null) {
            ImageTitle(
                imageTitle = imageEntry.title,
                modifier = Modifier.weight(1f)
            )
            FavouriteButton(imageEntry = imageEntry, viewModel = viewModel)
        }
    }
}

@Composable
fun ImageTitle(imageTitle: String, modifier: Modifier) {
    Text(
        text = imageTitle,
        maxLines = 1,
        fontSize = 14.sp,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

@Composable
fun FavouriteButton(
    imageEntry: ImageEntry,
    viewModel: BaseViewModel
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
        modifier = Modifier.size(dimensionResource(id = R.dimen.image_list_favourite_button_size))
    ) {
        Icon(
            imageVector = if (isAdded == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = stringResource(id = R.string.favourite_button),
            tint = when {
                isAdded == true -> Red
                isSystemInDarkTheme() -> Color.LightGray
                else -> Color.DarkGray
            },
            modifier = Modifier.size(dimensionResource(id = R.dimen.image_list_favourite_icon_size))
        )
    }
}