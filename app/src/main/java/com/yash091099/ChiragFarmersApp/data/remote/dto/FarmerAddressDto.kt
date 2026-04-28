package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FarmerAddressesResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<FarmerAddressDto> = emptyList()
)

data class FarmerAddressDto(
    @SerializedName("_id")
    val id: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("pincode")
    val pincode: String,
    @SerializedName("landmark")
    val landmark: String? = null
)

