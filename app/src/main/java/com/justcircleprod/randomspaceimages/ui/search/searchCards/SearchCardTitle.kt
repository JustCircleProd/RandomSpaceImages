package com.justcircleprod.randomspaceimages.ui.search.searchCards

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily

@Composable
fun SearchCardTitle(titleText: String) {
    Text(
        text = titleText,
        color = colorResource(id = R.color.text),
        fontFamily = LatoFontFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.search_card_top_space_size))
            .padding(bottom = dimensionResource(id = R.dimen.search_card_title_bottom_space_size))
            .fillMaxWidth()
    )
}