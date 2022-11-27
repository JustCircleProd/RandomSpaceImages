package com.justcircleprod.randomspaceimages.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.models.ImageEntry
import com.justcircleprod.randomspaceimages.ui.home.favourites.FavouriteImageList
import com.justcircleprod.randomspaceimages.ui.home.favourites.FavouriteImageListViewModel
import com.justcircleprod.randomspaceimages.ui.home.random.RandomImageList
import com.justcircleprod.randomspaceimages.ui.home.random.RandomImageListViewModel
import com.justcircleprod.randomspaceimages.ui.home.tabs.TabItem
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily
import com.justcircleprod.randomspaceimages.ui.theme.NoRippleTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeFragmentContent(
    randomImageListViewModel: RandomImageListViewModel,
    favouriteImageListViewModel: FavouriteImageListViewModel,
    onImageEntryClick: (imageEntry: ImageEntry) -> Unit
) {
    val pagerState = rememberPagerState()

    Column {
        Tabs(pagerState)

        HorizontalPager(count = TabItem.items.size, state = pagerState) { page ->
            when (page) {
                0 -> {
                    RandomImageList(
                        viewModel = randomImageListViewModel,
                        onImageEntryClick = onImageEntryClick
                    )
                }
                1 -> {
                    FavouriteImageList(
                        viewModel = favouriteImageListViewModel,
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
    val scope = rememberCoroutineScope()

    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
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
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.tabs_padding_bottom))
        ) {
            TabItem.items.forEachIndexed { index, tabItem ->
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
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }
    }
}