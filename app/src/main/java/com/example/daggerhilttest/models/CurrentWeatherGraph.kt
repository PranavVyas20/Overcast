package com.example.daggerhilttest.models

import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatryk.vico.core.marker.Marker

data class CurrentWeatherGraph(
    val chartProducer: ChartEntryModelProducer? = null,
    val timeStampMap: MutableMap<Float, String>? = null,
    val markerMap: MutableMap<Float, Marker>? = null
)
