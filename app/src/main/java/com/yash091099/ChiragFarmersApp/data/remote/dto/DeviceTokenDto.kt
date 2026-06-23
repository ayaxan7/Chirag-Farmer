package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateDeviceTokenRequest(
    @SerializedName("token")
    val token: String,
    @SerializedName("deviceId")
    val deviceId: String,
    @SerializedName("deviceType")
    val deviceType: String
)

data class DeviceTokenData(
    @SerializedName("_id") val id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("deviceId")
    val deviceId: String,
    @SerializedName("deviceType")
    val deviceType: String,
    @SerializedName("userModel")
    val userModel: String,
    @SerializedName("lastActive")
    val lastActive: String? = null,
    @SerializedName("__v") val version: Int? = null
)

data class UpdateDeviceTokenResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("deviceToken")
    val deviceToken: DeviceTokenData? = null,
    @SerializedName("message")
    val message: String? = null
)

data class DeleteDeviceTokenRequest(
    @SerializedName("deviceId")
    val deviceId: String
)

data class DeleteDeviceTokenResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("deletedCount")
    val deletedCount: Int? = null
)
