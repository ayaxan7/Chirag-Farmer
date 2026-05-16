package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateOrderStatusRequest(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("status")
    val status: String
)

data class UpdateOrderStatusResponse(
    val success: Boolean,
    val message: String,
    val data: OrderTrackingData?
)
