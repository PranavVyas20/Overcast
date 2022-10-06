package com.example.daggerhilttest

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.example.daggerhilttest.constants.Constants.USER_PREFERENCES_NAME
import com.example.daggerhilttest.models.LatLong
import com.example.daggerhilttest.models.Weather
import com.example.daggerhilttest.screens.CurrentWeatherScreen
import com.example.daggerhilttest.ui.theme.DaggerHiltTestTheme
import com.example.daggerhilttest.ui_components.WeatherTest
import com.example.daggerhilttest.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var latLong: LatLong
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
                    // if(!dataStore.hasLatLong) {
                    //      hasLocationAccess = viewmodel.requestForLocation(),
                    //      also save location coord in datastore when access granted
                    // }
                    // Else:
                    CurrentWeatherScreen(weatherViewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Log.d("grtCompose","cmposeable called")
    Text(text = name)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DaggerHiltTestTheme {
        Greeting("Android")
    }
}