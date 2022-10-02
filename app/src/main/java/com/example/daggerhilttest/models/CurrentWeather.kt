package com.example.daggerhilttest.models

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("name") val cityName:String? = "",
    @SerializedName("dt") val unixTime:Long? = 0L,
    @SerializedName("wind") val wind:Wind? = null,
    @SerializedName("main") val mainTempData:MainTemp? = null,
    @SerializedName("weather") val weatherList:List<Weather>? = null,
    @SerializedName("coord") val latLong:LatLong? = null
)
