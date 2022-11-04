package com.justcircleprod.randomspaceimages.ui.more

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.dataStore.DataStoreConstants
import com.justcircleprod.randomspaceimages.ui.more.licenses.LicensesList
import com.justcircleprod.randomspaceimages.ui.theme.customColors

@Composable
fun MoreScreen(viewModel: MoreViewModel) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.more_screen_cards_vertical_arrangement)),
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
            WhereAreTheImagesFromCard()
        }
        item {
            DevelopersCard()
        }
        item {
            OpenSourceLicensesCard()
        }

        item {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.more_screen_bottom_space_size)))
        }
    }
}

@Composable
fun ExpandableCard(
    cardTitle: String,
    cardContent: @Composable (contentModifier: Modifier) -> Unit
) {
    val isCardExpanded = rememberSaveable { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.more_card_rounded_corner_radius)),
        backgroundColor = MaterialTheme.customColors.cardBackground,
        elevation = dimensionResource(id = R.dimen.card_elevation),
        modifier = Modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.more_card_rounded_corner_radius)))
            .clickable {
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
                    color = MaterialTheme.customColors.text,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 17.sp
                )

                val iconPainter = if (isCardExpanded.value) {
                    painterResource(id = R.drawable.icon_arrow_up)
                } else {
                    painterResource(id = R.drawable.icon_arrow_down)
                }

                Icon(
                    painter = iconPainter,
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.more_card_icon_size))
                )
            }

            AnimatedVisibility(visible = isCardExpanded.value)
            {
                cardContent(contentModifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.more_card_vertical_space_size)))
            }
        }
    }
}

@Composable
fun ThemeCard(viewModel: MoreViewModel) {
    val themeValue =
        viewModel.themeValue.collectAsState(initial = DataStoreConstants.SYSTEM_THEME)

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
                        .selectable(
                            selected = isSelected(index),
                            onClick = { onClick(index) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = dimensionResource(id = R.dimen.theme_item_vertical_space_size))
                        .padding(horizontal = dimensionResource(id = R.dimen.more_card_horizontal_space_size))
                        .fillMaxWidth()
                ) {
                    RadioButton(
                        selected = isSelected(index),
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary
                        )
                    )
                    Text(
                        text = item,
                        color = MaterialTheme.customColors.text,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.theme_item_text_start_space_size))
                    )
                }
            }

            Text(
                text = stringResource(id = R.string.app_theme_warning),
                color = MaterialTheme.customColors.text,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.theme_warning_text_top_space_size))
                    .padding(horizontal = dimensionResource(id = R.dimen.more_card_horizontal_space_size))
            )
        }
    }
}

@Composable
fun WhereAreTheImagesFromCard() {
    ExpandableCard(cardTitle = stringResource(id = R.string.where_are_the_images_from)) { contentModifier ->
        Text(
            text = stringResource(id = R.string.where_are_the_images_from_answer),
            color = MaterialTheme.customColors.text,
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
    ExpandableCard(cardTitle = stringResource(id = R.string.open_source_licenses)) { contentModifier ->
        LicensesList(licenses = Licenses, modifier = contentModifier)
    }
}



