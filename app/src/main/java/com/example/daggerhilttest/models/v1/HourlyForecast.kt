package com.example.daggerhilttest.models.v1

import com.google.gson.annotations.SerializedName

data class HourlyForecast(
     @SerializedName("list") val forecastList:List<CurrentWeather>? = null
)
