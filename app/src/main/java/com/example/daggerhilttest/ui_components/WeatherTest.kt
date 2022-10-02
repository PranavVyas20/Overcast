package com.example.daggerhilttest.ui_components


import androidx.compose.runtime.Composable

import com.example.daggerhilttest.viewmodels.WeatherViewModel
import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatryk.vico.core.entry.FloatEntry


@Composable
fun WeatherTest() {
    val ls = listOf<FloatEntry>(
        FloatEntry(x = 2f, y = 1f),
        FloatEntry(x = 3f, y = 7f),
        FloatEntry(x = 4f, y = 1f),
        FloatEntry(x = 5f, y = 4f),
        FloatEntry(x = 6f, y = 3f),
    )
    val producer = ChartEntryModelProducer(ls)
//    CurrentWeatherGraph(null, producer)
}


