package com.example.daggerhilttest.models

import com.google.gson.annotations.SerializedName

data class HourlyForecast(
     @SerializedName("list") val forecastList:List<CurrentWeather>? = null
)
