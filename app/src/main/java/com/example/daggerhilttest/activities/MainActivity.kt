package com.example.daggerhilttest.activities

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.daggerhilttest.models.LatLong
import com.example.daggerhilttest.screens.CurrentWeatherScreen
import com.example.daggerhilttest.ui.theme.DaggerHiltTestTheme
import com.example.daggerhilttest.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val savedLat = intent.getDoubleExtra("saved_lat", 0.0)
        val savedLong = intent.getDoubleExtra("saved_long", 0.0)
        setContent {
            DaggerHiltTestTheme {
                val weatherViewModel: WeatherViewModel by viewModels()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F6F8)
                ) {
                    val savedLatLog = LatLong(savedLat, savedLong)
                    CurrentWeatherScreen(weatherViewModel = weatherViewModel)
                }
            }
        }
    }
}
