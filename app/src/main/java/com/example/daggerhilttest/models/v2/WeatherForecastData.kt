package com.example.daggerhilttest.models.v2

data class WeatherForecastData(
    val temp: Double,
    val minTemp: Double,
    val maxTemp: Double,
    val hourlyForecastData: List<HourlyForecastDataV2>,
    val icon: String,
    val dateTime: String
)
