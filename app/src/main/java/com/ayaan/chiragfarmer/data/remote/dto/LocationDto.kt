package com.ayaan.chiragfarmer.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.ayaan.chiragfarmer.domain.model.Location

data class LocationDto(
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("lon")
    val lon: String
)

fun LocationDto.toDomain(): Location {
    return Location(
        displayName = displayName,
        latitude = lat.toDoubleOrNull() ?: 0.0,
        longitude = lon.toDoubleOrNull() ?: 0.0
    )
}
