package com.example.daggerhilttest.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import com.example.daggerhilttest.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.example.daggerhilttest.models.HourlyForecastLocal
import com.example.daggerhilttest.models.HourlyForecastTest
import com.example.daggerhilttest.ui.theme.shimmerColor
import com.example.daggerhilttest.ui_components.*
import com.example.daggerhilttest.viewmodels.WeatherViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatryk.vico.core.entry.FloatEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CurrentWeatherScreen(weatherViewModel: WeatherViewModel) {
    val savedLatLongState = weatherViewModel.savedLatLong.value
    Log.d("tagss", savedLatLongState.lat.toString() + savedLatLongState.long.toString())

    LaunchedEffect(key1 = savedLatLongState) {
        weatherViewModel.getLatLongFromDataStorePref()

        if(savedLatLongState.lat != null) {
            // Need to add one more check here !
            // just need to add a bool in view model, to keep track if recomposition is due to :
            // a - city api call
            // b - location access
            // c - refresh location
            weatherViewModel.getCurrentWeatherByLatLong(savedLatLongState.lat!!.toFloat(), savedLatLongState.long!!.toFloat())
        }
        else {
            weatherViewModel.requestLocationAccess()
        }
    }
    val currentWeatherState = weatherViewModel.currentWeatherState.value
    val todayHourlyForecastState = weatherViewModel.todayHourlyForecast.value
    val currentWeatherGraphState = weatherViewModel.currentWeatherGraph.value

    Column(
        Modifier
            .padding(10.dp)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ExpandableSearchView(
            "",
            onSearchDisplayClosed = { },
            onSearchDisplayChanged = { }
        )
        CurrentWeatherCard(currentWeather = currentWeatherState.data, placeHolderVisibility = currentWeatherState.isLoading)
        WeatherExtraDetailCard(placeHolderVisibility = currentWeatherState.isLoading)
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Hourly Forecast",
            fontSize = 20.sp,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp)
        )
        LazyRow(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .placeholder(
                    visible = todayHourlyForecastState.isLoading,
                    color = shimmerColor,
                    shape = RoundedCornerShape(7.dp),
                    highlight = PlaceholderHighlight.shimmer(
                        highlightColor = Color.White,
                    )
                ),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(todayHourlyForecastState.data?: listOf()) { item ->
                HourlyForecastItem(
//                    if (item.isCurrentWeather) R.drawable.waves else
                    bgImage =  R.drawable.white_bg,
                    iconUrl = item.iconUrl!!,
                    temperature = item.temp.toString(),
                    time = item.timeString!!,
                )
            }
        }
        CurrentWeatherGraph(currentWeatherGraphState.data, currentWeatherGraphState.isLoading)
        BottomButtonLayout()
    }
}