package com.justcircleprod.randomspaceimages.ui.common

import android.Manifest
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
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
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.dataStore.DataStoreConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@Composable
fun ImageActionMenu(
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope, // for showing snackbars
    viewModelScope: CoroutineScope, // for working with files
    title: String?,
    href: String,
    isAddedToFavourites: State<Boolean?>,
    onFavouriteButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    qualityOfSavingAndSharingImages: Flow<String?>? = null,
    hrefHd: String? = null
) {
    Box(
        modifier = modifier
    ) {
        // SaveToGalleryButton
        val context = LocalContext.current

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

                    viewModelScope.launch {
                        val imageHref =
                            if (hrefHd != null && qualityOfSavingAndSharingImages?.first() == DataStoreConstants.HIGH_QUALITY) {
                                hrefHd
                            } else {
                                href
                            }

                        saveToGallery(
                            context = context,
                            viewModelScope = viewModelScope,
                            imageTitle = title,
                            imageHref = imageHref,
                            onSaved = onSaved
                        )
                    }
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

        // ShareButton
        val shareLoadingState = remember { mutableStateOf(false) }

        ActionMenu {
            ShareButton(
                onClick = {
                    if (!shareLoadingState.value) {
                        shareLoadingState.value = true

                        viewModelScope.launch {
                            val imageHref =
                                if (hrefHd != null && qualityOfSavingAndSharingImages?.first() == DataStoreConstants.HIGH_QUALITY) {
                                    hrefHd
                                } else {
                                    href
                                }

                            shareImage(
                                context = context,
                                viewModelScope = viewModelScope,
                                title = title,
                                href = imageHref,
                                loadingState = shareLoadingState,
                                onShareFailed = {
                                    coroutineScope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            context.getString(R.string.failed_to_share),
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            )
                        }
                    }
                },
                loadingState = shareLoadingState.value,
            )

            SaveToGalleryButton(
                savingToGallery = { savingToGalleryState.value },
                onClick = {
                    if (hasWriteExternalStoragePermission.value) {
                        if (!savingToGalleryState.value) {
                            savingToGalleryState.value = true

                            viewModelScope.launch {
                                val imageHref =
                                    if (hrefHd != null && qualityOfSavingAndSharingImages?.first() == DataStoreConstants.HIGH_QUALITY) {
                                        hrefHd
                                    } else {
                                        href
                                    }

                                saveToGallery(
                                    context = context,
                                    viewModelScope = viewModelScope,
                                    imageTitle = title,
                                    imageHref = imageHref,
                                    onSaved = onSaved
                                )
                            }
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

private fun saveToGallery(
    context: Context,
    viewModelScope: CoroutineScope,
    imageTitle: String?,
    imageHref: String,
    onSaved: () -> Unit
) {
    Glide
        .with(context)
        .load(imageHref)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return true
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                viewModelScope.launch {
                    resource?.let { imageDrawable ->
                        val savedSuccessfully =
                            saveToExternalStorage(
                                context,
                                getDisplayName(imageTitle),
                                imageDrawable.toBitmap()
                            )
                        if (savedSuccessfully) {
                            onSaved()
                        }
                    }
                }
                return true
            }
        })
        .submit()
}

private fun shareImage(
    context: Context,
    viewModelScope: CoroutineScope,
    title: String?,
    href: String,
    loadingState: MutableState<Boolean>,
    onShareFailed: () -> Unit
) {
    Glide.with(context).load(href)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                loadingState.value = false
                onShareFailed()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                viewModelScope.launch {
                    resource?.let {
                        if (!CachedImageHelper.save(context, it.toBitmap())) {
                            loadingState.value = false
                            onShareFailed()
                            return@launch
                        }
                    }

                    val uri = CachedImageHelper.getUri(context) ?: return@launch

                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            StringBuilder().apply {
                                if (title != null) {
                                    append(title, "\n\n")
                                }
                                append(context.getString(R.string.watch_more_in_the_app))
                            }.toString()
                        )

                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        clipData = ClipData(
                            "Image",
                            arrayOf("image/png"),
                            ClipData.Item(uri)
                        )

                        putExtra(
                            Intent.EXTRA_STREAM,
                            uri
                        )
                        type = "image/png"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)

                    loadingState.value = false
                }

                return false
            }
        }).submit()
}

@Composable
fun VideoActionMenu(
    title: String,
    href: String,
    isAddedToFavourites: State<Boolean?>,
    onFavouriteButtonClick: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        val context = LocalContext.current

        ActionMenu {
            ShareButton(
                onClick = {
                    shareVideo(context, title, href)
                }
            )

            FavouriteButton(
                isAddedToFavourites = isAddedToFavourites,
                onClick = { onFavouriteButtonClick() }
            )
        }
    }
}

private fun shareVideo(context: Context, title: String, href: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            StringBuilder().apply {
                append(
                    title,
                    '\n',
                    href,
                    "\n\n",
                    context.getString(R.string.watch_more_in_the_app)
                )
            }.toString()
        )

        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
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
    savingToGallery: () -> Boolean,
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
        if (savingToGallery()) {
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

@Composable
fun ShareButton(onClick: () -> Unit, loadingState: Boolean? = null) {
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
        if (loadingState == true) {
            CircularProgressIndicator(
                strokeWidth = dimensionResource(id = R.dimen.progress_indicator_stroke_width),
                color = colorResource(id = R.color.primary),
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.action_button_progress_indicator_size))
                    .align(Alignment.Center)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.icon_share),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.action_menu_button_icon_size))
            )
        }
    }
}
