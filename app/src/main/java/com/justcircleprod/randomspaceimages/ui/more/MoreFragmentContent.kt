package com.justcircleprod.randomspaceimages.ui.more

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.dataStore.DataStoreConstants
import com.justcircleprod.randomspaceimages.ui.more.expandableCard.ExpandableCard
import com.justcircleprod.randomspaceimages.ui.more.licenses.Licenses
import com.justcircleprod.randomspaceimages.ui.more.licenses.LicensesList
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily

@Composable
fun MoreFragmentContent(viewModel: MoreViewModel) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.more_screen_cards_vertical_arrangement)),
        contentPadding = PaddingValues(bottom = dimensionResource(id = R.dimen.more_screen_bottom_space_size)),
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.more_screen_horizontal_space_size))
            .fillMaxSize()
    ) {
        item {
            Spacer(Modifier.height(dimensionResource(id = R.dimen.more_screen_top_space_size)))
        }

        item {
            ThemeCard(viewModel = viewModel)
        }
        item {
            StartScreenCard(viewModel = viewModel)
        }
        item {
            WhereAreTheImagesFromCard()
        }
        item {
            RequestLimitsCard()
        }
        item {
            DevelopersCard()
        }
        item {
            OpenSourceLicensesCard()
        }
    }
}

@Composable
fun ThemeCard(viewModel: MoreViewModel) {
    val themeValue =
        viewModel.themeValue.collectAsState(initial = "not_initialized")

    ExpandableCard(cardTitle = stringResource(id = R.string.app_theme)) { contentModifier ->
        val radioButtonItems = listOf(
            stringResource(id = R.string.system_default_theme),
            stringResource(id = R.string.light_theme),
            stringResource(id = R.string.dark_theme)
        )

        val isSelected: (Int) -> Boolean = { index ->
            when {
                index == 0 && (themeValue.value == DataStoreConstants.SYSTEM_THEME || themeValue.value == null) -> true
                index == 1 && themeValue.value == DataStoreConstants.LIGHT_THEME -> true
                index == 2 && themeValue.value == DataStoreConstants.DARK_THEME -> true
                else -> false
            }
        }

        val onClick: (Int) -> Unit = { index ->
            val selectedThemeValue = when (index) {
                0 -> DataStoreConstants.SYSTEM_THEME
                1 -> DataStoreConstants.LIGHT_THEME
                2 -> DataStoreConstants.DARK_THEME
                else -> DataStoreConstants.SYSTEM_THEME
            }

            viewModel.saveThemeValue(selectedThemeValue)
        }

        Column(modifier = contentModifier) {
            radioButtonItems.forEachIndexed { index, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(
                                bounded = true,
                                color = colorResource(id = R.color.ripple)
                            )
                        ) {
                            onClick(index)
                        }
                        .padding(vertical = dimensionResource(id = R.dimen.theme_item_vertical_space_size))
                        .padding(horizontal = dimensionResource(id = R.dimen.more_card_horizontal_space_size))
                        .fillMaxWidth()
                ) {
                    RadioButton(
                        selected = isSelected(index),
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            unselectedColor = colorResource(id = R.color.unselected_radio_button),
                            selectedColor = colorResource(id = R.color.primary)
                        )
                    )
                    Text(
                        text = item,
                        color = colorResource(id = R.color.text),
                        fontFamily = LatoFontFamily,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.theme_item_text_start_space_size))
                    )
                }
            }
        }
    }
}

@Composable
fun StartScreenCard(viewModel: MoreViewModel) {
    val startScreen =
        viewModel.startScreenValue.collectAsState(initial = "not_initialized")

    ExpandableCard(cardTitle = stringResource(id = R.string.start_screen)) { contentModifier ->
        val radioButtonItems = listOf(
            stringResource(id = R.string.random),
            stringResource(id = R.string.apod)
        )

        val isSelected: (Int) -> Boolean = { index ->
            when {
                index == 0 && (startScreen.value == DataStoreConstants.RANDOM_SCREEN || startScreen.value == null) -> true
                index == 1 && startScreen.value == DataStoreConstants.APOD_SCREEN -> true
                else -> false
            }
        }

        val onClick: (Int) -> Unit = { index ->
            val selectedThemeValue = when (index) {
                0 -> DataStoreConstants.RANDOM_SCREEN
                1 -> DataStoreConstants.APOD_SCREEN
                else -> DataStoreConstants.RANDOM_SCREEN
            }

            viewModel.saveStartScreenValue(selectedThemeValue)
        }

        Column(modifier = contentModifier) {
            radioButtonItems.forEachIndexed { index, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(
                                bounded = true,
                                color = colorResource(id = R.color.ripple)
                            )
                        ) {
                            onClick(index)
                        }
                        .padding(vertical = dimensionResource(id = R.dimen.theme_item_vertical_space_size))
                        .padding(horizontal = dimensionResource(id = R.dimen.more_card_horizontal_space_size))
                        .fillMaxWidth()
                ) {
                    RadioButton(
                        selected = isSelected(index),
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            unselectedColor = colorResource(id = R.color.unselected_radio_button),
                            selectedColor = colorResource(id = R.color.primary)
                        )
                    )
                    Text(
                        text = item,
                        color = colorResource(id = R.color.text),
                        fontFamily = LatoFontFamily,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.theme_item_text_start_space_size))
                    )
                }
            }
        }
    }
}

@Composable
fun WhereAreTheImagesFromCard() {
    ExpandableCard(cardTitle = stringResource(id = R.string.where_are_the_images_from)) { contentModifier ->
        Text(
            text = stringResource(id = R.string.where_are_the_images_from_answer),
            color = colorResource(id = R.color.text),
            fontFamily = LatoFontFamily,
            fontSize = 16.sp,
            modifier = contentModifier.padding(horizontal = dimensionResource(id = R.dimen.more_card_horizontal_space_size))
        )
    }
}

@Composable
fun RequestLimitsCard() {
    ExpandableCard(cardTitle = stringResource(id = R.string.request_limits)) { contentModifier ->
        Text(
            text = stringResource(id = R.string.request_limits_text),
            color = colorResource(id = R.color.text),
            fontFamily = LatoFontFamily,
            fontSize = 16.sp,
            modifier = contentModifier.padding(horizontal = dimensionResource(id = R.dimen.more_card_horizontal_space_size))
        )
    }
}

@Composable
fun DevelopersCard() {
    ExpandableCard(cardTitle = stringResource(id = R.string.developers)) { contentModifier ->
        Image(
            painter = painterResource(id = R.drawable.developers_logo),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = contentModifier
                .padding(horizontal = dimensionResource(id = R.dimen.more_card_horizontal_space_size))
                .fillMaxSize()
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.developers_logo_rounded_corner_size))),
        )
    }
}

@Composable
fun OpenSourceLicensesCard() {
    ExpandableCard(cardTitle = stringResource(id = R.string.licenses)) { contentModifier ->
        LicensesList(licenses = Licenses, modifier = contentModifier)
    }
}



