package com.example.daggerhilttest.models

import com.example.daggerhilttest.R

data class HourlyForecastTest(
    val isCurrentWeather:Boolean = false,
    val time:String = "15:32",
    val icon:Int = R.drawable.partly_cloudy,
    val temperature:String = "29",
)
