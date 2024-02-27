package com.example.daggerhilttest.models.v2

import com.google.gson.annotations.SerializedName

data class HourlyForecastResponseV2(
    @SerializedName("temp")
    val temp: Float?,
    @SerializedName("datetimeEpoch")
    val dateTimeEpoch: Long?,
    @SerializedName("icon")
    val icon: String?
)
