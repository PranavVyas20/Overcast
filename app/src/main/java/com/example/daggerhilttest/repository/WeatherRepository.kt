package com.example.daggerhilttest.repository

import com.example.daggerhilttest.constants.Constants
import com.example.daggerhilttest.models.CurrentWeather
import com.example.daggerhilttest.models.HourlyForecast
import com.example.daggerhilttest.remote.WeatherApi
import com.example.daggerhilttest.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class WeatherRepository(private val weatherApi: WeatherApi) {
     suspend fun getCurrentWeatherByCity(city: String): Flow<Resource<CurrentWeather>> {
         return flow{
             emit(Resource(Constants.WeatherApiStatus.STATUS_LOADING))
             val response = weatherApi.getCurrentWeatherByCity(city,Constants.WEATHER_API_KEY)
             if(response.code() == 200) {
                 val currentWeatherData = response.body()
                 emit(Resource(Constants.WeatherApiStatus.STATUS_SUCCESS,currentWeatherData))
             } else {
                 emit(Resource(Constants.WeatherApiStatus.STATUS_ERROR,null,response.message()))
             }
         }
     }

    suspend fun getHourlyForecastByCity(city: String): Flow<Resource<HourlyForecast>> {
        return flow {
            emit(Resource(Constants.WeatherApiStatus.STATUS_LOADING))
            val response = weatherApi.getHourlyForecastByCity(city,Constants.WEATHER_API_KEY)
            if(response.code() == 200) {
                val hourlyForecastData = response.body()
                emit(Resource(Constants.WeatherApiStatus.STATUS_SUCCESS,hourlyForecastData))
            } else {
                emit(Resource(Constants.WeatherApiStatus.STATUS_ERROR,null,response.message()))
            }
        }
    }
}
