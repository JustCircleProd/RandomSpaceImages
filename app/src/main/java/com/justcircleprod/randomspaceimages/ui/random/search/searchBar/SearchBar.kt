package com.justcircleprod.randomspaceimages.ui.random.search.searchBar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.random.common.SearchButton
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily

@Composable
fun SearchBar(
    searchText: MutableState<String>,
    onBackButtonClick: () -> Unit,
    onSearchButtonClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.search_bar_top_space_size))
    ) {
        val (backButton, searchField, searchButton) = createRefs()

        SearchBarBackButton(
            onClick = onBackButtonClick,
            modifier = Modifier
                .constrainAs(backButton) {
                    top.linkTo(searchField.top)
                    bottom.linkTo(searchField.bottom)
                    start.linkTo(parent.start)
                }
        )
        SearchField(
            searchText = searchText,
            onSearch = onSearchButtonClick,
            modifier = Modifier
                .constrainAs(searchField) {
                    top.linkTo(parent.top)
                    start.linkTo(backButton.end)
                    end.linkTo(searchButton.start)
                }
        )
        SearchButton(
            onSearchButtonClick,
            modifier = Modifier
                .constrainAs(searchButton) {
                    top.linkTo(searchField.top)
                    bottom.linkTo(searchField.bottom)
                    end.linkTo(parent.end)
                }
        )
    }
}

@Composable
fun SearchBarBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(CircleShape)
            .size(dimensionResource(id = R.dimen.search_back_button_size))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    color = colorResource(id = R.color.ripple)
                ),
            ) {
                onClick()
            }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_back_arrow),
            contentDescription = stringResource(id = R.string.back_button),
            tint = colorResource(id = R.color.icon_tint),
            modifier = Modifier.size(dimensionResource(id = R.dimen.search_back_button_icon_size))
        )
    }
}

@Composable
fun SearchField(
    searchText: MutableState<String>,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val searchBarWidth =
        LocalConfiguration.current.screenWidthDp.dp - dimensionResource(id = R.dimen.search_button_summary_width) - dimensionResource(
            id = R.dimen.search_back_button_summary_width
        )

    BasicTextField(
        value = searchText.value,
        singleLine = true,
        cursorBrush = SolidColor(colorResource(id = R.color.primary)),
        maxLines = 1,
        textStyle = TextStyle(
            color = colorResource(id = R.color.search_field_text),
            fontFamily = LatoFontFamily,
            fontSize = 15.sp
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            }
        ),
        onValueChange = {
            searchText.value = it
        },
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.search_field_rounded_corner_radius)))
            .background(colorResource(id = R.color.search_field_background))
            .width(searchBarWidth)
            .padding(vertical = dimensionResource(id = R.dimen.search_field_inner_vertical_padding))
            .padding(
                horizontal = dimensionResource(id = R.dimen.search_field_inner_horizontal_padding)
            )
    ) { innerTextField ->
        Box {
            innerTextField()

            Text(
                text = stringResource(id = R.string.search_bar_placeholder),
                color = colorResource(id = R.color.search_field_placeholder),
                fontFamily = LatoFontFamily,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.alpha(if (searchText.value.isEmpty()) 1f else 0f)
            )
        }
    }
}