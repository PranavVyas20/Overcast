package com.example.daggerhilttest.activities

import android.annotation.SuppressLint
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.view.ContentInfoCompat.Flags
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.example.daggerhilttest.R
import com.example.daggerhilttest.models.LatLong
import com.example.daggerhilttest.viewmodels.WeatherViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.util.*

@AndroidEntryPoint
class LocationSplashActivity : AppCompatActivity() {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val TAG = "activity_lifecycle"
    private val permissionId = 2
    private val weatherViewModel: WeatherViewModel by viewModels()
    var appPausedForLocation = false
    var onResumeCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("activity_lifecycle", "onCreate")
        setContentView(R.layout.activity_location_splash)
        weatherViewModel
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        CoroutineScope(Dispatchers.Default).launch {
            retrieveAndHandleSavedLocation()
        }
        Log.d(TAG, "after that fun -> onCreate")
    }

    private suspend fun retrieveAndHandleSavedLocation() {
        val savedLocation = CoroutineScope(Dispatchers.IO).async {
            weatherViewModel.getLatLongFromDataStorePref()
        }
        if(savedLocation.await().lat == null && savedLocation.await().long == null) {
            getLocation()
        } else {
            startWeatherActivity()
        }
    }

    override fun onResume() {
        onResumeCount++
        super.onResume()
        Log.d(TAG,onResumeCount.toString())
        if(onResumeCount > 2) {
            if (appPausedForLocation) {
                appPausedForLocation = false
                if(isLocationEnabled()) {
                    fetchCurrentLocation()
                } else {
                    Toast.makeText(this, "Location is needed, please restart app and enable location", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun startWeatherActivity() {
        val intent = Intent(this@LocationSplashActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun startLocationActivity() {
        appPausedForLocation = true
        runOnUiThread {
            Toast.makeText(
                this, "Please turn on location and restart the app", Toast.LENGTH_LONG
            ).show()
        }

        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun fetchCurrentLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = Priority.PRIORITY_HIGH_ACCURACY

        LocationServices.getFusedLocationProviderClient(this@LocationSplashActivity)
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(this@LocationSplashActivity)
                        .removeLocationUpdates(this)
                    if (locationResult.locations.size > 0) {
                        val locationItem = locationResult.locations
                        // Saving lat and long to datastore pref
                        lifecycleScope.launch {
                            weatherViewModel.saveLatLongInDataStorePref(
                                locationItem[0].latitude, locationItem[0].longitude
                            )
                            startWeatherActivity()
                        }
                        Log.d(
                            "locUpdate",
                            "Location updated ${locationItem[0].latitude} ${locationItem[0].longitude} "
                        )
                        Toast.makeText(
                            this@LocationSplashActivity,
                            "location fetched",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@LocationSplashActivity,
                            "error fetching location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }, Looper.getMainLooper())
    }

    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fetchCurrentLocation()
            } else {
                startLocationActivity()
            }
        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ), permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }
}
