package com.yash091099.ChiragFarmersApp.data.remote.dto

data class BookingRequestDto(
    val latitude: Double,
    val longitude: Double,
    val serviceType: String,
    val farmArea: Int,
    val cropName: String?,
    val locationName: String?
)

data class BookingResponseDto(
    val success: Boolean,
    val message: String
)
