package com.example.daggerhilttest.models

import com.google.gson.annotations.SerializedName

data class LatLong(
    @SerializedName("lat") var lat: Double? = 0.0,
    @SerializedName("lon") var long: Double? = 0.0
)
