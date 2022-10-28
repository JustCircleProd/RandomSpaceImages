package com.justcircleprod.randomspaceimages.ui.detailImage

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import com.justcircleprod.randomspaceimages.data.models.ImageEntry
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.shimmer.ShimmerPlugin
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch

@Composable
fun ZoomableImage(imageEntry: ImageEntry, scrollState: ScrollableState) {
    val coroutineScope = rememberCoroutineScope()

    val localDensity = LocalDensity.current
    var width by remember { mutableStateOf(0f) }
    var height by remember { mutableStateOf(0f) }

    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(1f) }
    var offsetY by remember { mutableStateOf(1f) }

    GlideImage(
        imageModel = { imageEntry.imageHref },
        component = rememberImageComponent {
            +ShimmerPlugin(
                baseColor = MaterialTheme.colors.background,
                highlightColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray,
                durationMillis = 1400,
                dropOff = 0.65f,
                tilt = 20f
            )
        },
        imageOptions = ImageOptions(
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            contentDescription = imageEntry.title
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                width = with(localDensity) { coordinates.size.width.toDp().value }
                height = with(localDensity) { coordinates.size.height.toDp().value }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (scale > 1f) {
                            scale = 1f
                            offsetX = 1f
                            offsetY = 1f
                        } else {
                            scale = 3f
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        awaitFirstDown()
                        do {
                            val event = awaitPointerEvent()
                            scale = (scale * event.calculateZoom()).coerceIn(1f..3f)

                            if (scale > 1) {
                                coroutineScope.launch {
                                    scrollState.setScrolling(false)
                                }

                                val offset = event.calculatePan()

                                offsetX = (offsetX + offset.x)
                                    .coerceIn(-(width * scale)..width * scale)

                                offsetY = (offsetY + offset.y)
                                    .coerceIn(-(height * scale)..height * scale)


                                coroutineScope.launch {
                                    scrollState.setScrolling(true)
                                }
                            } else {
                                scale = 1f
                                offsetX = 1f
                                offsetY = 1f
                            }
                        } while (event.changes.any { it.pressed })
                    }
                }
            }
            .graphicsLayer {
                scaleX = scale.coerceIn(1f..3f)
                scaleY = scale.coerceIn(1f..3f)

                translationX = offsetX
                translationY = offsetY
            }
    )
}

private suspend fun ScrollableState.setScrolling(value: Boolean) {
    scroll(scrollPriority = MutatePriority.PreventUserInput) {
        when (value) {
            true -> Unit
            else -> awaitCancellation()
        }
    }
}