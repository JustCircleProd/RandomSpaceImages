package com.justcircleprod.randomspaceimages.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.theme.customColors

@Composable
fun ProgressIndicator(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Card(
            shape = CircleShape,
            backgroundColor = MaterialTheme.customColors.cardBackground,
            elevation = dimensionResource(id = R.dimen.progress_card_elevation)
        ) {
            Box(modifier = Modifier.padding(dimensionResource(id = R.dimen.progress_card_padding))) {
                CircularProgressIndicator(
                    strokeWidth = dimensionResource(id = R.dimen.progress_indicator_stroke_width),
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.progress_indicator_size))
                        .align(Alignment.Center)
                )
            }
        }
    }
}