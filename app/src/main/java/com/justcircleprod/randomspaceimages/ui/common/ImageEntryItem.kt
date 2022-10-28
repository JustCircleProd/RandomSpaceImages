package com.justcircleprod.randomspaceimages.ui.common

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.models.ImageEntry
import com.justcircleprod.randomspaceimages.ui.baseViewModel.BaseViewModel
import com.justcircleprod.randomspaceimages.ui.navigation.Screen
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin

@Composable
fun ImageEntryItem(
    imageEntry: ImageEntry,
    navController: NavHostController,
    viewModel: BaseViewModel
) {
    val isClickEnabled = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
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
        imageModel = { imageEntry.imageHref },
        imageOptions = ImageOptions(
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            contentDescription = imageEntry.title
        ),
        onImageStateChanged = {
            when (it) {
                GlideImageState.None -> {}
                GlideImageState.Loading -> {}
                is GlideImageState.Success -> {
                    isClickEnabled.value = true
                }
                is GlideImageState.Failure -> {}
            }
        },
        component = rememberImageComponent {
            +ShimmerPlugin(
                baseColor = MaterialTheme.colors.background,
                highlightColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray,
                durationMillis = 1400,
                dropOff = 0.65f,
                tilt = 20f
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.main_rounded_corner_radius)))
    )
}

@Composable
fun ImageExtra(imageEntry: ImageEntry, viewModel: BaseViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.image_list_item_extra_padding_top))
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
        modifier = modifier.padding(start = dimensionResource(id = R.dimen.image_list_title_padding_start))
    )
}

// yandex ad
/*@Composable
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
}*/

// admob ad
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
