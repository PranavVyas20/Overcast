package com.example.daggerhilttest.ui_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeAction.Companion.Search
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daggerhilttest.R
import com.example.daggerhilttest.models.PlaceSuggestion
import com.example.daggerhilttest.ui.theme.productSans
import com.example.daggerhilttest.ui.theme.shimmerColor
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun ExpandableSearchBar(
    autoSuggestLocation: (String) -> Unit,
    nonExpandedModifier: Modifier,
    expandedModifier: Modifier,
    primaryModifier: Modifier,
    onLocationSuggestionItemClick: (String) -> Unit,
    placeSuggestions: MutableList<PlaceSuggestion>,
    locationName: String,
    showLocationSuggestionsView: MutableState<Boolean>
) {
    val materialPurplrLight = Color(0xFFF6EDFF)
    val isVisible = remember { mutableStateOf(false) }
    val searchQuery = remember { mutableStateOf("") }

    Column(
        modifier = primaryModifier
    ) {
        AnimatedVisibility(visible = !isVisible.value) {
            Row(
                modifier = nonExpandedModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = locationName,
                    fontSize = 22.sp,
                    color = Color.White,
                    fontFamily = productSans,
                    fontWeight = FontWeight(400)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    isVisible.value = true
//                    showLocationSuggestionsView.value = true
                }) {
                    androidx.compose.material3.Icon(
                        tint = Color.White,
                        imageVector = Icons.Default.Search,
                        contentDescription = ""
                    )
                }
            }
        }

        AnimatedVisibility(visible = isVisible.value) {
            MaterialSearchBar(
                autoSuggestLocation = autoSuggestLocation,
                searchQuery = searchQuery,
                isVisible = isVisible,
                placesSuggestion = placeSuggestions,
                showLocationSuggestionsView = showLocationSuggestionsView,
                modifier = expandedModifier
            )
        }

        AnimatedVisibility(visible = showLocationSuggestionsView.value) {
            LocationSuggestionLayout(
                searchQuery = searchQuery,
                locationSuggestions = placeSuggestions,
                onLocationSuggestionItemClick = onLocationSuggestionItemClick,
                locationSuggestionsVisibilityState = showLocationSuggestionsView,
                searchBarVisibilityState = isVisible
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MaterialSearchBar(
    modifier: Modifier,
    autoSuggestLocation: (String) -> Unit,
    searchQuery: MutableState<String>,
    isVisible: MutableState<Boolean>,
    placesSuggestion: MutableList<PlaceSuggestion>,
    showLocationSuggestionsView: MutableState<Boolean>

) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box() {
        TextField(
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
                autoSuggestLocation(it)
            },
            placeholder = { Text("Search") },
            trailingIcon = {
                IconButton(onClick = {
                    if (searchQuery.value.isNotEmpty()) {
                        searchQuery.value = ""
                    } else {
                        isVisible.value = false
                    }
                    showLocationSuggestionsView.value = false
                    placesSuggestion.clear()
                }) {
                    Icon(
                        imageVector = Icons.Default.Close, contentDescription = ""
                    )
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                if (searchQuery.value.isNotEmpty()) {
                    keyboardController?.hide()
                    isVisible.value = false
                    searchQuery.value = ""
                }
            }),
            modifier = modifier,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Gray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Gray
            )
        )
    }

}

@Preview
@Composable
fun ExpandableSearchBarPreview() {
    Surface() {
        ExpandableSearchBar(
            primaryModifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
                .placeholder(
                    visible = false,
                    color = shimmerColor,
                    shape = RoundedCornerShape(8.dp),
                    highlight = PlaceholderHighlight.shimmer(
                        highlightColor = Color.White,
                    )
                ),
            autoSuggestLocation = {},
            nonExpandedModifier = Modifier.padding(vertical = 8.dp),
            expandedModifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(color = Color(0xFFEBDEFF), shape = RoundedCornerShape(26.dp))
                .clip(RoundedCornerShape(26.dp)),
            onLocationSuggestionItemClick = {},
            placeSuggestions = mutableListOf(),
            locationName = "Jaipur",
            showLocationSuggestionsView = remember { mutableStateOf(value = false) }
        )
    }

}

