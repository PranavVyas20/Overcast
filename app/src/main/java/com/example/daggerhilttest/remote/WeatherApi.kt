package com.example.daggerhilttest.remote

import com.example.daggerhilttest.models.CurrentWeather
import com.example.daggerhilttest.models.HourlyForecast
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeatherByCity(
        @Query("q") city:String,
        @Query("appid") apiKey:String
    ): Response<CurrentWeather>

    @GET("forecast")
    suspend fun getHourlyForecastByCity(
        @Query("q") city:String,
        @Query("appid") apiKey:String
    ): Response<HourlyForecast>

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