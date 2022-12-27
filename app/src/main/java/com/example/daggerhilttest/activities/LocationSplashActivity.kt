package com.example.daggerhilttest.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewModelScope
import com.birjuvachhani.locus.Locus
import com.example.daggerhilttest.PreferencesKeys
import com.example.daggerhilttest.R
import com.example.daggerhilttest.models.LatLong
import com.example.daggerhilttest.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class LocationSplashActivity : AppCompatActivity() {
    private val TAG = "activity_lifecycle"
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var savedLatLong: LatLong

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_splash)
        onStartUp()
    }

    private fun onStartUp() {
        CoroutineScope(Dispatchers.Main).launch {
            weatherViewModel.getLatLongFromDataStorePref().let {Latlong ->
                if (Latlong != null) {
                    startWeatherActivity(Latlong)
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
                savedLatLong = LatLong(it.latitude, it.longitude)
                startWeatherActivity(savedLatLong)
            }
            Toast.makeText(this, "Location Fetched", Toast.LENGTH_SHORT).show()
            result.error?.let {  }
        }
    }

    private fun startWeatherActivity(savedLatLong: LatLong) {
        val intent = Intent(this@LocationSplashActivity, MainActivity::class.java)
        intent.putExtra("saved_lat", savedLatLong.lat)
        intent.putExtra("saved_long", savedLatLong.long)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

}
