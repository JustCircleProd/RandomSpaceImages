package com.justcircleprod.randomspaceimages.ui.random.detail

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.domain.model.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.ui.common.BackButton
import com.justcircleprod.randomspaceimages.ui.common.ImageActionButtons
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator
import com.justcircleprod.randomspaceimages.ui.extensions.getActivity
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun DetailFragmentContent(
    viewModel: DetailViewModel,
    nasaLibraryImageEntry: NASALibraryImageEntry,
    onBackButtonClick: () -> Unit,
    onImageClick: (imageUrl: String) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val snackBarState by viewModel.snackBarState.collectAsStateWithLifecycle()

    LaunchedEffect(snackBarState) {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()

            when (snackBarState) {
                DetailFragmentSnackBarState.SavingImage -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.saving_the_image),
                        actionLabel = context.getString(R.string.dismiss),
                        duration = SnackbarDuration.Indefinite
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {
                            viewModel.snackBarState.value = null
                        }
                    }
                }

                DetailFragmentSnackBarState.FailedToSaveImage -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.failed_to_save),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                DetailFragmentSnackBarState.SuccessfullySavedImage -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.successfully_saved_to_gallery),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                DetailFragmentSnackBarState.GrantPermission -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.grant_permission),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                DetailFragmentSnackBarState.PermissionIsRequired -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.permission_is_required),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                DetailFragmentSnackBarState.PreparingToShare -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.preparing_to_share),
                        actionLabel = context.getString(R.string.dismiss),
                        duration = SnackbarDuration.Indefinite
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {
                            viewModel.snackBarState.value = null
                        }
                    }
                }

                DetailFragmentSnackBarState.FailedToShare -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        context.getString(R.string.failed_to_share),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                DetailFragmentSnackBarState.DownloadingLanguageModel -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.downloading_language_model),
                        actionLabel = context.getString(R.string.dismiss),
                        duration = SnackbarDuration.Indefinite
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {
                            viewModel.snackBarState.value = null
                        }
                    }
                }

                DetailFragmentSnackBarState.FailedToDownloadLanguageModel -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        context.getString(R.string.failed_to_download_language_model),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                DetailFragmentSnackBarState.Translating -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = context.getString(R.string.translating),
                        actionLabel = context.getString(R.string.dismiss),
                        duration = SnackbarDuration.Indefinite
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {}
                        SnackbarResult.ActionPerformed -> {
                            viewModel.snackBarState.value = null
                        }
                    }
                }

                DetailFragmentSnackBarState.FailedToTranslate -> {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        context.getString(R.string.failed_to_translate),
                        duration = SnackbarDuration.Short
                    )

                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            viewModel.snackBarState.value = null
                        }

                        SnackbarResult.ActionPerformed -> {}
                    }
                }

                null -> {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                }
            }
        }
    }

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
    ) { scaffoldPadding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(scaffoldPadding)
        ) {
            val (content, backButton) = createRefs()

            Column(modifier = Modifier
                .constrainAs(content) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .verticalScroll(rememberScrollState())
            ) {
                Card(
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_rounded_corner_radius)),
                    backgroundColor = colorResource(id = R.color.card_background),
                    elevation = dimensionResource(id = R.dimen.card_elevation),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(bottom = dimensionResource(id = R.dimen.detail_card_content_bottom_space_size))
                    ) {
                        Image(
                            viewModel = viewModel,
                            title = nasaLibraryImageEntry.title,
                            imageHref = nasaLibraryImageEntry.imageHref,
                            onImageClick = onImageClick
                        )

                        Info(viewModel = viewModel)

                        ActionButtons(
                            context = context,
                            viewModel = viewModel,
                            nasaLibraryImageEntryId = nasaLibraryImageEntry.nasaId
                        )
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.detail_screen_bottom_space)))
            }

            BackButton(
                onBackButtonClick,
                modifier = Modifier
                    .constrainAs(backButton) {
                        top.linkTo(parent.top, margin = 9.dp)
                        start.linkTo(parent.start, margin = 9.dp)
                    }
            )
        }
    }
}

