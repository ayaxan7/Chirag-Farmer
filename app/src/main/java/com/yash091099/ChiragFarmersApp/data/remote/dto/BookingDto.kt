package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BookingRequestDto(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("serviceType")
    val serviceType: String,
    @SerializedName("farmArea")
    val farmArea: Int,
    @SerializedName("cropName")
    val cropName: String?,
    @SerializedName("locationName")
    val locationName: String?
)

data class BookingResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String
)
