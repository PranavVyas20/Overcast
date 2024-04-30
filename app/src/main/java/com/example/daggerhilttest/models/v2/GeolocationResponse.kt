package com.example.daggerhilttest.models.v2

import com.google.gson.annotations.SerializedName

data class GeolocationResponse(
    @SerializedName("location")
    val location: Geolocation? = null
)
 data class Geolocation(
     @SerializedName("lat")
     val geoLat: Float? = null,
     @SerializedName("lng")
     val geoLng: Float? = null
 )
