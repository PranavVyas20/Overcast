package com.example.daggerhilttest.remote

import com.example.daggerhilttest.models.v1.CurrentWeather
import com.example.daggerhilttest.models.v1.HourlyForecast
import com.example.daggerhilttest.models.v2.WeatherResponseV2
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {

    @GET("VisualCrossingWebServices/rest/services/timeline/{latitude},{longitude}")
    suspend fun getWeatherData(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double,
        @Query("key") apiKey: String,
    ): Response<WeatherResponseV2>

    @GET("weather")
    suspend fun getCurrentWeatherByLatLong(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") apiKey: String
    ): Response<CurrentWeather>

    @GET("forecast")
    suspend fun getHourlyForecastByLatLong(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") apiKey: String
    ): Response<HourlyForecast>
}