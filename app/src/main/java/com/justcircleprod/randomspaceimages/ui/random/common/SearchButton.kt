package com.justcircleprod.randomspaceimages.ui.random.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.justcircleprod.randomspaceimages.R

@Composable
fun SearchButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(CircleShape)
            .size(dimensionResource(id = R.dimen.search_button_size))
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
        Icon(
            painter = painterResource(id = R.drawable.icon_search),
            contentDescription = stringResource(id = R.string.search),
            tint = colorResource(id = R.color.icon_tint),
            modifier = Modifier.size(dimensionResource(id = R.dimen.search_button_icon_size))
        )
    }
}