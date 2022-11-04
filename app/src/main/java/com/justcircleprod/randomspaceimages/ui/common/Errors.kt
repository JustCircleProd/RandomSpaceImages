package com.justcircleprod.randomspaceimages.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.theme.customColors

@Composable
fun ErrorInfo(
    modifier: Modifier = Modifier,
    errorText: String = stringResource(id = R.string.error_info_text)
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = errorText,
            color = MaterialTheme.customColors.text,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.rocket_icon_space_size)))
        Icon(
            painter = painterResource(id = R.drawable.icon_rocket_launch),
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(id = R.dimen.info_icon_size))
        )
    }
}

@Composable
fun ErrorInfoCard(errorText: String = stringResource(id = R.string.error_info_card_text)) {
    Card(
        backgroundColor = MaterialTheme.customColors.errorCardBackground,
        shape = RectangleShape,
        elevation = dimensionResource(id = R.dimen.card_elevation),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.error_info_card_horizontal_space_size))
                .padding(vertical = dimensionResource(id = R.dimen.error_info_card_vertical_space_size)),
        ) {

            Text(
                text = errorText,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.rocket_launch_icon_space_size)))

            Icon(
                painter = painterResource(id = R.drawable.icon_rocket_launch),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.error_info_card_icon_size))
            )
        }
    }
}