package com.example.daggerhilttest.ui_components
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableSearchView(
    searchDisplay: String,
    onSearchDisplayChanged: (String) -> Unit,
    onSearchDisplayClosed: () -> Unit,
    modifier: Modifier = Modifier,
    expandedInitially: Boolean = false,
    tint: Color = MaterialTheme.colors.onPrimary
) {
    val (expanded, onExpandedChanged) = remember {
        mutableStateOf(expandedInitially)
    }
    Crossfade(targetState = expanded) { isSearchFieldVisible ->

        when (isSearchFieldVisible) {

            true -> CustomTextField(
                onExpandedChanged = onExpandedChanged,
                onSearchDisplayClosed = onSearchDisplayClosed
            )
            false -> CollapsedSearchView(
                onExpandedChanged = onExpandedChanged,
                modifier = modifier,
                tint = tint
            )
        }
    }
}

@Composable
fun SearchIcon() {
    Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "search icon",
        tint = Color.Black
    )
}

@Composable
fun CollapsedSearchView(
    locationName:String = "Banasthali",
    onExpandedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colors.onPrimary,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = locationName,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(start = 16.dp)
        )
        IconButton(
            onClick = { onExpandedChanged(true) }
        ) {
            SearchIcon()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CustomTextField(
    onSearchDisplayClosed: () -> Unit,
    onExpandedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "Search Location",
    fontSize: TextUnit = MaterialTheme.typography.body2.fontSize
) {
    val trailingIconView = @Composable {
        IconButton(
            onClick = {
                onExpandedChanged(false)
                onSearchDisplayClosed()
            },
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "",
                tint = Color.Black
            )
        }
    }
    var searchBarText by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    BasicTextField(modifier = modifier
        .fillMaxWidth()
        .padding(10.dp)
        .border(1.dp, Color(0xAFD0D0D1), RoundedCornerShape(10.dp))
        .shadow(1.dp, RoundedCornerShape(10.dp))
        .background(Color.White)
        .height(50.dp),
        value = searchBarText,
        onValueChange = {
            searchBarText = it
        },
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colors.primary),
        textStyle = LocalTextStyle.current.copy(
            color = Color.Black,
            fontSize = fontSize
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = androidx.compose.ui.text.input.ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (searchBarText.isNotEmpty()) {

                    keyboardController?.hide()
                    onExpandedChanged(false)
                    onSearchDisplayClosed()
                }
            }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                trailingIconView()
                Box(Modifier.weight(1f)) {
                    if (searchBarText.isEmpty()) {
                        Text(
                            placeholderText,
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Preview
@Composable
fun SearchBarPreview() {

}


