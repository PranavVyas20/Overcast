package com.example.daggerhilttest.ui_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeAction.Companion.Search
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.daggerhilttest.R
import com.example.daggerhilttest.models.PlaceSuggestion
import com.example.daggerhilttest.ui.theme.productSans
import com.example.daggerhilttest.ui.theme.purpleBgColor
import com.example.daggerhilttest.ui.theme.purpleWeatherItemColor
import com.example.daggerhilttest.ui.theme.shimmerColor
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.patrykandpatryk.vico.core.axis.vertical.VerticalAxis

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
    val isVisible = remember { mutableStateOf(false) }
    val searchQuery = remember { mutableStateOf("") }

    Column(
        modifier = primaryModifier
    ) {
        AnimatedVisibility(modifier = Modifier.wrapContentHeight(), visible = !isVisible.value) {
            Row(
                modifier = nonExpandedModifier, verticalAlignment = Alignment.CenterVertically
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
        TextField(value = searchQuery.value, onValueChange = {
            searchQuery.value = it
            autoSuggestLocation(it)
        }, placeholder = { Text("Search") }, trailingIcon = {
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
        }, leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = "Search Icon"
            )
        }, keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ), keyboardActions = KeyboardActions(onSearch = {
            if (searchQuery.value.isNotEmpty()) {
                keyboardController?.hide()
                isVisible.value = false
                searchQuery.value = ""
            }
        }), modifier = modifier, colors = TextFieldDefaults.textFieldColors(
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
    Surface(color = Color.LightGray) {
        ExpandableSearchBar(primaryModifier = Modifier
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
            nonExpandedModifier = Modifier,
            expandedModifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(color = Color(0xFFEBDEFF), shape = RoundedCornerShape(26.dp))
                .clip(RoundedCornerShape(26.dp)),
            onLocationSuggestionItemClick = {},
            placeSuggestions = mutableListOf(),
            locationName = "Jaipur",
            showLocationSuggestionsView = remember { mutableStateOf(value = false) })
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBarV2(
    modifier: Modifier, onLocationSuggestionItemClick: (String) -> Unit,
    autoSuggestLocation: (String) -> Unit,
    placeSuggestions: MutableList<PlaceSuggestion>,
    locationName: String,
    showLocationSuggestionsView: MutableState<Boolean>
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val searchQuery = remember {
        mutableStateOf("")
    }
    val isExpanded = remember {
        mutableStateOf(false)
    }
    val placeHolderText = remember {
        mutableStateOf("")
    }

    val isFullyExpanded = remember {
        mutableStateOf(false)
    }

    val isFullyCollapsed = remember {
        mutableStateOf(true)
    }
    val configuration = LocalConfiguration.current
    val screenWidth = remember { configuration.screenWidthDp.dp }
    val purpleColor = Color(0xFFEBDEFF)

    val searchBarColor = animateColorAsState(
        animationSpec = tween(
            durationMillis = 250
        ), targetValue = if (isExpanded.value || (!isFullyCollapsed.value && !isExpanded.value)) {
            purpleColor.copy(alpha = 1f)
        } else {
            purpleColor.copy(alpha = 0f)
        }, label = ""
    )
    val boxWidth = animateDpAsState(targetValue = if (isExpanded.value) screenWidth else 65.dp,
        label = "",
        animationSpec = tween(durationMillis = 300),
        finishedListener = { size ->
            if (size == screenWidth) {
                placeHolderText.value = "Search"
                isFullyExpanded.value = true
                isFullyCollapsed.value = false
            } else {
                isFullyExpanded.value = false
                isFullyCollapsed.value = true
            }
        })

    ConstraintLayout(
        modifier = modifier
    ) {
        val (locationName, searchInputField) = createRefs()
        Box(modifier = Modifier
            .animateContentSize()
            .constrainAs(locationName) {
                start.linkTo(parent.start, 18.dp)
                top.linkTo(parent.top)
                centerVerticallyTo(parent)
            }) {
            Text(
                text = "New York", fontSize = 22.sp,
                color = Color.White,
                fontFamily = productSans,
                fontWeight = FontWeight(400),
            )
        }
        TextField(value = searchQuery.value, onValueChange = {
            searchQuery.value = it
        }, placeholder = { Text(placeHolderText.value) }, trailingIcon = {
            if (isFullyExpanded.value) {
                Icon(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        isExpanded.value = false
                        placeHolderText.value = ""
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "close-icon"
                )
            }
        }, leadingIcon = {
            Icon(
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    isExpanded.value = true
                },
                imageVector = Icons.Default.Search,
                contentDescription = "search-icon",
                tint = if (isExpanded.value) Color.Gray else Color.White
            )
        }, keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ), keyboardActions = KeyboardActions(onSearch = {
            if (searchQuery.value.isNotEmpty()) {
                keyboardController?.hide()
                searchQuery.value = ""
            }
        }), modifier = Modifier
            .constrainAs(searchInputField) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            }
            .width(boxWidth.value)
            .padding(horizontal = 8.dp)
            .background(
                color = Color.Transparent, shape = RoundedCornerShape(26.dp)
            )
            .clip(RoundedCornerShape(26.dp)), colors = TextFieldDefaults.textFieldColors(
            backgroundColor = searchBarColor.value,
            textColor = Color.Gray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Gray
        ))
    }
}
