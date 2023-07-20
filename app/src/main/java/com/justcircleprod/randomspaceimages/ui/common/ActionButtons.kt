package com.justcircleprod.randomspaceimages.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.justcircleprod.randomspaceimages.R
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun ImageActionButtons(
    isAddedToFavourites: State<Boolean?>,
    onFavouriteButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    savingImageToGallery: MutableStateFlow<Boolean>,
    onSaveButtonClick: () -> Unit,
    sharingImage: MutableStateFlow<Boolean>,
    onShareButtonClick: () -> Unit,
    withTranslateButton: Boolean = true,
    translating: Boolean,
    translated: Boolean,
    onTranslateButtonClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FavouriteButton(
                isAddedToFavourites = isAddedToFavourites,
                onClick = onFavouriteButtonClick
            )

            Spacer(Modifier.width(dimensionResource(id = R.dimen.action_buttons_space_between)))

            SaveToGalleryButton(
                savingToGallery = savingImageToGallery.collectAsStateWithLifecycle(),
                onClick = onSaveButtonClick
            )

            Spacer(Modifier.width(dimensionResource(id = R.dimen.action_buttons_space_between)))

            ShareButton(
                loadingState = sharingImage.collectAsStateWithLifecycle(),
                onClick = onShareButtonClick
            )
        }

        if (withTranslateButton) {
            Spacer(Modifier.width(dimensionResource(id = R.dimen.action_buttons_space_between)))

            TranslateButton(
                translating = translating,
                translated = translated,
                onClick = onTranslateButtonClick
            )
        }
    }
}

@Composable
fun VideoActionButtons(
    isAddedToFavourites: State<Boolean?>,
    onFavouriteButtonClick: () -> Unit,
    onShareButtonClick: () -> Unit,
    withTranslateButton: Boolean,
    translating: Boolean,
    translated: Boolean,
    onTranslateButtonClick: () -> Unit,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FavouriteButton(
                isAddedToFavourites = isAddedToFavourites,
                onClick = onFavouriteButtonClick
            )

            Spacer(Modifier.width(dimensionResource(id = R.dimen.action_buttons_space_between)))

            ShareButton(onClick = onShareButtonClick)
        }

        if (withTranslateButton) {
            Spacer(Modifier.width(dimensionResource(id = R.dimen.action_buttons_space_between)))

            TranslateButton(
                translating = translating,
                translated = translated,
                onClick = onTranslateButtonClick
            )
        }
    }
}

@Composable
private fun ActionButton(
    onClick: () -> Unit,
    content: @Composable (contentModifier: Modifier) -> Unit
) {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0.85f else 1f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .background(colorResource(id = R.color.action_button_background))
            .width(dimensionResource(id = R.dimen.action_button_width))
            .height(dimensionResource(id = R.dimen.action_button_height))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    color = colorResource(id = R.color.ripple)
                ),
                onClick = onClick
            )
            .pointerInput(buttonState) {
                awaitPointerEventScope {
                    buttonState = if (buttonState == ButtonState.Pressed) {
                        waitForUpOrCancellation()
                        ButtonState.Idle
                    } else {
                        awaitFirstDown(false)
                        ButtonState.Pressed
                    }
                }
            }
    ) {
        content(contentModifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            })
    }
}


@Composable
private fun FavouriteButton(
    isAddedToFavourites: State<Boolean?>,
    onClick: () -> Unit
) {
    ActionButton(
        onClick = onClick
    ) { modifier ->
        Icon(
            painter = painterResource(id = if (isAddedToFavourites.value == true) R.drawable.icon_favorite else R.drawable.icon_favorite_border),
            contentDescription = if (isAddedToFavourites.value == true) {
                stringResource(id = R.string.remove_from_favourite)
            } else {
                stringResource(id = R.string.add_to_favourite)
            },
            tint = if (isAddedToFavourites.value == true) colorResource(id = R.color.red) else colorResource(
                id = R.color.action_button_icon_tint
            ),
            modifier = modifier.size(dimensionResource(id = R.dimen.action_button_icon_size))
        )
    }
}

@Composable
private fun SaveToGalleryButton(
    savingToGallery: State<Boolean>,
    onClick: () -> Unit
) {
    ActionButton(
        onClick = onClick
    ) { modifier ->
        if (savingToGallery.value) {
            CircularProgressIndicator(
                strokeWidth = dimensionResource(id = R.dimen.progress_indicator_stroke_width),
                color = colorResource(id = R.color.primary),
                modifier = modifier
                    .size(dimensionResource(id = R.dimen.action_button_progress_indicator_size))
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.icon_download),
                contentDescription = stringResource(
                    id = R.string.save_to_gallery
                ),
                tint = colorResource(id = R.color.action_button_icon_tint),
                modifier = modifier
                    .size(dimensionResource(id = R.dimen.action_button_icon_size))
            )
        }
    }
}

@Composable
private fun ShareButton(onClick: () -> Unit, loadingState: State<Boolean>? = null) {
    ActionButton(
        onClick = onClick
    ) { modifier ->
        if (loadingState?.value == true) {
            CircularProgressIndicator(
                strokeWidth = dimensionResource(id = R.dimen.progress_indicator_stroke_width),
                color = colorResource(id = R.color.primary),
                modifier = modifier
                    .size(dimensionResource(id = R.dimen.action_button_progress_indicator_size))
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.icon_share),
                contentDescription = null,
                tint = colorResource(id = R.color.action_button_icon_tint),
                modifier = modifier
                    .size(dimensionResource(id = R.dimen.action_button_icon_size))
            )
        }
    }
}

@Composable
private fun TranslateButton(
    translating: Boolean,
    translated: Boolean,
    onClick: () -> Unit
) {
    ActionButton(
        onClick = onClick
    ) { modifier ->
        if (translating) {
            CircularProgressIndicator(
                strokeWidth = dimensionResource(id = R.dimen.progress_indicator_stroke_width),
                color = colorResource(id = R.color.primary),
                modifier = modifier
                    .size(dimensionResource(id = R.dimen.action_button_progress_indicator_size))
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.icon_translate),
                contentDescription = stringResource(id = R.string.translate),
                tint = if (translated) colorResource(id = R.color.primary) else colorResource(id = R.color.action_button_icon_tint),
                modifier = modifier.size(dimensionResource(id = R.dimen.action_button_icon_size))
            )
        }
    }
}
