package com.ayaan.chiragfarmer.domain.model

data class BookingRequest(
    val latitude: Double,
    val longitude: Double,
    val serviceType: String,
    val farmArea: Int,
    val cropName: String?,
    val locationName: String?
)
