package com.justcircleprod.randomspaceimages.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.justcircleprod.randomspaceimages.R

@Composable
fun ActionMenu(Buttons: @Composable () -> Unit) {
    var isMenuOpened by rememberSaveable {
        mutableStateOf(false)
    }
    val angle by animateFloatAsState(if (isMenuOpened) 90f else 0f)

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.Black.copy(0.45f))
    ) {
        AnimatedVisibility(
            visible = isMenuOpened
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Buttons()
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.action_icon_button_size))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        bounded = true,
                        color = colorResource(id = R.color.ripple)
                    )
                ) {
                    isMenuOpened = !isMenuOpened
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_more),
                contentDescription = stringResource(id = if (isMenuOpened) R.string.close_action_menu else R.string.open_action_menu),
                tint = Color.White,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.detail_image_action_button_icon_size))
                    .rotate(angle)
            )
        }
    }
}


@Composable
fun FavouriteButton(
    isAddedToFavourites: State<Boolean?>,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.action_icon_button_size))
    ) {
        Icon(
            painter = painterResource(id = if (isAddedToFavourites.value == true) R.drawable.icon_favorite else R.drawable.icon_favorite_border),
            contentDescription = if (isAddedToFavourites.value == true) {
                stringResource(id = R.string.remove_from_favourite)
            } else {
                stringResource(id = R.string.add_to_favourite)
            },
            tint = if (isAddedToFavourites.value == true) colorResource(id = R.color.red) else Color.White,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.detail_image_action_button_icon_size))
                .bounceClick {
                    onClick()
                }
        )
    }
}

@Composable
fun SaveToGalleryButton(
    savedToGallery: MutableState<Boolean>,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.action_icon_button_size))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    color = colorResource(id = R.color.ripple)
                ),
            ) {
                onClick()
            }
    ) {
        Icon(
            painter = painterResource(id = if (savedToGallery.value) R.drawable.icon_download_done else R.drawable.icon_download),
            contentDescription = stringResource(
                id = if (savedToGallery.value) {
                    R.string.saved_to_gallery
                } else {
                    R.string.save_to_gallery
                }
            ),
            tint = if (savedToGallery.value) colorResource(id = R.color.saved_to_gallery) else Color.White,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.detail_image_action_button_icon_size))
        )
    }
}
