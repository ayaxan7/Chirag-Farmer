package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateDeviceTokenRequest(
    val token: String,
    val deviceType: String
)

data class DeviceTokenData(
    @SerializedName("_id") val id: String,
    val userId: String,
    val token: String,
    val deviceType: String,
    val userModel: String,
    val lastActive: String? = null,
    @SerializedName("__v") val version: Int? = null
)

data class UpdateDeviceTokenResponse(
    val success: Boolean,
    val deviceToken: DeviceTokenData? = null,
    val message: String? = null
)
