package com.justcircleprod.randomspaceimages.ui.random.imageEntryItem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.domain.model.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.ui.common.bounceClick
import com.justcircleprod.randomspaceimages.ui.random.randomBaseViewModel.RandomBaseViewModel
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.shimmer.Shimmer
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

@Composable
fun NASALibraryImageEntryItem(
    nasaLibraryImageEntry: NASALibraryImageEntry,
    viewModel: RandomBaseViewModel,
    onImageEntryClick: (nasaLibraryImageEntry: NASALibraryImageEntry) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.main_rounded_corner_radius)))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    color = colorResource(id = R.color.ripple)
                ),
            ) {
                onImageEntryClick(nasaLibraryImageEntry)
            }
    ) {
        NASALibraryImageEntryImage(nasaLibraryImageEntry)
        ImageExtra(nasaLibraryImageEntry, viewModel)
    }
}

@Composable
fun NASALibraryImageEntryImage(
    nasaLibraryImageEntry: NASALibraryImageEntry
) {
    GlideImage(
        imageModel = { nasaLibraryImageEntry.imageHref },
        imageOptions = ImageOptions(
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            contentDescription = nasaLibraryImageEntry.title
        ),
        component = rememberImageComponent {
            +ShimmerPlugin(
                Shimmer.Flash(
                    baseColor = colorResource(id = R.color.background),
                    highlightColor = colorResource(id = R.color.shimmer_highlights),
                    duration = 1400,
                    dropOff = 0.65f,
                    tilt = 20f
                )
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.main_rounded_corner_radius)))
    )
}

@Composable
fun ImageExtra(nasaLibraryImageEntry: NASALibraryImageEntry, viewModel: RandomBaseViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.random_list_item_extra_padding_top))
    ) {
        if (nasaLibraryImageEntry.title != null) {
            ImageTitle(
                imageTitle = nasaLibraryImageEntry.title,
                modifier = Modifier.weight(1f)
            )
            FavouriteButton(nasaLibraryImageEntry = nasaLibraryImageEntry, viewModel = viewModel)
        }
    }
}

@Composable
fun ImageTitle(imageTitle: String, modifier: Modifier) {
    Text(
        text = imageTitle,
        color = colorResource(id = R.color.text),
        fontFamily = LatoFontFamily,
        maxLines = 1,
        fontSize = 14.sp,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.padding(start = dimensionResource(id = R.dimen.random_list_item_title_padding_start))
    )
}

@Composable
private fun FavouriteButton(
    nasaLibraryImageEntry: NASALibraryImageEntry,
    viewModel: RandomBaseViewModel
) {
    val isAdded by viewModel.isAddedToFavourites(nasaLibraryImageEntry.nasaId).observeAsState()

    Icon(
        painter = painterResource(id = if (isAdded == true) R.drawable.icon_favorite else R.drawable.icon_favorite_border),
        contentDescription = if (isAdded == true) {
            stringResource(id = R.string.remove_from_favourite)
        } else {
            stringResource(id = R.string.add_to_favourite)
        },
        tint = when (isAdded) {
            true -> colorResource(id = R.color.red)
            else -> colorResource(id = R.color.favourite_list_button_neutral)
        },
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.random_list_item_favourite_icon_size))
            .bounceClick {
                if (isAdded == true) {
                    viewModel.removeFromFavourites(nasaLibraryImageEntry)
                } else {
                    viewModel.addToFavourites(nasaLibraryImageEntry)
                }
            }
    )
}
