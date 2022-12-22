package com.justcircleprod.randomspaceimages.ui.more.expandableCard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily

@Composable
fun ExpandableCard(
    cardTitle: String,
    CardContent: @Composable (contentModifier: Modifier) -> Unit
) {
    val isCardExpanded = rememberSaveable { mutableStateOf(false) }
    val angle by animateFloatAsState(if (isCardExpanded.value) 180f else 0f)

    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.more_card_rounded_corner_radius)),
        backgroundColor = colorResource(id = R.color.card_background),
        elevation = dimensionResource(id = R.dimen.card_elevation),
        modifier = Modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.more_card_rounded_corner_radius)))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    color = colorResource(id = R.color.ripple)
                )
            ) {
                isCardExpanded.value = !isCardExpanded.value
            }
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.more_card_vertical_space_size))
                    .padding(horizontal = dimensionResource(id = R.dimen.more_card_horizontal_space_size))
                    .fillMaxWidth()
            ) {
                Text(
                    text = cardTitle,
                    color = colorResource(id = R.color.text),
                    fontFamily = LatoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 17.sp
                )

                Icon(
                    painter = painterResource(id = R.drawable.icon_arrow_down),
                    contentDescription = null,
                    tint = colorResource(id = R.color.icon_tint),
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.more_card_icon_size))
                        .rotate(angle)
                )
            }

            AnimatedVisibility(visible = isCardExpanded.value)
            {
                CardContent(contentModifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.more_card_vertical_space_size)))
            }
        }
    }
}