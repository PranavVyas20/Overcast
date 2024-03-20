package com.example.daggerhilttest.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daggerhilttest.models.PlaceSuggestion
import com.example.daggerhilttest.models.v2.GeocodingDataV2
import com.example.daggerhilttest.models.v2.WeatherDataV2
import com.example.daggerhilttest.models.v2.toGeocodingData
import com.example.daggerhilttest.ui.theme.purpleBgColor
import com.example.daggerhilttest.ui_components.NetworkErrorLayout
import com.example.daggerhilttest.viewmodels.WeatherViewModel

@Composable
fun CurrentWeatherScreenV2(weatherViewModel: WeatherViewModel, currentLat: Float, currentLong: Float) {
    val currentLocationState =
        weatherViewModel.currentLocationState.collectAsStateWithLifecycle().value
    val currentWeatherState =
        weatherViewModel.currentWeatherStateV3.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = Unit) {
        Log.d("location_tag", "lat = $currentLat, long = $currentLong")
        weatherViewModel.getWeather(currentLat, currentLong)
        weatherViewModel.getLocationFromGeocoding(currentLat, currentLong)
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
            NetworkErrorLayout(26.3f, 75.9f, onRetry = weatherViewModel::getWeather)
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
                dayForecast = currentWeatherData.graphPoints.take(7),
                twoWeeksForecastData = currentWeatherData.weatherForecast
            )
        }) {

    }
}
