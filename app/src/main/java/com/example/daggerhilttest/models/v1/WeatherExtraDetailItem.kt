package com.example.daggerhilttest.models.v1

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.daggerhilttest.util.WeatherExtraDetailType

data class WeatherExtraDetailItem(
    val type: WeatherExtraDetailType,
    val title: String,
    val value: String,
    val icon: ImageVector = Icons.Default.HistoryToggleOff
)
