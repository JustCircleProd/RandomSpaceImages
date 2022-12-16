package com.justcircleprod.randomspaceimages.ui.apod.apodPage

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun APODPage(
    viewModel: APODPageViewModel
) {
    val apodList = viewModel.apodList.collectAsState()

    val isLoading = viewModel.isLoading.collectAsState()
    val loadError = viewModel.loadError.collectAsState()

    LazyColumn {

    }
}