@Composable
private fun Image(
    viewModel: DetailViewModel,
    title: String?,
    imageHref: String,
    onImageClick: (imageUrl: String) -> Unit
) {
    val imageLoaded = viewModel.imageLoaded.collectAsStateWithLifecycle()

    Box(Modifier.fillMaxWidth()) {
        if (!imageLoaded.value) {
            ProgressIndicator(
                cardBackgroundColor = colorResource(id = R.color.apod_item_image_progress_card_background),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.detail_image_progress_height))
                    .background(colorResource(id = R.color.card_background))
            )
        }

        GlideImage(
            imageModel = { imageHref },
            imageOptions = ImageOptions(
                contentDescription = title
            ),
            onImageStateChanged = {
                when (it) {
                    GlideImageState.None -> {}
                    GlideImageState.Loading -> {}
                    is GlideImageState.Success -> {
                        viewModel.imageLoaded.value = true
                    }

                    is GlideImageState.Failure -> {}
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        bounded = true,
                        color = colorResource(id = R.color.image_ripple)
                    ),
                ) {
                    onImageClick(imageHref)
                }
        )
    }
}

@Composable
private fun Info(
    viewModel: DetailViewModel
) {
    val title by viewModel.title.collectAsStateWithLifecycle()
    val date by viewModel.date.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val additionalInfo by viewModel.additionalInfo.collectAsStateWithLifecycle()

    SelectionContainer {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.detail_card_content_horizontal_space_size))
                .padding(top = dimensionResource(id = R.dimen.detail_info_space_size))
        ) {
            if (title.isNotEmpty()) {
                Title(title)
            }

            if (date.isNotEmpty()) {
                Date(date)
            }

            if (description.isNotEmpty()) {
                Spacer(Modifier.height(dimensionResource(id = R.dimen.detail_info_space_size)))
                Description(description)
            }

            if (additionalInfo.isNotEmpty()) {
                Spacer(Modifier.height(dimensionResource(id = R.dimen.detail_info_space_size)))
                AdditionalInfo(additionalInfo)
            }
        }
    }
}

@Composable
private fun Title(title: String) {
    Text(
        title,
        color = colorResource(id = R.color.text),
        fontFamily = LatoFontFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun Date(date: String) {
    Text(
        text = date,
        color = colorResource(id = R.color.second_text),
        fontFamily = LatoFontFamily,
        fontSize = 15.sp,
        maxLines = 1
    )
}

@Composable
private fun Description(description: String) {
    Text(
        text = description,
        color = colorResource(id = R.color.text),
        fontFamily = LatoFontFamily,
        fontSize = 16.sp
    )
}

@Composable
private fun AdditionalInfo(additionalInfo: String) {
    Text(
        text = additionalInfo,
        color = colorResource(id = R.color.second_text),
        fontFamily = LatoFontFamily,
        fontSize = 15.sp
    )
}

@Composable
private fun ActionButtons(
    context: Context,
    viewModel: DetailViewModel,
    nasaLibraryImageEntryId: String
) {
    val isAddedToFavourites =
        viewModel.isAddedToFavourites(nasaLibraryImageEntryId)
            .observeAsState()

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
                viewModel.onEvent(DetailFragmentEvent.SaveImage(context))
                return@rememberLauncherForActivityResult
            }

            context.getActivity()?.let {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    viewModel.snackBarState.value = DetailFragmentSnackBarState.GrantPermission
                } else {
                    viewModel.snackBarState.value = DetailFragmentSnackBarState.PermissionIsRequired
                }
            }

        }
    )

    val translating = viewModel.translating.collectAsStateWithLifecycle()
    val translated = viewModel.translated.collectAsStateWithLifecycle()

    val withTranslateButton = Locale.getDefault().language != "en"

    ImageActionButtons(
        sharingImage = viewModel.sharingImage,
        onShareButtonClick = {
            viewModel.onEvent(DetailFragmentEvent.ShareImage(context))
        },
        savingImageToGallery = viewModel.savingToGallery,
        onSaveButtonClick = {
            if (hasWriteExternalStoragePermission.value) {
                viewModel.onEvent(DetailFragmentEvent.SaveImage(context))
            } else {
                permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        },
        isAddedToFavourites = isAddedToFavourites,
        onFavouriteButtonClick = {
            viewModel.onEvent(DetailFragmentEvent.OnFavouriteButtonClick)
        },
        withTranslateButton = withTranslateButton,
        translating = translating.value,
        translated = translated.value,
        onTranslateButtonClick = {
            viewModel.onEvent(DetailFragmentEvent.Translate)
        },
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.detail_screen_action_button_top_space_size))
            .padding(horizontal = dimensionResource(id = R.dimen.detail_action_button_horizontal_space_size))
    )
}