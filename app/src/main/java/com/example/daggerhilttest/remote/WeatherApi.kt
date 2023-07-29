package com.example.daggerhilttest.remote

import com.example.daggerhilttest.models.CurrentWeather
import com.example.daggerhilttest.models.HourlyForecast
import com.example.daggerhilttest.models.WeatherResponse
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
    ): Response<WeatherResponse>

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