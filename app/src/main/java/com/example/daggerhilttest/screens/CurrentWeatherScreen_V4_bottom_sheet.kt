package com.example.daggerhilttest.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.daggerhilttest.R
import com.example.daggerhilttest.ui.theme.Purple200
import com.example.daggerhilttest.ui.theme.shimmerColor
import com.example.daggerhilttest.ui_components.ExpandableSearchBar
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun CurrentWeatherScreenBottomSheet() {
    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
    val configuration = LocalConfiguration.current

    val screenHeight = remember { configuration.screenHeightDp.dp }

    // Calculate the peek height as 70% of the screen height
    val peekHeight = screenHeight * 0.65f
    var maxHeight by remember { mutableStateOf(screenHeight * 0.92f) }
    val scaffoldState = rememberBottomSheetScaffoldState()

    Column(modifier = Modifier.fillMaxSize()) {
        ExpandableSearchBar(
            primaryModifier = Modifier
                .fillMaxWidth()
                .paint(
                    painterResource(id = R.drawable.weather_searchbar_bg),
                    contentScale = ContentScale.FillBounds
                )
                .padding(start = 16.dp, end = 16.dp)
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
                .padding(top = 10.dp, bottom = 10.dp)
                .background(color = Color(0xFFEBDEFF), shape = RoundedCornerShape(26.dp))
                .clip(RoundedCornerShape(26.dp)),
            onLocationSuggestionItemClick = {},
            placeSuggestions = mutableListOf(),
            locationName = "Jaipur",
            showLocationSuggestionsView = remember { mutableStateOf(false) }
        )

        BottomSheetScaffold(scaffoldState = scaffoldState,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            sheetPeekHeight = peekHeight,
            sheetBackgroundColor = Color.Transparent,
            sheetContent = {
                CurrentWeatherScreen_V4()
            }) { innerPadding ->
            BottomSheetScafoldContent()
        }
    }
}

