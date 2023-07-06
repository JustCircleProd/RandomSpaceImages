package com.justcircleprod.randomspaceimages.ui.more

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.local.settings.DataStoreConstants
import com.justcircleprod.randomspaceimages.ui.more.expandableCard.ExpandableCard
import com.justcircleprod.randomspaceimages.ui.more.licenses.Licenses
import com.justcircleprod.randomspaceimages.ui.more.licenses.LicensesList
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily

@Composable
fun MoreFragmentContent(viewModel: MoreViewModel) {
    Scaffold(
        backgroundColor = colorResource(id = R.color.background),
        scaffoldState = rememberScaffoldState()
    )
    { scaffoldPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.more_screen_cards_vertical_arrangement)),
            contentPadding = PaddingValues(bottom = dimensionResource(id = R.dimen.more_screen_bottom_space_size)),
            modifier = Modifier
                .padding(scaffoldPadding)
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
                QualityOfSavingAndSharingImagesCard(viewModel = viewModel)
            }
            item {
                WhereAreTheImagesFromCard()
            }
            item {
                DevelopersAndLicensesCard()
            }
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
            stringResource(id = R.string.apod),
            stringResource(id = R.string.random)
        )

        val isSelected: (Int) -> Boolean = { index ->
            when {
                index == 0 && (startScreen.value == DataStoreConstants.APOD_SCREEN || startScreen.value == null) -> true
                index == 1 && startScreen.value == DataStoreConstants.RANDOM_SCREEN -> true
                else -> false
            }
        }

        val onClick: (Int) -> Unit = { index ->
            val selectedValue = when (index) {
                0 -> DataStoreConstants.APOD_SCREEN
                1 -> DataStoreConstants.RANDOM_SCREEN
                else -> DataStoreConstants.APOD_SCREEN
            }

            viewModel.saveStartScreenValue(selectedValue)
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
fun QualityOfSavingAndSharingImagesCard(viewModel: MoreViewModel) {
    val qualityOfSavingAndSharingImages =
        viewModel.qualityOfSavingAndSharingImages.collectAsState(initial = "not_initialized")

    ExpandableCard(cardTitle = stringResource(id = R.string.quality_of_saving_and_sharing_images)) { contentModifier ->
        val radioButtonItems = listOf(
            stringResource(id = R.string.standard_quality),
            stringResource(id = R.string.high_quality)
        )

        val isSelected: (Int) -> Boolean = { index ->
            when {
                index == 0 && (qualityOfSavingAndSharingImages.value == DataStoreConstants.STANDARD_QUALITY || qualityOfSavingAndSharingImages.value == null) -> true
                index == 1 && qualityOfSavingAndSharingImages.value == DataStoreConstants.HIGH_QUALITY -> true
                else -> false
            }
        }

        val onClick: (Int) -> Unit = { index ->
            val selectedValue = when (index) {
                0 -> DataStoreConstants.STANDARD_QUALITY
                1 -> DataStoreConstants.HIGH_QUALITY
                else -> DataStoreConstants.STANDARD_QUALITY
            }

            viewModel.saveQualityOfSavingAndSharingImagesValue(selectedValue)
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

            Text(
                text = stringResource(id = R.string.quality_of_saving_and_sharing_images_hint),
                color = colorResource(id = R.color.second_text),
                fontFamily = LatoFontFamily,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.more_card_hint_top_space_size))
                    .padding(horizontal = dimensionResource(id = R.dimen.more_card_horizontal_space_size))
            )
        }
    }
}

@Composable
fun WhereAreTheImagesFromCard() {
    ExpandableCard(cardTitle = stringResource(id = R.string.about_apis)) { contentModifier ->
        Column(
            modifier = contentModifier
                .padding(horizontal = dimensionResource(id = R.dimen.more_card_horizontal_space_size))
        ) {
            Text(
                text = stringResource(id = R.string.where_are_the_media_from),
                color = colorResource(id = R.color.text),
                fontFamily = LatoFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.more_card_content_title_bottom_space_size)))

            Text(
                text = stringResource(id = R.string.where_are_the_media_from_text),
                color = colorResource(id = R.color.second_text),
                fontFamily = LatoFontFamily,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.more_card_content_paragraph_space_size)))

            Text(
                text = stringResource(id = R.string.request_limits),
                color = colorResource(id = R.color.text),
                fontFamily = LatoFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.more_card_content_title_bottom_space_size)))

            Text(
                text = stringResource(id = R.string.request_limits_text),
                color = colorResource(id = R.color.second_text),
                fontFamily = LatoFontFamily,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun DevelopersAndLicensesCard() {
    ExpandableCard(cardTitle = stringResource(id = R.string.developers_and_licenses)) { contentModifier ->
        Column(
            modifier = contentModifier
                .padding(horizontal = dimensionResource(id = R.dimen.more_card_horizontal_space_size))
        ) {
            Image(
                painter = painterResource(id = R.drawable.developers_logo),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.developers_logo_rounded_corner_size))),
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.more_card_content_paragraph_space_size)))

            LicensesList(licenses = Licenses)
        }
    }
}



