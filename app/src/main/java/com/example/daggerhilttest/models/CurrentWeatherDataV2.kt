package com.example.daggerhilttest.models

data class CurrentWeatherDataV2(
    val dateTime: String,
    val temperature: Double,
    val feelsLikeTemp: Double,
    val weatherCondition: String,
    val weatherDetailItems: List<WeatherExtraDetailItem>,
    val icon: String,
    val sunsetSunriseWeatherItems: List<WeatherExtraDetailItem>
)
