package com.justcircleprod.randomnasaimages.ui.common

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.navigation.NavHostController
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.justcircleprod.randomnasaimages.R
import com.justcircleprod.randomnasaimages.data.models.ImageEntry
import com.justcircleprod.randomnasaimages.databinding.NativeAdBinding
import com.justcircleprod.randomnasaimages.ui.baseViewModel.BaseViewModel
import com.justcircleprod.randomnasaimages.ui.screen.Screen
import com.justcircleprod.randomnasaimages.ui.theme.Red
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.nativeads.*

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
            painter = painterResource(id = if (isAdded == true) R.drawable.icon_favorite else R.drawable.icon_favorite_border),
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

@Composable
fun Ad() {
    val preferableWidth = dimensionResource(id = R.dimen.min_grid_cell_size)
    val feedbackDots = if (isSystemInDarkTheme()) "feedback_light_dots" else "feedback_dark_dots"
    val context = LocalContext.current

    val loader = remember(context) {
        NativeAdLoader(context)
    }

    AndroidViewBinding(
        factory = NativeAdBinding::inflate
    ) {
        loader.setNativeAdLoadListener(object : NativeAdLoadListener {
            override fun onAdLoaded(nativeAd: NativeAd) {
                val nativeAdViewBinder = NativeAdViewBinder.Builder(root)
                    .setCallToActionView(this@AndroidViewBinding.callToAction)
                    .setDomainView(this@AndroidViewBinding.domain)
                    .setFeedbackView(this@AndroidViewBinding.feedback)
                    .setIconView(this@AndroidViewBinding.icon)
                    .setMediaView(this@AndroidViewBinding.media)
                    .setPriceView(this@AndroidViewBinding.price)
                    .setSponsoredView(this@AndroidViewBinding.sponsored)
                    .setTitleView(this@AndroidViewBinding.title)
                    .setWarningView(this@AndroidViewBinding.warning)
                    .build()

                try {
                    nativeAd.bindNativeAd(nativeAdViewBinder)
                    root.visibility = View.VISIBLE
                } catch (exception: NativeAdException) {
                    Log.d("Tag111", exception.toString())
                }
            }

            override fun onAdFailedToLoad(error: AdRequestError) {
                Log.d("Tag111", error.toString())
            }

        })

        val parameters = mapOf(
            "preferable-width" to "$preferableWidth",
            "feedback_image" to feedbackDots
        )

        val nativeAdRequestConfiguration =
            NativeAdRequestConfiguration.Builder("R-M-DEMO-native-i")
                .setParameters(parameters).build()

        loader.loadAd(nativeAdRequestConfiguration)
    }
}

/*
@Composable
fun Ad() {
    AndroidViewBinding(factory = NativeAdBinding::inflate) {
        val adView = root.also { adView ->
            adView.mediaView = mediaView
            adView.iconView = iconView
            adView.callToActionView = callToActionBtn
            adView.headlineView = headlineView
            adView.bodyView = bodyView
        }

        val adLoader = AdLoader.Builder(adView.context, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { nativeAd ->
                nativeAd.icon?.let { icon ->
                    iconView.setImageDrawable(icon.drawable)
                }

                nativeAd.callToAction?.let { cta ->
                    callToActionBtn.text = cta
                }

                nativeAd.headline?.let { headline ->
                    headlineView.text = headline
                }

                nativeAd.body?.let { body ->
                    bodyView.text = body
                }

                adView.setNativeAd(nativeAd)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("Tag111", "${adError.code}: ${adError.message}")
                }

                override fun onAdLoaded() {
                    Log.d("Tag111", "adLoaded")
                    super.onAdLoaded()
                }
            })
        .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }
}*/
