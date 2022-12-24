package com.example.daggerhilttest.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.birjuvachhani.locus.Locus
import com.example.daggerhilttest.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationSplashActivity : AppCompatActivity() {
    private val TAG = "activity_lifecycle"
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel
        CoroutineScope(Dispatchers.IO).launch {
            weatherViewModel.getLatLongFromDataStorePref().let {
                if (it != null) {
                    startWeatherActivity()
                } else {
                    getLocation()
                }
            }
        }
    }

    private fun getLocation() {
        Locus.getCurrentLocation(this) { result ->
            result.location?.let {
                weatherViewModel.saveLatLongInDataStorePref(it.latitude, it.longitude)
                startWeatherActivity()
            }
            result.error?.let {  }
        }
    }

    private fun startWeatherActivity() {
        val intent = Intent(this@LocationSplashActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

}
