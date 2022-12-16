package com.justcircleprod.randomspaceimages.ui.apod

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.apod.apodFavourites.APODFavouritesPage
import com.justcircleprod.randomspaceimages.ui.apod.apodPage.APODPage
import com.justcircleprod.randomspaceimages.ui.apod.apodPage.APODPageViewModel
import com.justcircleprod.randomspaceimages.ui.apod.apodTabs.APODTabItem
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily
import com.justcircleprod.randomspaceimages.ui.theme.NoRippleTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun APODFragmentContent(
    viewModel: APODPageViewModel
) {
    val pagerState = rememberPagerState()

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tabs(pagerState)
        }

        HorizontalPager(count = APODTabItem.items.size, state = pagerState) { page ->
            when (page) {
                0 -> {
                    APODPage(
                        viewModel = viewModel
                    )
                }
                1 -> {
                    APODFavouritesPage(

                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()

    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        val scrollableTabRowWidth =
            LocalConfiguration.current.screenWidthDp.dp

        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = colorResource(id = R.color.background),
            edgePadding = dimensionResource(id = R.dimen.tabs_edge_padding),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = when (pagerState.currentPage) {
                        0 -> {
                            colorResource(id = R.color.primary)
                        }
                        1 -> {
                            colorResource(id = R.color.secondary)
                        }
                        else -> {
                            colorResource(id = R.color.primary)
                        }
                    },
                    height = dimensionResource(id = R.dimen.tabs_indicator_height),
                    modifier = Modifier
                        .pagerTabIndicatorOffset(
                            pagerState = pagerState,
                            tabPositions = tabPositions,
                        )
                        .padding(horizontal = dimensionResource(id = R.dimen.tabs_indicator_horizontal_padding))
                        .clip(RoundedCornerShape(dimensionResource(id = R.dimen.tabs_indicator_rounded_corner_radius)))
                )
            },
            divider = {
                Spacer(Modifier.height(0.dp))
            },
            modifier = Modifier
                .width(scrollableTabRowWidth)
                .padding(bottom = dimensionResource(id = R.dimen.tabs_padding_bottom))
        ) {
            APODTabItem.items.forEachIndexed { index, tabItem ->
                Tab(
                    text = {
                        Text(
                            text = stringResource(id = tabItem.titleResId),
                            color = colorResource(id = R.color.text),
                            fontFamily = LatoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }
    }
}