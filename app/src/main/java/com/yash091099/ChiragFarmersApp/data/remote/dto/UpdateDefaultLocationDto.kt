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

