package com.example.daggerhilttest.models.v2

data class WeatherDataV2(
    val currentWeather: CurrentWeatherDataV2,
    val weatherForecast: List<WeatherForecastData>,
    val graphPoints: List<GraphPoints>
)
