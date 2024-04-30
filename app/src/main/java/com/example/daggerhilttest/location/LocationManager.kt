package com.example.daggerhilttest.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationManager
@Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(applicationContext)

    @SuppressLint("MissingPermission")
    suspend fun getLastKnownLocation() = runCatching {
        fusedLocationClient.lastLocation.await()
    }
}