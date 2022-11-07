package com.example.daggerhilttest.constants

object Constants {
    const val WEATHER_API_KEY = "32d0d01a465884c72557ddb727101c4f"
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val BASE_ICON_URL = "https://openweathermap.org/img/wn/"
    const val  ICON_URL_SUFFIX = "@4x.png"
    const val USER_PREFERENCES_NAME = "user_preferences"

    object WeatherApiStatus{
        const val STATUS_LOADING = "status_loading"
        const val STATUS_ERROR = "status_error"
        const val STATUS_SUCCESS = "status_success"
    }

}