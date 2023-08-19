package com.example.daggerhilttest.models.v2

import com.google.gson.annotations.SerializedName

data class GeocodingResponseV2(
    @SerializedName("name")
    val locationName: String?,
)

internal fun GeocodingResponseV2.toGeocodingData() =
    GeocodingDataV2(locationName = locationName ?: "Not Available")
