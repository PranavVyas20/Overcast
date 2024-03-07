package com.example.daggerhilttest.models.v2

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.outlined.NightsStay
import com.example.daggerhilttest.constants.Constants
import com.example.daggerhilttest.models.v1.WeatherExtraDetailItem
import com.example.daggerhilttest.util.WeatherExtraDetailType
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.round
import kotlin.math.roundToInt

data class CurrentWeatherResponseV2(
    @SerializedName("datetime")
    val dateTime: String?,
    @SerializedName("datetimeEpoch")
    val dateTimeEpoch: Long?,
    @SerializedName("hours")
    val hours: List<HourlyForecastResponseV2>?,
    @SerializedName("temp")
    val temperature: Float?,
    @SerializedName("tempmin")
    val tempMin: Float?,
    @SerializedName("tempmax")
    val tempMax: Float?,
    @SerializedName("feelslike")
    val feelsLikeTemp: Float?,
    @SerializedName("humidity")
    val humidity: Float?,
    @SerializedName("windspeed")
    val windSpeed: Float?,
    @SerializedName("pressure")
    val pressure: Float?,
    @SerializedName("uvindex")
    val uvIndex: Float?,
    @SerializedName("visibility")
    val visibility: Float?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("conditions")
    val weatherCondition: String?,
    @SerializedName("sunrise")
    val sunrise: String?,
    @SerializedName("sunset")
    val sunset: String?,
)

private fun convertTempToCelsius(kFarenheit: Float): Float {
    val cel = ((kFarenheit - 32) * 5) / 9
    return round(cel)
}

private fun convertTo12HourFormat(
    timeString: String,
): String {
    val timeParts = timeString.split(":")
    val hours = timeParts[0].toInt()
    val minutes = timeParts[1]

    val amOrPm = if (hours >= 12) "PM" else "AM"

    var hours12Format = if (hours % 12 == 0) 12 else hours % 12
    if (hours12Format < 10) {
        hours12Format = "0$hours12Format".toInt()
    }
    return "$hours12Format:$minutes $amOrPm"
}

fun epochSecondsToDayString(epochSeconds: Long): String {
    val instant = Instant.ofEpochSecond(epochSeconds)
    val zoneId = ZoneId.systemDefault()
    val dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE")
    return instant.atZone(zoneId).format(dateTimeFormatter).take(3)
}

fun convertEpochTimeToFormattedDateTime(epochSeconds: Long): String {
    val sdf = SimpleDateFormat("MMMM dd, h:mm a", Locale.ENGLISH)
    val date = Date(epochSeconds * 1000)
    return sdf.format(date)
}

fun convertEpochToFormattedTime(epochSeconds: Long): String {
    val sdf = SimpleDateFormat("ha", Locale.ENGLISH)
    val date = Date(epochSeconds * 1000) // Convert seconds to milliseconds
    return sdf.format(date)
}

private fun getIconUrl(icon: String): String {
    return "${Constants.VISUAL_CROSSING_BASE_WEATHER_ICON_URL}$icon${Constants.VISUAL_CROSSING_WEATHER_ICON_URL_SUFFIX}"
}

private fun HourlyForecastResponseV2.toHourlyForecastDataV2() =
    HourlyForecastDataV2(
        temp = temp?.let { convertTempToCelsius(it) } ?: 0f,
        formattedTime = dateTimeEpoch?.let { convertEpochToFormattedTime(it) } ?: "na",
        icon = icon?.let { getIconUrl(it) } ?: ""
    )

private fun List<HourlyForecastResponseV2>.toHourlyForecastListData(): List<HourlyForecastDataV2> {
    return this.map { it.toHourlyForecastDataV2() }
}

private fun CurrentWeatherResponseV2.toWeatherForecastData() =
    WeatherForecastData(
        temp = temperature?.let { convertTempToCelsius(it) } ?: 0f,
        minTemp = tempMin?.let { convertTempToCelsius(it) } ?: 0f,
        maxTemp = tempMax?.let { convertTempToCelsius(it) } ?: 0f,
        dateTime = dateTimeEpoch?.let { convertEpochTimeToFormattedDateTime(it) } ?: "",
        hourlyForecastData = hours?.toHourlyForecastListData() ?: listOf(),
        icon = icon?.let { getIconUrl(it) } ?: "",
        day = dateTimeEpoch?.let { epochSecondsToDayString(it) } ?: ""
    )

private fun CurrentWeatherResponseV2.toGraphPoints() = GraphPoints(
    temp = temperature?.let { convertTempToCelsius(it) } ?: 0f,
    day = dateTimeEpoch?.let { epochSecondsToDayString(it) } ?: ""
)

fun List<CurrentWeatherResponseV2>.toGraphPointsList() : List<GraphPoints> {
    return this.map {
        it.toGraphPoints()
    }
}
fun List<CurrentWeatherResponseV2>.toWeatherForecastListData(): List<WeatherForecastData> {
    return this.map {
        it.toWeatherForecastData()
    }
}

fun CurrentWeatherResponseV2.toCurrentWeatherData(): CurrentWeatherDataV2 =
    CurrentWeatherDataV2(
        dateTime = dateTimeEpoch?.let { convertEpochTimeToFormattedDateTime(it) } ?: "",
        dateTimeEpoch = dateTimeEpoch?.let { convertEpochTimeToFormattedDateTime(it) } ?: "",
        feelsLikeTemp = feelsLikeTemp?.let { convertTempToCelsius(it) } ?: 0f,
        temperature = temperature?.let { convertTempToCelsius(it) } ?: 0f,
        icon = icon?.let { getIconUrl(it) } ?: "",
        weatherCondition = weatherCondition ?: "",
        weatherDetailItems = this.toWeatherDetailItems(false),
        sunsetSunriseWeatherItems = this.toWeatherDetailItems(true)
    )

private fun CurrentWeatherResponseV2.toWeatherDetailItems(includeSunsetSunrise: Boolean): List<WeatherExtraDetailItem> {
    val weatherItems = if (includeSunsetSunrise) {
        listOf(
            WeatherExtraDetailItem(
                type = WeatherExtraDetailType.SUNSET,
                title = "Sunset",
                value = convertTo12HourFormat(sunset.toString()),
                icon = Icons.Outlined.NightsStay
            ),
            WeatherExtraDetailItem(
                type = WeatherExtraDetailType.SUNRISE,
                title = "Sunrise",
                value = convertTo12HourFormat(sunrise.toString()),
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
