package com.example.daggerhilttest.remote

import com.example.daggerhilttest.constants.Constants
import com.example.daggerhilttest.models.v1.CurrentWeather
import com.example.daggerhilttest.models.v1.HourlyForecast
import com.example.daggerhilttest.models.v2.GeocodingResponseV2
import com.example.daggerhilttest.models.v2.WeatherResponseV2
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface WeatherApi {

    @GET("VisualCrossingWebServices/rest/services/timeline/{latitude},{longitude}")
    suspend fun getWeatherData(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double,
        @Query("key") apiKey: String,
    ): Response<WeatherResponseV2>


    @GET
    suspend fun getLocationFromGeocoding(
        @Url url: String,
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") apiKey: String,
    ): Response<List<GeocodingResponseV2>>


    // Old code
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