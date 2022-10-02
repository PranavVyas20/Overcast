package com.example.daggerhilttest.models

import com.google.gson.annotations.SerializedName

data class LatLong(
    @SerializedName("lat") val lat: Double? = 0.0,
    @SerializedName("lon") val long: Double? = 0.0
)
