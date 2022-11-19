package com.example.daggerhilttest.repository

import android.accounts.NetworkErrorException
import android.util.Log
import com.example.daggerhilttest.constants.Constants
import com.example.daggerhilttest.models.CurrentWeather
import com.example.daggerhilttest.models.HourlyForecast
import com.example.daggerhilttest.remote.WeatherApi
import com.example.daggerhilttest.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response

class WeatherRepository (private val weatherApi: WeatherApi) {
     suspend fun getCurrentWeatherByCity(city: String): Flow<Resource<CurrentWeather>> {
         return flow{
             emit(Resource(Constants.WeatherApiStatus.STATUS_LOADING))
             try {
                 val response = weatherApi.getCurrentWeatherByCity(city,Constants.WEATHER_API_KEY)
                 Log.d("responseWeather", response.toString())
                 if(response.code() == 200) {
                     val currentWeatherData = response.body()
                     emit(Resource(Constants.WeatherApiStatus.STATUS_SUCCESS,currentWeatherData))
                 } else {
                     emit(Resource(Constants.WeatherApiStatus.STATUS_ERROR,null,response.message()))
                 }
             }catch (e:Exception) {
                 // Network error (no internet or some other shit)
                 val errorMessage = "Network error: ${e.message}"
                 emit(Resource(Constants.WeatherApiStatus.STATUS_ERROR, null, errorMessage))
             }
         }
     }

    suspend fun getCurrentWeatherByLatLong(lat: Double, long: Double): Flow<Resource<CurrentWeather>> {
        return flow{
            emit(Resource(Constants.WeatherApiStatus.STATUS_LOADING))
            try {
                val response = weatherApi.getCurrentWeatherByLatLong(lat, long, Constants.WEATHER_API_KEY)
                Log.d("responseWeather", response.toString())
                if(response.code() == 200) {
                    val currentWeatherData = response.body()
                    emit(Resource(Constants.WeatherApiStatus.STATUS_SUCCESS,currentWeatherData))
                } else {
                    emit(Resource(Constants.WeatherApiStatus.STATUS_ERROR,null,response.message()))
                }
            }catch (e:Exception) {
                // Network error (no internet or some other shit)
                val errorMessage = "Network error: ${e.message}"
                emit(Resource(Constants.WeatherApiStatus.STATUS_ERROR, null, errorMessage))
            }
        }
    }

    suspend fun getHourlyForecastByCity(city: String): Flow<Resource<HourlyForecast>> {
        return flow {
            emit(Resource(Constants.WeatherApiStatus.STATUS_LOADING))
            val response = weatherApi.getHourlyForecastByCity(city,Constants.WEATHER_API_KEY)
            try {
                if(response.code() == 200) {
                    val hourlyForecastData = response.body()
                    emit(Resource(Constants.WeatherApiStatus.STATUS_SUCCESS,hourlyForecastData))
                } else {
                    emit(Resource(Constants.WeatherApiStatus.STATUS_ERROR,null,response.message()))
                }
            }catch (e:Exception) {
                // Network error (no internet or some other shit)
                val errorMessage = "Network error: ${e.message}"
                emit(Resource(Constants.WeatherApiStatus.STATUS_ERROR, null, errorMessage))
            }
        }
    }

    suspend fun getHourlyForecastByLatLong(lat: Double, long: Double): Flow<Resource<HourlyForecast>> {
        return flow {
            emit(Resource(Constants.WeatherApiStatus.STATUS_LOADING))
            val response = weatherApi.getHourlyForecastByLatLong(lat, long, Constants.WEATHER_API_KEY)
            try {
                if(response.code() == 200) {
                    val hourlyForecastData = response.body()
                    emit(Resource(Constants.WeatherApiStatus.STATUS_SUCCESS,hourlyForecastData))
                } else {
                    emit(Resource(Constants.WeatherApiStatus.STATUS_ERROR,null,response.message()))
                }
            }catch (e:Exception) {
                // Network error (no internet or some other shit)
                val errorMessage = "Network error: ${e.message}"
                emit(Resource(Constants.WeatherApiStatus.STATUS_ERROR, null, errorMessage))
            }
        }
    }
}
