package com.justcircleprod.randomspaceimages.ui.random

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
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
import com.justcircleprod.randomspaceimages.data.models.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.ui.common.SearchButton
import com.justcircleprod.randomspaceimages.ui.random.randomFavouritesPage.RandomFavouritesPage
import com.justcircleprod.randomspaceimages.ui.random.randomFavouritesPage.RandomFavouritesPageViewModel
import com.justcircleprod.randomspaceimages.ui.random.randomPage.RandomPage
import com.justcircleprod.randomspaceimages.ui.random.randomPage.RandomPageViewModel
import com.justcircleprod.randomspaceimages.ui.random.randomTabs.RandomTabItem
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily
import com.justcircleprod.randomspaceimages.ui.theme.NoRippleTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun RandomFragmentContent(
    randomViewModel: RandomPageViewModel,
    favouriteViewModel: RandomFavouritesPageViewModel,
    onImageEntryClick: (nasaLibraryImageEntry: NASALibraryImageEntry) -> Unit,
    onSearchClick: () -> Unit
) {
    val pagerState = rememberPagerState()

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tabs(pagerState)

            if (pagerState.currentPage == 0) {
                SearchButton(
                    onClick = { onSearchClick() },
                    modifier = Modifier.padding(end = 6.dp)
                )
            }
        }

        HorizontalPager(count = RandomTabItem.items.size, state = pagerState) { page ->
            when (page) {
                0 -> {
                    RandomPage(
                        viewModel = randomViewModel,
                        onImageEntryClick = onImageEntryClick
                    )
                }
                1 -> {
                    RandomFavouritesPage(
                        viewModel = favouriteViewModel,
                        onImageEntryClick = onImageEntryClick
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
        val scrollableTabRowWidth by animateDpAsState(
            if (pagerState.currentPage == 0) {
                LocalConfiguration.current.screenWidthDp.dp - dimensionResource(id = R.dimen.search_button_summary_width)
            } else {
                LocalConfiguration.current.screenWidthDp.dp
            }
        )

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
            RandomTabItem.items.forEachIndexed { index, tabItem ->
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