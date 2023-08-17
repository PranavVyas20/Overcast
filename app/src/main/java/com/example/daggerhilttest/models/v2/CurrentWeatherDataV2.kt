package com.example.daggerhilttest.models.v2

import com.example.daggerhilttest.models.v1.WeatherExtraDetailItem

data class CurrentWeatherDataV2(
    val dateTime: String,
    val dateTimeEpoch: String,
    val temperature: Double,
    val feelsLikeTemp: Double,
    val weatherCondition: String,
    val weatherDetailItems: List<WeatherExtraDetailItem>,
    val icon: String,
    val sunsetSunriseWeatherItems: List<WeatherExtraDetailItem>
)
