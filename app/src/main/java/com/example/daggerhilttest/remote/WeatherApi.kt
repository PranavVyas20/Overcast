package com.example.daggerhilttest.remote

import com.example.daggerhilttest.models.v1.CurrentWeather
import com.example.daggerhilttest.models.v1.HourlyForecast
import com.example.daggerhilttest.models.v2.GeocodingResponseV2
import com.example.daggerhilttest.models.v2.GeolocationResponse
import com.example.daggerhilttest.models.v2.WeatherResponseV2
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface WeatherApi {

    @GET("VisualCrossingWebServices/rest/services/timeline/{latitude},{longitude}")
    suspend fun getWeatherData(
        @Path("latitude") latitude: Float,
        @Path("longitude") longitude: Float,
        @Query("key") apiKey: String,
    ): WeatherResponseV2

    @GET
    suspend fun getLocationFromGeocoding(
        @Url url: String,
        @Query("lat") lat: Float,
        @Query("lon") long: Float,
        @Query("appid") apiKey: String,
    ): Response<List<GeocodingResponseV2>>

    @POST("https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyCcMTFsvjmOPbHrpeZmCC0LCFJ_3e2jEgs")
    suspend fun getGeolocation(): GeolocationResponse

}