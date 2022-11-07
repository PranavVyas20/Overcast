package com.example.daggerhilttest.activities

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.daggerhilttest.screens.CurrentWeatherScreen
import com.example.daggerhilttest.ui.theme.DaggerHiltTestTheme
import com.example.daggerhilttest.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DaggerHiltTestTheme {
                val weatherViewModel = hiltViewModel<WeatherViewModel>()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F6F8)
                ) {
                    CurrentWeatherScreen(weatherViewModel = weatherViewModel)
                }
            }
        }
    }
}
