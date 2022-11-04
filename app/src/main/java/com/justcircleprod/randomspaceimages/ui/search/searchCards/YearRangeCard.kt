package com.justcircleprod.randomspaceimages.ui.search.searchCards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.NumberPicker
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.remote.RemoteConstants

@Composable
fun YearRangeCard(
    yearStart: MutableState<Int>,
    yearEnd: MutableState<Int>
) {
    Box {
        Card(
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.main_rounded_corner_radius)),
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.search_year_sliders_card_height))
        ) {
            Image(
                painter = painterResource(id = R.drawable.year_sliders_card_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(Color.Black.copy(0.4f), BlendMode.Darken),
                modifier = Modifier.fillMaxSize()
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NumberPicker(
                    value = yearStart.value,
                    range = RemoteConstants.NASA_LIBRARY_YEAR_START..RemoteConstants.NASA_LIBRARY_YEAR_END,
                    onValueChange = {
                        yearStart.value = it

                        if (yearStart.value > yearEnd.value) {
                            yearEnd.value = yearStart.value
                        }
                    },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Text(
                    text = "${yearStart.value} — ${yearEnd.value}",
                    color = Color.White,
                    maxLines = 1,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                NumberPicker(
                    value = yearEnd.value,
                    range = yearStart.value..RemoteConstants.NASA_LIBRARY_YEAR_END,
                    onValueChange = {
                        yearEnd.value = it

                        if (yearEnd.value < yearStart.value) {
                            yearStart.value = yearEnd.value
                        }
                    },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}