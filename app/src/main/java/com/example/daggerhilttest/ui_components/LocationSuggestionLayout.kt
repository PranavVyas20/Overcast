package com.example.daggerhilttest.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.daggerhilttest.models.PlaceSuggestion
import com.example.daggerhilttest.ui.theme.graphLineColor

@Composable
fun LocationSuggestionLayout(
    searchBarVisibilityState: MutableState<Boolean>,
    locationSuggestionsVisibilityState: MutableState<Boolean>,
    searchQuery: MutableState<String>,
    locationSuggestions: List<PlaceSuggestion>,
    onLocationSuggestionItemClick: (String) -> Unit
) {
    Column(
        Modifier
            .background(Color.White)
            .padding(start = 10.dp, end = 10.dp)
    ) {
        locationSuggestions.forEach {
            LocationSuggestionItem(
                location = it,
                searchQuery = searchQuery,
                onLocationSuggestionItemClick = onLocationSuggestionItemClick,
                searchBarVisibilityState = searchBarVisibilityState,
                locationSuggestionsVisibilityState = locationSuggestionsVisibilityState
            )
        }
    }
}

@Composable
fun LocationSuggestionItem(
    searchBarVisibilityState: MutableState<Boolean>,
    locationSuggestionsVisibilityState: MutableState<Boolean>,
    location: PlaceSuggestion,
    searchQuery: MutableState<String>,
    onLocationSuggestionItemClick: (String) -> Unit
) {
    Column(modifier = Modifier.clickable {
        searchBarVisibilityState.value = false
        locationSuggestionsVisibilityState.value = false
        onLocationSuggestionItemClick(location.placeId)
        searchQuery.value = ""
    }, verticalArrangement = Arrangement.SpaceEvenly) {
        Text(text = location.place, modifier = Modifier.padding(10.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray)
        )
    }
}