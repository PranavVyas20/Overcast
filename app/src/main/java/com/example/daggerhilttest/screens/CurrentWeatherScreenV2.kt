package com.example.daggerhilttest.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldDefaults
import androidx.compose.material.BackdropValue
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.isPopupLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daggerhilttest.models.PlaceSuggestion
import com.example.daggerhilttest.models.v2.GeocodingDataV2
import com.example.daggerhilttest.models.v2.WeatherDataV2
import com.example.daggerhilttest.models.v2.toGeocodingData
import com.example.daggerhilttest.ui.theme.purpleBgColor
import com.example.daggerhilttest.ui_components.NetworkErrorLayout
import com.example.daggerhilttest.viewmodels.WeatherViewModel

@Composable
fun CurrentWeatherScreenV2(weatherViewModel: WeatherViewModel) {
    val currentLocationState =
        weatherViewModel.currentLocationState.collectAsStateWithLifecycle().value
    val currentWeatherState =
        weatherViewModel.currentWeatherStateV3.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = Unit) {
        weatherViewModel.getWeather(26.3609, 75.9289)
        weatherViewModel.getLocationFromGeocoding(26.3609, 75.9289)
    }

    when (currentWeatherState) {
        is WeatherViewModel.UIState.Success -> {
            CurrentWeatherScreenContent(
                currentWeatherData = (currentWeatherState.data),
                getAutoCompleteSuggestion = {
                    weatherViewModel.tryAutoComplete(it)
                },
                getLatLongFromPlaceId = { weatherViewModel.getLatLongByPlaceId(it) },
                placesSuggestion = weatherViewModel.placeSuggestions,
                currentLocationData = when (currentLocationState) {
                    is WeatherViewModel.UIState.Loading -> {
                        GeocodingDataV2("Loading")
                    }

                    is WeatherViewModel.UIState.Error -> {
                        GeocodingDataV2("Not Available")
                    }

                    is WeatherViewModel.UIState.Success -> {
                        currentLocationState.data.toGeocodingData()
                    }
                }
            )
        }

        is WeatherViewModel.UIState.Error -> {
            NetworkErrorLayout(26.3609, 75.9289, onRetry = weatherViewModel::getWeather)
        }

        is WeatherViewModel.UIState.Loading -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrentWeatherScreenContent(
    currentWeatherData: WeatherDataV2,
    placesSuggestion: List<PlaceSuggestion>,
    currentLocationData: GeocodingDataV2,
    getAutoCompleteSuggestion: (String) -> Unit,
    getLatLongFromPlaceId: (String) -> Unit
) {

    val scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Revealed)
    BackdropScaffold(
        frontLayerScrimColor = Color.Unspecified,
        frontLayerBackgroundColor = purpleBgColor,
        appBar = {},
        backLayerBackgroundColor = Color(0xFF101e37),
        scaffoldState = scaffoldState,
        peekHeight = 0.dp,
        backLayerContent = {
            BottomSheetScaffoldContent(
                location = currentLocationData.locationName,
                temp = currentWeatherData.currentWeather.temperature,
                weatherCondition = currentWeatherData.currentWeather.weatherCondition,
                icon = currentWeatherData.currentWeather.icon,
                time = currentWeatherData.currentWeather.dateTime,
                feelsLikeTemp = currentWeatherData.currentWeather.feelsLikeTemp,
                placesSuggestions = placesSuggestion,
                getAutoCompleteSuggestion = getAutoCompleteSuggestion,
                getLatLongFromPlaceId = getLatLongFromPlaceId
            )
        },
        frontLayerContent = {
            CurrentWeatherScreenBottomSheetContent(
                currentWeather = currentWeatherData.currentWeather,
                hourlyForecast = currentWeatherData.weatherForecast.first().hourlyForecastData,
                dayForecast = currentWeatherData.weatherForecast
            )
        }) {

    }
}
