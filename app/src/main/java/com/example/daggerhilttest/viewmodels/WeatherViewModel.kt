package com.example.daggerhilttest.viewmodels

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daggerhilttest.constants.Constants
import com.example.daggerhilttest.models.CurrentWeather
import com.example.daggerhilttest.models.CurrentWeatherGraph
import com.example.daggerhilttest.models.HourlyForecastLocal
import com.example.daggerhilttest.repository.WeatherRepository
import com.example.daggerhilttest.util.getMarker
import com.example.daggerhilttest.util.marker
import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatryk.vico.core.entry.FloatEntry
import com.patrykandpatryk.vico.core.marker.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    data class UIState<T : Any>(
        val isLoading: Boolean = true,
        val data: T? = null,
        val error: String = ""
    )

    private val tempList: List<HourlyForecastLocal> = listOf(
        HourlyForecastLocal(0.0, "", ""),
        HourlyForecastLocal(0.0, "", ""),
        HourlyForecastLocal(0.0, "", ""),
        HourlyForecastLocal(0.0, "", ""),
        HourlyForecastLocal(0.0, "", ""),
        HourlyForecastLocal(0.0, "", "")
    )

    private val _currentWeatherState = mutableStateOf<UIState<CurrentWeather>>(UIState())
    val currentWeatherState get() = _currentWeatherState

    private val _todayHourlyForecastState =
        mutableStateOf<UIState<List<HourlyForecastLocal>>>(UIState(data = tempList))
    val todayHourlyForecast get() = _todayHourlyForecastState

    private val _currentWeatherGraphDataState =
        mutableStateOf<UIState<CurrentWeatherGraph>>(UIState())
    val currentWeatherGraph get() = _currentWeatherGraphDataState

    private val producer = ChartEntryModelProducer()
    private var timeStampMap: MutableMap<Float, Float> = mutableMapOf()
    private val markerMap: MutableMap<Float, Marker> = mutableMapOf()

    private fun convertTempToCelcius(kTemp: Double): Double {
        val cel = kTemp - 273
        return (cel * 10.0).roundToInt() / 10.0
    }

    private fun getDateFromUnix(unixTime: Long): String {
        val sdf = SimpleDateFormat("MM-dd-yyyy")
        val netDate = Date(("1664564638").toLong() * 1000)
        return (sdf.format(netDate))
    }

    private fun getTimeFromUnix(unixTime: Long): String {
        val sdf = SimpleDateFormat("h.mm aa")
        val netDate = Date(unixTime * 1000)
        val dateInString = sdf.format(netDate)
        val dateInFloat = dateInString.dropLast(3).toFloat()
        return dateInString
    }

    private fun getTimeFromUnixHrs(unixTime: Long): String {
        val sdf = SimpleDateFormat("HH.mm")
        val netDate = Date(unixTime * 1000)
        return sdf.format(netDate)
    }

    suspend fun getCurrentWeather(city: String) {
        weatherRepository.getCurrentWeatherByCity(city).onEach { result ->
            when (result.status) {
                Constants.WeatherApiStatus.STATUS_LOADING -> {
                    _currentWeatherState.value = UIState(isLoading = true)
                }
                Constants.WeatherApiStatus.STATUS_SUCCESS -> {
                    _currentWeatherState.value = UIState(isLoading = false, data = result.data)
                    getTodayWeatherForecastByCity(city)
                }
                Constants.WeatherApiStatus.STATUS_ERROR -> {
                    Log.d("currentWeatherStatus", result.errorMessage.toString())
                    _currentWeatherState.value =
                        UIState(isLoading = false, error = result.errorMessage.toString())
                }
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun getTodayWeatherForecastByCity(city: String) {
        weatherRepository.getHourlyForecastByCity(city).onEach { result ->
            when (result.status) {

                Constants.WeatherApiStatus.STATUS_LOADING -> {
                    _todayHourlyForecastState.value = UIState(data = tempList, isLoading = true)
                }
                Constants.WeatherApiStatus.STATUS_SUCCESS -> {
                    val hourlyForecast = mutableListOf<HourlyForecastLocal>()
                    val hourlyForecastGraphPoints = mutableListOf<FloatEntry>()

                    var prevTime = 0f

                    for (i in 0..7) {
                        val currentWeatherItem = result.data!!.forecastList!![i]

                        val hourlyForecastItem = HourlyForecastLocal(
                            temp = convertTempToCelcius(currentWeatherItem.mainTempData!!.temp!!),
                            iconUrl = "${Constants.BASE_ICON_URL}${
                                currentWeatherItem.weatherList?.get(
                                    0
                                )?.icon
                            }@4x.png",
                            timeString = getTimeFromUnix(currentWeatherItem.unixTime!!),
                            timeFloatHrs = getTimeFromUnixHrs(currentWeatherItem.unixTime).toFloat()
                        )
                        hourlyForecast.add(hourlyForecastItem)

                        val xCoordinate = if (hourlyForecastItem.timeFloatHrs!! < prevTime) {
                            (hourlyForecastItem.timeFloatHrs + 24.0)
                        } else {
                            hourlyForecastItem.timeFloatHrs
                        }

                        prevTime = xCoordinate.toInt().toFloat()
                        val hourlyForecastGraphItem = FloatEntry(
                            x = xCoordinate.toInt().toFloat(),
                            y = hourlyForecastItem.temp!!.toFloat()
                        )
                        // 2f pe map hai 2.3f
                        timeStampMap[hourlyForecastGraphItem.x] = xCoordinate.toFloat()

                        hourlyForecastGraphPoints.add(hourlyForecastGraphItem)
                    }
                    _todayHourlyForecastState.value =
                        UIState(isLoading = false, data = hourlyForecast)
                    producer.setEntries(hourlyForecastGraphPoints)
                    _currentWeatherGraphDataState.value = UIState(
                        isLoading = false,
                        data = CurrentWeatherGraph(producer, timeStampMap, markerMap)
                    )
                }
                Constants.WeatherApiStatus.STATUS_ERROR -> {
                    Log.d("weatherForecastStatus", result.errorMessage.toString())
                    _todayHourlyForecastState.value = UIState(
                        isLoading = false,
                        data = null,
                        error = result.errorMessage.toString()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}