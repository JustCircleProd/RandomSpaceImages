package com.justcircleprod.randomspaceimages.ui.random.search.searchCards

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily

@Composable
fun SuggestionCard(
    @StringRes suggestionStringRes: Int,
    suggestionText: String,
    @DrawableRes SuggestionImageRes: Int,
    onClick: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.main_rounded_corner_radius)),
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.suggestion_card_height))
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.main_rounded_corner_radius)))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    color = colorResource(id = R.color.ripple)
                ),
            ) {
                onClick(suggestionText)
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = SuggestionImageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(Color.Black.copy(0.4f), BlendMode.Darken),
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = stringResource(id = suggestionStringRes),
                color = Color.White,
                fontFamily = LatoFontFamily,
                maxLines = 1,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}