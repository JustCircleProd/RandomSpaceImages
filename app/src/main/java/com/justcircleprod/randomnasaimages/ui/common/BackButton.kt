package com.justcircleprod.randomnasaimages.ui.common

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.justcircleprod.randomnasaimages.R

@Composable
fun BackButton(
    navController: NavController,
    modifier: Modifier
) {
    IconButton(
        onClick = { navController.popBackStack() },
        modifier = modifier.size(dimensionResource(id = R.dimen.detail_image_back_button_size))
    ) {
        Icon(
            Icons.Default.KeyboardArrowLeft,
            contentDescription = null,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.detail_image_back_button_icon_size))
                .offset(x = 1.dp, y = 1.dp)
                .alpha(0.4f)
                .blur(dimensionResource(id = R.dimen.action_button_icon_blur)),
            tint = Color(0xFF2E2E2E)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = stringResource(id = R.string.back_button),
            tint = Color.White,
            modifier = Modifier.size(dimensionResource(id = R.dimen.detail_image_back_button_icon_size))
        )
    }
}