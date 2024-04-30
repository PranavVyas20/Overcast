package com.example.daggerhilttest.repository

import android.util.Log
import com.example.daggerhilttest.constants.Constants
import com.example.daggerhilttest.models.v2.BaseApiResponse
import com.example.daggerhilttest.models.v2.GeocodingResponseV2
import com.example.daggerhilttest.models.v2.GeolocationResponse
import com.example.daggerhilttest.models.v2.WeatherResponseV2
import com.example.daggerhilttest.remote.WeatherApi
import com.example.daggerhilttest.util.Resource_v2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepository(private val weatherApi: WeatherApi) : BaseApiResponse() {

    suspend fun getWeather(lat: Float, long: Float) : Result<WeatherResponseV2> {
        Log.d("worker_tag", "api call")
        return runCatching{ weatherApi.getWeatherData(lat, long, Constants.VISUAL_CROSSING_WEATHER_API_KEY) }
    }

    suspend fun getGeolocation(): Result<GeolocationResponse> {
        return runCatching { weatherApi.getGeolocation() }
    }

    suspend fun getLocationFromGeocoding(
        lat: Float,
        long: Float
    ): Flow<Resource_v2<List<GeocodingResponseV2>>> {
        return flow {
            emit(Resource_v2.Loading())
            emit(
                safeApiCall {
                    weatherApi.getLocationFromGeocoding(
                        url = Constants.OPEN_WEATHER_BASE_GEOCODING_URL,
                        lat = lat,
                        long = long,
                        apiKey = Constants.OPEN_WEATHER_API_KEY
                    )
                }
            )
        }
    }
}
