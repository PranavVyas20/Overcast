package com.example.daggerhilttest.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
//import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.daggerhilttest.constants.Constants
import com.example.daggerhilttest.models.LatLong
//import com.example.daggerhilttest.screens.CurrentWeatherScreen
import com.example.daggerhilttest.screens.CurrentWeatherScreenV2
import com.example.daggerhilttest.ui.theme.DaggerHiltTestTheme
import com.example.daggerhilttest.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val savedLat = intent.getDoubleExtra("saved_lat", 0.0)
        val savedLong = intent.getDoubleExtra("saved_long", 0.0)
        setContent {
            DaggerHiltTestTheme {
                val weatherViewModel: WeatherViewModel by viewModels()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val savedLatLog = LatLong(savedLat, savedLong)
                    //test commit
//                    val state = weatherViewModel.currentWeatherState_v2
//                    Log.d("current-weather-state", state.value.toString())
//                    LaunchedEffect(key1 = Unit) {
//                        weatherViewModel.getWeather(12.34, 11.657)
//                    }
                    weatherViewModel.currentLocationLatLong = LatLong(savedLat, savedLong)
                    CurrentWeatherScreenV2(weatherViewModel)

                }
            }
        }
    }
}
