package com.justcircleprod.randomspaceimages.ui.detailImage

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.common.BackButton
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily
import com.ortiz.touchview.TouchImageView

@Composable
fun DetailImageFragmentContent(
    imageUrl: String,
    imageUrlHd: String?,
    onBackButtonClick: () -> Unit
) {
    Scaffold(
        backgroundColor = colorResource(id = R.color.background),
        scaffoldState = rememberScaffoldState()
    )
    { scaffoldPadding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(scaffoldPadding)
        ) {
            val (imageView, backButton, hdButton) = createRefs()

            val loadState = remember { mutableStateOf(DetailImageLoadState.FIRST_LOAD) }
            val inHd = remember { mutableStateOf(false) }

            if (loadState.value == DetailImageLoadState.FIRST_LOAD) {
                ProgressIndicator()
            }

            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(imageView) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                factory = { context ->
                    TouchImageView(context).apply {
                        Glide.with(context).load(imageUrl)
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    loadState.value = DetailImageLoadState.LOADED
                                    return false
                                }
                            }).into(this)
                    }
                },
                update = {
                    val url = if (inHd.value) imageUrlHd else imageUrl

                    Glide.with(it.context).load(url)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                loadState.value = DetailImageLoadState.LOADED
                                return false
                            }
                        }).into(it)
                }
            )

            BackButton(
                modifier = Modifier.constrainAs(backButton) {
                    start.linkTo(parent.start, margin = 9.dp)
                    top.linkTo(parent.top, margin = 9.dp)
                },
                onClick = onBackButtonClick
            )

            if (imageUrlHd != null) {
                HdButton(
                    activationState = inHd,
                    loadState = loadState,
                    modifier = Modifier.constrainAs(hdButton) {
                        end.linkTo(parent.end, margin = 9.dp)
                        top.linkTo(parent.top, margin = 9.dp)
                    },
                    onClick = {
                        inHd.value = !inHd.value
                        loadState.value = DetailImageLoadState.LOADING
                    }
                )
            }
        }
    }
}

@Composable
fun HdButton(
    activationState: MutableState<Boolean>,
    loadState: MutableState<DetailImageLoadState>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(CircleShape)
            .background(Color.Black.copy(0.45f))
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
        if (loadState.value == DetailImageLoadState.LOADING) {
            CircularProgressIndicator(
                strokeWidth = dimensionResource(id = R.dimen.progress_indicator_stroke_width),
                color = colorResource(id = R.color.primary),
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.action_button_progress_indicator_size))
                    .align(Alignment.Center)
            )
        } else {
            Text(
                text = "HD",
                color = if (activationState.value) colorResource(id = R.color.success) else Color.White,
                fontFamily = LatoFontFamily,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}