package com.example.daggerhilttest.models.v2

import com.google.gson.annotations.SerializedName

data class WeatherResponseV2(
    @SerializedName("description")
    val description: String,
    @SerializedName("currentConditions")
    val currentWeather: CurrentWeatherResponseV2,
    @SerializedName("days")
    val weatherForecast: List<CurrentWeatherResponseV2>
)

fun WeatherResponseV2.toWeatherData() =
    WeatherDataV2(
        currentWeather = this.currentWeather.toCurrentWeatherData(),
        weatherForecast = this.weatherForecast.toWeatherForecastListData(),
        graphPoints = this.weatherForecast.toGraphPointsList()
    )

