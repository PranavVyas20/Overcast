package com.example.daggerhilttest.models.v2

data class WeatherForecastData(
    val temp: Float,
    val minTemp: Float,
    val maxTemp: Float,
    val hourlyForecastData: List<HourlyForecastDataV2>,
    val icon: String,
    val dateTime: String,
    val day: String
)
