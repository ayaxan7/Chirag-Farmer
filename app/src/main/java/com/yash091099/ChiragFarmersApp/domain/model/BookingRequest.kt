package com.yash091099.ChiragFarmersApp.domain.model

data class BookingRequest(
    val latitude: Double,
    val longitude: Double,
    val serviceType: String,
    val farmArea: Int,
    val cropName: String?,
    val locationName: String?
)
