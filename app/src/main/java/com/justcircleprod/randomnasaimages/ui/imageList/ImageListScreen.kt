package com.justcircleprod.randomnasaimages.ui.imageList

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.justcircleprod.randomnasaimages.data.models.ImageEntry
import com.justcircleprod.randomnasaimages.ui.MainActivity
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ImageListScreen(navController: NavController) {
    val viewModel: ImageListViewModel = hiltViewModel()
    LaunchedEffect(true) {
        viewModel.loadImages()
    }
    ImageList(navController = navController, viewModel = viewModel)
}

@Composable
fun ImageList(
    navController: NavController,
    viewModel: ImageListViewModel
) {
    val imageEntryList by remember { viewModel.imageEntries }
    val isLoading by remember { viewModel.isLoading }
    val loadError by remember { viewModel.loadError }
    val endReached by remember { viewModel.endReached }

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(imageEntryList.size) {
            if (it >= imageEntryList.size - 1 && !endReached && !isLoading) {
                viewModel.loadImages()
            }
            ImageItem(navController = navController, imageEntry = imageEntryList[it])
        }
        if (isLoading && imageEntryList.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                ProgressIndicator()
            }
        }
    }

    Column {
        if (isLoading && imageEntryList.isEmpty()) {
            ProgressIndicator()
        }

        if (loadError.isNotEmpty()) {
            RetrySection(loadError = loadError) {
                viewModel.loadImages()
            }
        }
    }
}

@Composable
fun ProgressIndicator() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            strokeWidth = 3.dp,
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun ImageItem(
    navController: NavController,
    imageEntry: ImageEntry
) {
    val clickEnabled = remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .padding(horizontal = 6.dp)
        .padding(top = 6.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable {
            if (clickEnabled.value) {
                val json = Uri.encode(Gson().toJson(imageEntry))
                navController.navigate("${MainActivity.DETAIL_IMAGE_SCREEN_DESTINATION}/${json}")
            }
        }
    ) {
        GlideImage(
            imageModel = imageEntry.imageHref,
            requestListener = object: RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    clickEnabled.value = false
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    clickEnabled.value = true
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
                .clip(RoundedCornerShape(12.dp))
                .aspectRatio(1f),
        )

        if (imageEntry.title != null) {
            Text(
                text = imageEntry.title,
                maxLines = 1,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .padding(horizontal = 6.dp)
                    .padding(bottom = 6.dp)
            )
        }
    }
}

@Composable
fun RetrySection(
    loadError: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = loadError, color = Color.Red, fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Button(onClick = { onRetry() }) {
            Text(text = "Retry")
        }
    }
}