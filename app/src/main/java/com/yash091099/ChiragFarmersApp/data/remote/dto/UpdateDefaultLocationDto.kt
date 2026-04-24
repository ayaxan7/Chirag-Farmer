package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateDefaultLocationRequest(
    val latitude: Double,
    val longitude: Double
)

data class UpdateDefaultLocationResponse(
    val success: Boolean,
    val message: String,
    val data: DefaultLocationData? = null
)

data class DefaultLocationData(
    val name: String,
    val addressString: String,
    val pincode: String? = null,
    @SerializedName("coordinates")
    val coordinates: List<Double>? = null,
    @SerializedName("_id")
    val id: String? = null
)

