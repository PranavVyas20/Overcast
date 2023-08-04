package com.example.daggerhilttest.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.outlined.NightsStay
import com.example.daggerhilttest.util.WeatherExtraDetailType
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponseV2(
    @SerializedName("datetime")
    val dateTime: String?,
    @SerializedName("temp")
    val temperature: Double?,
    @SerializedName("tempmin")
    val tempMin: Double?,
    @SerializedName("tempmax")
    val tempMax: Double?,
    @SerializedName("feelslike")
    val feelsLikeTemp: Double?,
    @SerializedName("humidity")
    val humidity: Double?,
    @SerializedName("windspeed")
    val windSpeed: Double?,
    @SerializedName("pressure")
    val pressure: Double?,
    @SerializedName("uvindex")
    val uvIndex: Double?,
    @SerializedName("visibility")
    val visibility: Double?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("conditions")
    val weatherCondition: String?,
    @SerializedName("sunrise")
    val sunrise: String?,
    @SerializedName("sunset")
    val sunset: String?,
)
private fun CurrentWeatherResponseV2.toWeatherForecastData() =
    WeatherForecastData(
        temp = temperature ?: 0.0,
        minTemp = tempMin ?: 0.0,
        maxTemp = tempMax ?: 0.0,
        dateTime = dateTime ?: "",
        icon = icon ?: ""
    )

fun List<CurrentWeatherResponseV2>.toWeatherForecastListData(): List<WeatherForecastData> {
    return this.map {
        it.toWeatherForecastData()
    }
}
fun CurrentWeatherResponseV2.toCurrentWeatherData() : CurrentWeatherDataV2 =
    CurrentWeatherDataV2(
        dateTime = dateTime.toString(),
        feelsLikeTemp = feelsLikeTemp?:0.0,
        temperature = temperature?:0.0,
        icon = icon?:"",
        weatherCondition = weatherCondition?:"",
        weatherDetailItems = this.toWeatherDetailItems(false),
        sunsetSunriseWeatherItems = this.toWeatherDetailItems(true)
    )

private fun CurrentWeatherResponseV2.toWeatherDetailItems(includeSunsetSunrise: Boolean): List<WeatherExtraDetailItem> {
    val weatherItems = if (includeSunsetSunrise) {
        listOf(
            WeatherExtraDetailItem(
                type = WeatherExtraDetailType.SUNSET,
                title = "Sunset",
                value = sunset.toString(),
                icon = Icons.Outlined.NightsStay
            ),
            WeatherExtraDetailItem(
                type = WeatherExtraDetailType.SUNRISE,
                title = "Sunrise",
                value = sunset.toString(),
                icon = Icons.Default.LightMode
            )
        )
    } else {
        listOf(
            WeatherExtraDetailItem(
                type = WeatherExtraDetailType.HUMIDITY,
                title = "Humidity",
                value = "$humidity %",
                icon = Icons.Default.WaterDrop
            ),
            WeatherExtraDetailItem(
                type = WeatherExtraDetailType.WIND_SPEED,
                title = "Wind speed",
                value = "${windSpeed}km/h",
                icon = Icons.Default.Air
            ),
            WeatherExtraDetailItem(
                type = WeatherExtraDetailType.AIR_PRESSURE,
                title = "Pressure",
                value = "$pressure hpa",
                icon = Icons.Default.Waves
            ),
            WeatherExtraDetailItem(
                type = WeatherExtraDetailType.VISIBILITY,
                title = "Visibility",
                value = visibility.toString(),
                icon = Icons.Default.Visibility
            )
        )
    }
    return weatherItems
}
