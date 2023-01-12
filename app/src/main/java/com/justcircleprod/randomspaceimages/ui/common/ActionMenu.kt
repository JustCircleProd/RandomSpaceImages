package com.justcircleprod.randomspaceimages.ui.common

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.justcircleprod.randomspaceimages.R
import kotlinx.coroutines.launch

@Composable
fun ImageActionMenu(
    scaffoldState: ScaffoldState,
    imageTitle: String?,
    imageHref: String,
    isAddedToFavourites: State<Boolean?>,
    onFavouriteButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        // SaveToGalleryButton
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        val savingToGalleryState = remember { mutableStateOf(false) }
        val onSaved = {
            coroutineScope.launch {
                savingToGalleryState.value = false

                scaffoldState.snackbarHostState.showSnackbar(
                    message = context.getString(R.string.successfully_saved_to_gallery),
                    duration = SnackbarDuration.Short
                )
            }
            Unit
        }

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
                    savingToGalleryState.value = true

                    saveToGallery(
                        context = context,
                        imageTitle = imageTitle,
                        imageHref = imageHref,
                        onSaved = onSaved
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
                savingToGalleryState = savingToGalleryState,
                onClick = {
                    if (hasWriteExternalStoragePermission.value) {
                        if (!savingToGalleryState.value) {
                            savingToGalleryState.value = true

                            saveToGallery(
                                context = context,
                                imageTitle = imageTitle,
                                imageHref = imageHref,
                                onSaved = onSaved
                            )
                        }
                    } else {
                        permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
            )

            FavouriteButton(
                isAddedToFavourites = isAddedToFavourites,
                onClick = { onFavouriteButtonClick() }
            )
        }
    }
}

@Composable
fun VideoActionMenu(
    isAddedToFavourites: State<Boolean?>,
    onFavouriteButtonClick: () -> Unit,
    modifier: Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(CircleShape)
            .background(Color.Black.copy(0.45f))
            .size(dimensionResource(id = R.dimen.action_icon_button_size))
    ) {
        FavouriteButton(
            isAddedToFavourites = isAddedToFavourites,
            onClick = { onFavouriteButtonClick() }
        )
    }
}

@Composable
fun ActionMenu(Buttons: @Composable () -> Unit) {
    var isMenuOpened by remember { mutableStateOf(false) }
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
                    .size(dimensionResource(id = R.dimen.action_menu_button_icon_size))
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
                .size(dimensionResource(id = R.dimen.action_menu_button_icon_size))
                .bounceClick {
                    onClick()
                }
        )
    }
}

@Composable
fun SaveToGalleryButton(
    savingToGalleryState: MutableState<Boolean>,
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
        if (savingToGalleryState.value) {
            CircularProgressIndicator(
                strokeWidth = dimensionResource(id = R.dimen.progress_indicator_stroke_width),
                color = colorResource(id = R.color.primary),
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.action_button_progress_indicator_size))
                    .align(Alignment.Center)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.icon_download),
                contentDescription = stringResource(
                    id = R.string.save_to_gallery
                ),
                tint = Color.White,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.action_menu_button_icon_size))
            )
        }
    }
}
