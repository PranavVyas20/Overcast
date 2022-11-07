package com.example.daggerhilttest.ui_components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction.Companion.Search
import androidx.compose.ui.unit.dp
import com.example.daggerhilttest.models.PlaceSuggestion
import com.example.daggerhilttest.ui.theme.shimmerColor
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExpandableSearchBar(
    autoSuggestLocation: (String) -> Unit,
    onLocationSuggestionItemClick: (String) -> Unit,
    placeSuggestions: MutableList<PlaceSuggestion>,
    locationName: String,
    placeHolderVisibility: Boolean,
    showLocationSuggestionsView: MutableState<Boolean>
) {
    val isVisible = remember { mutableStateOf(false) }
    val searchQuery = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
            .placeholder(
                visible = placeHolderVisibility,
                color = shimmerColor,
                shape = RoundedCornerShape(8.dp),
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = Color.White,
                )
            )
    ) {
        AnimatedVisibility(visible = !isVisible.value) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = locationName, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    isVisible.value = true
                    showLocationSuggestionsView.value = true
                }) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Search, contentDescription = ""
                    )
                }
            }
        }
        AnimatedVisibility(visible = isVisible.value) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchQuery.value,
                placeholder = { Text(text = "Search Location") },
                onValueChange = {
                    searchQuery.value = it
                    autoSuggestLocation(it)
                },
                trailingIcon = {
                    IconButton(onClick = {
                        if (searchQuery.value.isNotEmpty()) {
                            searchQuery.value = ""
                        } else {
                            isVisible.value = false
                        }
//                        searchQuery.value = ""
                        showLocationSuggestionsView.value = false
                        placeSuggestions.clear()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close, contentDescription = ""
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = Search
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    if (searchQuery.value.isNotEmpty()) {
                        keyboardController?.hide()
                        isVisible.value = false
                        searchQuery.value = ""
                    }
                })
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

