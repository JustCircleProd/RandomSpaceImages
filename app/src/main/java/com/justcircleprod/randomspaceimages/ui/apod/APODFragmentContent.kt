package com.justcircleprod.randomspaceimages.ui.apod

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.apod.apodFavourites.APODFavouritesPage
import com.justcircleprod.randomspaceimages.ui.apod.apodFavourites.APODFavouritesPageViewModel
import com.justcircleprod.randomspaceimages.ui.apod.apodPage.APODPage
import com.justcircleprod.randomspaceimages.ui.apod.apodPage.APODPageEvent
import com.justcircleprod.randomspaceimages.ui.apod.apodPage.APODPageViewModel
import com.justcircleprod.randomspaceimages.ui.apod.apodTabs.APODTabItem
import com.justcircleprod.randomspaceimages.ui.common.pagerTabIndicatorOffset
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily
import com.justcircleprod.randomspaceimages.ui.theme.NoRippleTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun APODFragmentContent(
    apodPageViewModel: APODPageViewModel,
    apodFavouritesPageViewModel: APODFavouritesPageViewModel,
    onPickDateButtonClick: () -> Unit,
    onAPODEntryImageClick: (imageUrl: String, imageUrlHd: String?) -> Unit
) {
    val pagerState = rememberPagerState()

    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = dimensionResource(id = R.dimen.apod_tabs_elements_space_size),
                alignment = Alignment.Start
            ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            val pickedDate = apodPageViewModel.pickedDateInMills.collectAsStateWithLifecycle()

            Tabs(pickedDate = pickedDate, pagerState = pagerState)

            PickDateButtons(
                pickedDate = pickedDate,
                onPickDateButtonClick = {
                    onPickDateButtonClick()
                },
                onCancelButtonClick = {
                    apodPageViewModel.onEvent(APODPageEvent.OnCancelDateButtonClick)
                }
            )
        }

        HorizontalPager(pageCount = APODTabItem.items.size, state = pagerState) { page ->
            when (page) {
                0 -> {
                    APODPage(
                        viewModel = apodPageViewModel,
                        onAPODEntryImageClick = onAPODEntryImageClick
                    )
                }

                1 -> {
                    APODFavouritesPage(
                        viewModel = apodFavouritesPageViewModel,
                        onAPODEntryImageClick = onAPODEntryImageClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState, pickedDate: State<Long?>) {
    val coroutineScope = rememberCoroutineScope()

    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        val scrollableTabRowWidth by animateDpAsState(
            when {
                pagerState.currentPage == 0 && pickedDate.value != null -> {
                    LocalConfiguration.current.screenWidthDp.dp - dimensionResource(id = R.dimen.apod_tabs_two_buttons_summary_width)
                }
                pagerState.currentPage == 0 && pickedDate.value == null -> {
                    LocalConfiguration.current.screenWidthDp.dp - dimensionResource(id = R.dimen.apod_tabs_one_button_summary_width)
                }
                else -> {
                    LocalConfiguration.current.screenWidthDp.dp
                }
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

@Composable
fun PickDateButtons(
    pickedDate: State<Long?>,
    onPickDateButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .size(dimensionResource(id = R.dimen.tabs_button_size))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    color = colorResource(id = R.color.ripple)
                ),
            ) {
                onPickDateButtonClick()
            }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_pick_date),
            contentDescription = stringResource(id = R.string.select_a_date),
            tint = if (pickedDate.value != null) colorResource(id = R.color.primary) else colorResource(
                id = R.color.icon_tint
            ),
            modifier = Modifier.size(dimensionResource(id = R.dimen.tabs_button_icon_size))
        )
    }

    if (pickedDate.value != null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .size(dimensionResource(id = R.dimen.tabs_button_size))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        bounded = true,
                        color = colorResource(id = R.color.ripple)
                    ),
                ) {
                    onCancelButtonClick()
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_cancel),
                contentDescription = stringResource(id = R.string.cancel_date_selection),
                tint = colorResource(id = R.color.icon_tint),
                modifier = Modifier.size(dimensionResource(id = R.dimen.tabs_button_icon_size))
            )
        }
    }

    Spacer(Modifier.width(dimensionResource(id = R.dimen.apod_tabs_elements_space_size)))
}