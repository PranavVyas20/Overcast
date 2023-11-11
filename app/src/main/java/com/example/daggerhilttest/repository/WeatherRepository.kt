package com.example.daggerhilttest.repository

import android.util.Log
import com.example.daggerhilttest.constants.Constants
import com.example.daggerhilttest.models.v2.BaseApiResponse
import com.example.daggerhilttest.models.v1.CurrentWeather
import com.example.daggerhilttest.models.v1.HourlyForecast
import com.example.daggerhilttest.models.v2.GeocodingResponseV2
import com.example.daggerhilttest.models.v2.WeatherResponseV2
import com.example.daggerhilttest.remote.WeatherApi
import com.example.daggerhilttest.util.Resource
import com.example.daggerhilttest.util.Resource_v2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepository(private val weatherApi: WeatherApi) : BaseApiResponse() {

    suspend fun getWeather(lat: Double, long: Double): Flow<Resource_v2<WeatherResponseV2>> {
        return flow {
            emit(Resource_v2.Loading())
            emit(
                safeApiCall {
                    weatherApi.getWeatherData(lat, long, Constants.VISUAL_CROSSING_WEATHER_API_KEY)
                }
            )
        }
    }

    suspend fun getLocationFromGeocoding(
        lat: Double,
        long: Double
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

    // old Code below !!
    suspend fun getCurrentWeatherByLatLong(
        lat: Double,
        long: Double
    ): Flow<Resource<CurrentWeather>> {
        return flow {
            emit(Resource(Constants.WeatherApiStatus.STATUS_LOADING))
            try {
                val response =
                    weatherApi.getCurrentWeatherByLatLong(lat, long, Constants.OPEN_WEATHER_API_KEY)
                Log.d("responseWeather", response.toString())
                if (response.code() == 200) {
                    val currentWeatherData = response.body()
                    emit(Resource(Constants.WeatherApiStatus.STATUS_SUCCESS, currentWeatherData))
                } else {
                    emit(
                        Resource(
                            Constants.WeatherApiStatus.STATUS_ERROR,
                            null,
                            response.message()
                        )
                    )
                }
            } catch (e: Exception) {
                // Network error (no internet or some other shit)
                val errorMessage = "Network error: ${e.message}"
                emit(Resource(Constants.WeatherApiStatus.STATUS_ERROR, null, errorMessage))
            }
        }
    }

    suspend fun getHourlyForecastByLatLong(
        lat: Double,
        long: Double
    ): Flow<Resource<HourlyForecast>> {
        return flow {
            emit(Resource(Constants.WeatherApiStatus.STATUS_LOADING))
            val response =
                weatherApi.getHourlyForecastByLatLong(lat, long, Constants.OPEN_WEATHER_API_KEY)
            try {
                if (response.code() == 200) {
                    val hourlyForecastData = response.body()
                    emit(Resource(Constants.WeatherApiStatus.STATUS_SUCCESS, hourlyForecastData))
                } else {
                    emit(
                        Resource(
                            Constants.WeatherApiStatus.STATUS_ERROR,
                            null,
                            response.message()
                        )
                    )
                }
            } catch (e: Exception) {
                // Network error (no internet or some other shit)
                val errorMessage = "Network error: ${e.message}"
                emit(Resource(Constants.WeatherApiStatus.STATUS_ERROR, null, errorMessage))
            }
        }
    }
}
