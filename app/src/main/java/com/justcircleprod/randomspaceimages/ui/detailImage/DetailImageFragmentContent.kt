package com.justcircleprod.randomspaceimages.ui.detailImage

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView
import com.justcircleprod.randomspaceimages.ui.common.BackButton
import com.justcircleprod.randomspaceimages.ui.common.ProgressIndicator

@Composable
fun DetailImageFragmentContent(
    imageUrl: String,
    onBackButtonClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }

    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (imageView, backButton) = createRefs()

        if (isLoading) {
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