package com.example.daggerhilttest.models

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("description")
    val description: String,
    @SerializedName("currentConditions")
    val currentWeather: CurrentWeather_v2,
    @SerializedName("days")
    val weatherForecast: List<CurrentWeather_v2>
)
