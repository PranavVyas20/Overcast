package com.example.daggerhilttest.constants

object Constants {
    const val OPEN_WEATHER_API_KEY = "32d0d01a465884c72557ddb727101c4f"
    const val OPEN_WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val OPEN_WEATHER_BASE_GEOCODING_URL = "https://api.openweathermap.org/geo/1.0/reverse"
    const val VISUAL_CROSSING_WEATHER_API_KEY= "MXUPKH5T9B3ELZEEFR3BF9WPR"
    const val VISUAL_CROSSING_WEATHER_BASE_URL = "https://weather.visualcrossing.com/"
    const val OPEN_WEATHER_BASE_ICON_URL = "https://openweathermap.org/img/wn/"
    const val VISUAL_CROSSING_BASE_WEATHER_ICON_URL = "https://raw.githubusercontent.com/PranavVyas20/Weather-Icons/ce918eb7cdcd1a3a3ba27e7c0677cb598ccab3bc/icons/"
    const val VISUAL_CROSSING_WEATHER_ICON_URL_SUFFIX = ".svg"
    const val OPEN_WEATHER_ICON_URL_SUFFIX = "@4x.png"
    const val USER_PREFERENCES_NAME = "user_preferences"

    object WeatherApiStatus{
        const val STATUS_LOADING = "status_loading"
        const val STATUS_ERROR = "status_error"
        const val STATUS_SUCCESS = "status_success"
    }

}