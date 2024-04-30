package com.example.daggerhilttest.viewmodels


import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daggerhilttest.PreferencesKeys
import com.example.daggerhilttest.models.*
import com.example.daggerhilttest.models.v2.GeocodingResponseV2
import com.example.daggerhilttest.models.v2.WeatherDataV2
import com.example.daggerhilttest.models.v2.toWeatherData
import com.example.daggerhilttest.repository.WeatherRepository
import com.example.daggerhilttest.util.Resource_v2
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val dataStorePref: DataStore<Preferences>,
    private val placesClient: PlacesClient
) : ViewModel() {

    sealed class UIState<T> {

        data class Success<T>(val data: T) : UIState<T>()

        data class Error<T>(val message: String?) : UIState<T>()

        class Loading<T> : UIState<T>()

    }

    var currentLocationLatLong: LatLong? = null
    private var autoCompleteJob: Job? = null

    var currentLocationState = MutableStateFlow<UIState<GeocodingResponseV2>>(UIState.Loading())
        private set
    var currentWeatherStateV3 =
        MutableStateFlow<UIState<WeatherDataV2>>(UIState.Loading())
        private set

    var placeSuggestions = mutableStateListOf<PlaceSuggestion>()
        private set

    fun resetPlaceSuggestions() {
        placeSuggestions.clear()
    }

    fun getLatLongByPlaceId(placeId: String) {
        val placeFields = listOf(Place.Field.LAT_LNG)

        // Construct a request object, passing the place ID and fields array.
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request).addOnSuccessListener { response: FetchPlaceResponse ->
            val place = response.place
            Log.i("fetchPlaceTagg", "Place found: $place")
            placeSuggestions.clear()
            viewModelScope.launch {
                place.latLng?.let {
                    getWeather(it.latitude.toFloat(), it.longitude.toFloat(), true)
                }
            }
        }.addOnFailureListener { exception: Exception ->
            Log.d("fetchPlaceTagg", exception.toString())
        }
    }

    fun tryAutoComplete(query: String) {
        autoCompleteJob?.cancel()
        autoCompleteJob = viewModelScope.launch {
            Log.d("auto-complete-tag", "fun called")
            val token = AutocompleteSessionToken.newInstance()
            val request =
                FindAutocompletePredictionsRequest.builder().setSessionToken(token).setQuery(query)
                    .build()
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                    // Remove all elements
                    placeSuggestions.clear()
                    val suggestions = response.autocompletePredictions.map {
                        PlaceSuggestion(
                            it.getPrimaryText(null).toString(),
                            it.placeId
                        )
                    }
                    Log.d("auto-complete-tag", suggestions.toString())
                    placeSuggestions.addAll(suggestions)
                }.addOnFailureListener { exception: Exception? ->
                    Log.d("auto-complete-tag", exception.toString())
                }
        }
    }


    fun getLatLongFromDataStorePref(callback: (latLong: LatLong) -> Unit) {
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

    fun getLocationFromGeocoding(lat: Float, long: Float) {
        viewModelScope.launch {
            weatherRepository.getLocationFromGeocoding(lat, long).collect { response ->
                Log.d("geocoding-response", response.toString())
                when (response) {
                    is Resource_v2.Error -> {
                        currentLocationState.value = UIState.Error(
                            message = response.message ?: "Some error occurred"
                        )
                    }

                    is Resource_v2.Loading -> {
                        currentLocationState.value = UIState.Loading()
                    }

                    is Resource_v2.Success -> {
                        currentLocationState.value = UIState.Success(
                            data = response.data.first()
                        )
                    }
                }
            }
        }
    }

    fun getGeolocation() {
        viewModelScope.launch {
            weatherRepository.getGeolocation()
                .onSuccess {
                    Log.d("geolocation_tag","$it")
                }.onFailure {
                    Log.d("geolocation_tag","${it.message}")
                }
        }
    }
    fun getWeather(lat: Float, long: Float, fromAutoSuggest: Boolean = false) {
        viewModelScope.launch {
            weatherRepository.getWeather(lat, long).onSuccess {
                currentWeatherStateV3.value = UIState.Success(data = it.toWeatherData())
            }.onFailure {
                currentWeatherStateV3.value = UIState.Error(message = it.message)
            }
        }
    }
}