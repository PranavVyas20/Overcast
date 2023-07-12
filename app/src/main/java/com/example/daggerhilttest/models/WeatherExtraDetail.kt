package com.example.daggerhilttest.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.material.icons.filled.Radar
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.daggerhilttest.util.WeatherExtraDetailType

data class WeatherExtraDetail(
    val type: WeatherExtraDetailType,
    val title: String,
    val icon: ImageVector = Icons.Default.HistoryToggleOff
)
