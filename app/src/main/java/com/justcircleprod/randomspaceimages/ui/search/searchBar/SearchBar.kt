package com.justcircleprod.randomspaceimages.ui.search.searchBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily

@Composable
fun SearchBar(
    searchText: MutableState<String>,
    onSearch: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(id = R.dimen.search_bar_top_space_size))
    ) {
        val (searchBar, searchButton) = createRefs()

        SearchField(
            searchText = searchText,
            onSearch = onSearch,
            modifier = Modifier
                .constrainAs(searchBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )
        SearchButton(
            onSearch,
            modifier = Modifier
                .constrainAs(searchButton) {
                    top.linkTo(searchBar.top)
                    bottom.linkTo(searchBar.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(searchBar.end)
                }
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
        LocalConfiguration.current.screenWidthDp.dp - dimensionResource(id = R.dimen.search_icon_button_summary_width)

    BasicTextField(
        value = searchText.value,
        singleLine = true,
        cursorBrush = SolidColor(colorResource(id = R.color.primary)),
        maxLines = 1,
        textStyle = TextStyle(
            color = colorResource(id = R.color.search_bar_text),
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
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.search_bar_rounded_corner_radius)))
            .background(colorResource(id = R.color.search_bar_background))
            .width(searchBarWidth)
            .padding(vertical = dimensionResource(id = R.dimen.search_bar_vertical_padding))
            .padding(
                horizontal = dimensionResource(id = R.dimen.search_bar_start_padding)
            )


    ) { innerTextField ->
        Box {
            innerTextField()

            Text(
                text = stringResource(id = R.string.search_bar_placeholder),
                color = colorResource(id = R.color.search_bar_placeholder),
                fontFamily = LatoFontFamily,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.alpha(if (searchText.value.isEmpty()) 1f else 0f)
            )
        }
    }
}

@Composable
fun SearchButton(
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = {
            onSearch()
        },
        modifier = modifier
            .width(dimensionResource(id = R.dimen.search_icon_button_width))
            .height(dimensionResource(id = R.dimen.search_icon_button_height))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_search),
            contentDescription = stringResource(id = R.string.search),
            tint = colorResource(id = R.color.search_bar_icon),
            modifier = Modifier.size(dimensionResource(id = R.dimen.search_bar_icon_size))
        )
    }
}