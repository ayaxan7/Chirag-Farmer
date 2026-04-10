package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ToggleSoldOutRequest(
    @SerializedName("productId")
    val productId: String
)

data class ToggleSoldOutResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: ToggleSoldOutData?
)

data class ToggleSoldOutData(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("isAvailable")
    val isAvailable: Boolean
)

