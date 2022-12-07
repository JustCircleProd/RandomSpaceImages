package com.justcircleprod.randomspaceimages.ui.random.detail

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.data.remote.nasaLibrary.NASALibraryConstants
import com.justcircleprod.randomspaceimages.ui.common.BackButton
import com.justcircleprod.randomspaceimages.ui.common.GalleryHelper
import com.justcircleprod.randomspaceimages.ui.common.bounceClick
import com.justcircleprod.randomspaceimages.ui.common.getActivity
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DetailFragmentContent(
    viewModel: DetailViewModel,
    nasaLibraryImageEntry: NASALibraryImageEntry,
    onBackButtonClick: () -> Unit
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
                    ImageCard(nasaLibraryImageEntry = nasaLibraryImageEntry)
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

            ActionButtons(
                viewModel = viewModel,
                nasaLibraryImageEntry = nasaLibraryImageEntry,
                scaffoldState = scaffoldState,
                onBackButtonClick = onBackButtonClick,
                modifier = Modifier
                    .constrainAs(actionButtons) {
                        top.linkTo(parent.top, margin = 6.dp)
                    }
            )
        }
    }
}


@Composable
fun ImageCard(nasaLibraryImageEntry: NASALibraryImageEntry) {
    Card(
        shape = RoundedCornerShape(
            bottomStart = dimensionResource(id = R.dimen.main_rounded_corner_radius),
            bottomEnd = dimensionResource(id = R.dimen.main_rounded_corner_radius)
        ),
        backgroundColor = colorResource(id = R.color.card_background),
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
                    PhotoView(context).also { photoView ->
                        Glide.with(context).load(nasaLibraryImageEntry.imageHref).into(photoView)
                    }
                },
                update = {
                    it.isEnabled = isImageInteractionEnabled
                }
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (!isImageInteractionEnabled) {
                            Color.Black.copy(0.45f)
                        } else {
                            colorResource(id = R.color.red).copy(0.45f)
                        }
                    )
                    .size(dimensionResource(id = R.dimen.interaction_icon_button_size))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            bounded = true,
                            color = colorResource(id = R.color.ripple)
                        ),
                    ) {
                        isImageInteractionEnabled = !isImageInteractionEnabled
                    }
                    .constrainAs(enableImageInteractionButton) {
                        bottom.linkTo(imageView.bottom, margin = 10.dp)
                        end.linkTo(imageView.end, margin = 10.dp)
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_interaction),
                    contentDescription = stringResource(
                        id = if (isImageInteractionEnabled)
                            R.string.disable_image_interaction
                        else
                            R.string.enable_image_interaction
                    ),
                    tint = Color.White,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.interaction_icon_size))
                )
            }

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

        val date = SimpleDateFormat(NASALibraryConstants.DATE_CREATED_FORMAT, Locale.US)
            .parse(nasaLibraryImageEntry.dateCreated)

        if (date != null) {
            val strDate =
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
            Text(
                text = strDate,
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
fun ActionButtons(
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
            .padding(start = dimensionResource(id = R.dimen.action_buttons_start_space_size))
            .padding(end = dimensionResource(id = R.dimen.action_buttons_end_space_size))
    ) {
        BackButton(onBackButtonClick)
        ActionMenu(
            nasaLibraryImageEntry = nasaLibraryImageEntry,
            viewModel = viewModel,
            scaffoldState = scaffoldState
        )
    }
}

@Composable
fun ActionMenu(
    nasaLibraryImageEntry: NASALibraryImageEntry,
    viewModel: DetailViewModel,
    scaffoldState: ScaffoldState
) {
    // all necessary states for action menu buttons
    var isMenuOpened by rememberSaveable {
        mutableStateOf(false)
    }
    val angle by animateFloatAsState(if (isMenuOpened) 90f else 0f)

    // FavouriteButton
    val isAddedToFavourites =
        viewModel.isAddedToFavourites(nasaLibraryImageEntry.nasaId).observeAsState()

    // SaveToGalleryButton
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val savedToGallery = rememberSaveable { mutableStateOf(false) }

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
                saveToGallery(
                    context = context,
                    imageTitle = nasaLibraryImageEntry.title,
                    imageHref = nasaLibraryImageEntry.imageHref,
                    savedToGallery = savedToGallery
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
                SaveToGalleryButton(
                    imageTitle = nasaLibraryImageEntry.title,
                    imageHref = nasaLibraryImageEntry.imageHref,
                    scaffoldState = scaffoldState,
                    context = context,
                    coroutineScope = coroutineScope,
                    hasWriteExternalStoragePermission = hasWriteExternalStoragePermission,
                    permissionLauncher = permissionLauncher,
                    savedToGallery = savedToGallery
                )
                FavouriteButton(
                    nasaLibraryImageEntry = nasaLibraryImageEntry,
                    viewModel = viewModel,
                    isAddedToFavourites = isAddedToFavourites
                )
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
    nasaLibraryImageEntry: NASALibraryImageEntry,
    viewModel: DetailViewModel,
    isAddedToFavourites: State<Boolean?>
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
                    if (isAddedToFavourites.value == true) {
                        viewModel.removeFromFavourites(nasaLibraryImageEntry)
                    } else {
                        viewModel.addToFavourites(nasaLibraryImageEntry)
                    }
                }
        )
    }
}

@Composable
fun SaveToGalleryButton(
    imageTitle: String?,
    imageHref: String,
    scaffoldState: ScaffoldState,
    context: Context,
    coroutineScope: CoroutineScope,
    hasWriteExternalStoragePermission: MutableState<Boolean>,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    savedToGallery: MutableState<Boolean>
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
                if (hasWriteExternalStoragePermission.value) {
                    if (savedToGallery.value) {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = context.getString(R.string.already_saved_to_gallery),
                                duration = SnackbarDuration.Short
                            )
                        }
                        return@clickable
                    }

                    saveToGallery(
                        context = context,
                        imageTitle = imageTitle,
                        imageHref = imageHref,
                        savedToGallery = savedToGallery
                    )
                } else {
                    permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
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

fun saveToGallery(
    context: Context,
    imageTitle: String?,
    imageHref: String,
    savedToGallery: MutableState<Boolean>
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
                resource?.let { imageDrawable ->
                    val savedSuccessfully =
                        GalleryHelper.saveToExternalStorage(
                            context,
                            GalleryHelper.getDisplayName(imageTitle),
                            imageDrawable.toBitmap()
                        )
                    if (savedSuccessfully) {
                        savedToGallery.value = true
                    }
                }
                return true
            }
        })
        .submit()
}