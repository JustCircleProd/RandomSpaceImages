package com.justcircleprod.randomspaceimages.ui.detailImage

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
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
import com.github.chrisbanes.photoview.PhotoView
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.common.BackButton
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily

@Composable
fun DetailImageFragmentContent(
    imageUrl: String,
    onBackButtonClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (imageView, backButton) = createRefs()

        if (isLoading) {
            DetailImageProgressIndicator()
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
                PhotoView(context).also { photoView ->
                    photoView.maximumScale = 4f
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
                                isLoading = false
                                return false
                            }
                        }).into(photoView)
                }
            }
        )

        BackButton(
            modifier = Modifier.constrainAs(backButton) {
                start.linkTo(parent.start, margin = 9.dp)
                top.linkTo(parent.top, margin = 9.dp)
            },
            onClick = onBackButtonClick
        )
    }
}

@Composable
fun DetailImageProgressIndicator() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Loading the image in high resolution",
            color = colorResource(id = R.color.text),
            fontFamily = LatoFontFamily,
            textAlign = TextAlign.Center,
            fontSize = 17.sp
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.detail_image_progress_indicator_spacer_size)))

        Card(
            shape = CircleShape,
            backgroundColor = colorResource(id = R.color.card_background),
            elevation = dimensionResource(id = R.dimen.progress_card_elevation)
        ) {
            Box(modifier = Modifier.padding(dimensionResource(id = R.dimen.progress_card_padding))) {
                CircularProgressIndicator(
                    strokeWidth = dimensionResource(id = R.dimen.progress_indicator_stroke_width),
                    color = colorResource(id = R.color.primary),
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.progress_indicator_size))
                        .align(Alignment.Center)
                )
            }
        }
    }
}