package com.example.daggerhilttest.viewmodels


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daggerhilttest.PreferencesKeys
import com.example.daggerhilttest.constants.Constants
import com.example.daggerhilttest.models.*
import com.example.daggerhilttest.repository.WeatherRepository
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*
import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatryk.vico.core.entry.FloatEntry
import com.patrykandpatryk.vico.core.marker.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val dataStorePref: DataStore<Preferences>,
    private val placesClient: PlacesClient
) : ViewModel() {

//    data class UIState<T : Any>(
//        val isLoading: Boolean = true, val data: T? = null, val error: String = ""
//    )
sealed class UIState<T> {

    data class Success<T>(val data: T) : UIState<T>()

    data class Error<T>(val message: String?) : UIState<T>()

    class Loading<T> : UIState<T>()
    data class Initialised<T>(val data: T? = null): UIState<T>()

}

    var currentLocationLatLong: LatLong? = null

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


    // NEW!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    var currentWeatherStateV3 = MutableStateFlow<UIState<CurrentWeather_v2>>(UIState.Initialised())
    private set
    var currentWeatherStateV2 =  mutableStateOf<UIState<CurrentWeather_v2>>(UIState.Initialised())
    private set

    // NEW!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1

    private val _todayHourlyForecastState =
        mutableStateOf<UIState<List<HourlyForecastLocal>>>(UIState(data = tempList))
    val todayHourlyForecast get() = _todayHourlyForecastState

    private val producer = ChartEntryModelProducer()
    private var timeStampMap: MutableMap<Float, String> = mutableMapOf()
    private val markerMap: MutableMap<Float, Marker> = mutableMapOf()

    private val _currentWeatherGraphDataState = mutableStateOf<UIState<CurrentWeatherGraph>>(
        UIState(
            data = CurrentWeatherGraph(
                producer, timeStampMap, markerMap
            )
        )
    )
    val currentWeatherGraph get() = _currentWeatherGraphDataState
    val placeSuggestions = mutableStateListOf<PlaceSuggestion>()

    fun getLatLongByPlaceId(placeId: String) {
        val placeFields = listOf(Place.Field.LAT_LNG)

        // Construct a request object, passing the place ID and fields array.
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request).addOnSuccessListener { response: FetchPlaceResponse ->
            val place = response.place
            Log.i("fetchPlaceTagg", "Place found: $place")
            placeSuggestions.clear()
            viewModelScope.launch {
                delay(150)
                place.latLng?.let { it1 ->
                    place.latLng?.let { it2 ->
                        getCurrentWeatherByLatLong(it1.latitude, it2.longitude)
                    }
                }
            }
        }.addOnFailureListener { exception: Exception ->
            Log.d("fetchPlaceTagg", exception.toString())
        }
    }

    fun tryAutoComplete(query: String) {
        viewModelScope.launch {
            val token = AutocompleteSessionToken.newInstance()
            val request =
                FindAutocompletePredictionsRequest.builder().setSessionToken(token).setQuery(query)
                    .build()
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                    // Remove all elements
                    placeSuggestions.clear()
                    for (prediction in response.autocompletePredictions) {
                        Log.i("autocompleteTagg", prediction.getPrimaryText(null).toString())
                        Log.i("autocompleteTagg", prediction.placeId.toString())

                        placeSuggestions.add(
                            PlaceSuggestion(
                                prediction.getPrimaryText(null).toString(), prediction.placeId
                            )
                        )
                    }
                }.addOnFailureListener { exception: Exception? ->
                    Log.d("autocompleteTagg", exception.toString())
                }
        }
    }

    fun convertTempToCelsius(kTemp: Double): Double {
        val cel = kTemp - 273.15
        return (cel * 10.0).roundToInt() / 10.0
    }

     fun getLatLongFromDataStorePref(callback: (latLong: LatLong) -> Unit){
         viewModelScope.launch {
             val savedLat = dataStorePref.data.first()[PreferencesKeys.SAVED_LAT]
             val savedLong = dataStorePref.data.first()[PreferencesKeys.SAVED_LONG]
              callback.invoke(LatLong(savedLat, savedLong))
         }
    }

    fun saveLatLongInDataStorePref(lat: Double, long: Double) {
        viewModelScope.launch {
            dataStorePref.edit { preferences ->
                preferences[PreferencesKeys.SAVED_LAT] = lat
                preferences[PreferencesKeys.SAVED_LONG] = long
            }
        }
    }

    fun getDateFromUnix(unixTime: Long): String {
        val sdf = SimpleDateFormat("MM-dd-yyyy")
        val netDate = Date((unixTime) * 1000)
        return (sdf.format(netDate))
    }

    fun getTimeFromUnix(unixTime: Long): String {
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

    private fun handleGraphPointsCreation(
        forecastList: List<CurrentWeather>,
        hourlyForecast: MutableList<HourlyForecastLocal>,
        hourlyForecastGraphPoints: MutableList<FloatEntry>
    ) {
        var prevTime = 0f

        for (i in 0..7) {
            val currentWeatherItem = forecastList[i]

            val hourlyForecastItem = HourlyForecastLocal(
                temp = convertTempToCelsius(currentWeatherItem.mainTempData!!.temp!!),
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
                x = xCoordinate.toInt().toFloat(), y = hourlyForecastItem.temp!!.toFloat()
            )
            // 2f pe map hai 2.3f
            timeStampMap[hourlyForecastGraphItem.x] = hourlyForecastItem.timeString.toString()

            hourlyForecastGraphPoints.add(hourlyForecastGraphItem)
        }
    }

    fun getWeather(lat: Double, long: Double) {
        viewModelScope.launch {
          weatherRepository.getWeather(lat, long).collect { response ->
                when (response) {
                    is Resource_v2.Error -> {
                        currentWeatherStateV3.value = UIState.Error(
                            message = response.message?:"Some error occurred"
                        )
                    }
                    is Resource_v2.Loading -> {
                        currentWeatherStateV3.value = UIState.Loading()

                    }
                    is Resource_v2.Success -> {
                        currentWeatherStateV3.value = UIState.Success(
                            data = response.data.currentWeather
                        )
                    }
                }
            }
        }
    }

    suspend fun getCurrentWeatherByLatLong(lat: Double, long: Double) {
        Log.d("apiCall", "$lat $long")
        weatherRepository.getCurrentWeatherByLatLong(lat, long).onEach { result ->
            when (result.status) {
                Constants.WeatherApiStatus.STATUS_LOADING -> {
                    // Check if calling this fun when searching a location
                    if (_currentWeatherState.value.data != null) {
                        _todayHourlyForecastState.value =
                            (UIState(isLoading = true, data = tempList))
                        _currentWeatherGraphDataState.value = UIState(
                            true, data = CurrentWeatherGraph(
                                producer, timeStampMap, markerMap
                            )
                        )
                    }
                    // till here
                    _currentWeatherState.value = UIState(isLoading = true)
                }
                Constants.WeatherApiStatus.STATUS_SUCCESS -> {
                    _currentWeatherState.value = UIState(isLoading = false, data = result.data)
                    getTodayWeatherForecastByLatLong(lat, long)
                }
                Constants.WeatherApiStatus.STATUS_ERROR -> {
                    Log.d("currentWeatherStatus", result.errorMessage.toString())
                    _currentWeatherState.value =
                        UIState(isLoading = false, error = result.errorMessage.toString())
                }
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun getTodayWeatherForecastByLatLong(lat: Double, long: Double) {
        weatherRepository.getHourlyForecastByLatLong(lat, long).onEach { result ->

            when (result.status) {
                Constants.WeatherApiStatus.STATUS_LOADING -> {
                    _todayHourlyForecastState.value = UIState(data = tempList, isLoading = true)

                    producer.setEntries(listOf<FloatEntry>())
                    _currentWeatherGraphDataState.value =
                        UIState(data = CurrentWeatherGraph(producer, timeStampMap, markerMap))
                }
                Constants.WeatherApiStatus.STATUS_SUCCESS -> {
                    val hourlyForecast = mutableListOf<HourlyForecastLocal>()
                    val hourlyForecastGraphPoints = mutableListOf<FloatEntry>()

                    handleGraphPointsCreation(
                        result.data!!.forecastList!!, hourlyForecast, hourlyForecastGraphPoints
                    )

                    Log.d("weatherMap", timeStampMap.toString())

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
                        isLoading = false, data = null, error = result.errorMessage.toString()
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}