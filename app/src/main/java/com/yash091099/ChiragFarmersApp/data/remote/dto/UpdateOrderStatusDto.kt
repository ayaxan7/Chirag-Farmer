package com.yash091099.ChiragFarmersApp.data.remote.dto

data class UpdateOrderStatusRequest(
    val status: String
)

data class UpdateOrderStatusResponse(
    val success: Boolean,
    val message: String,
    val data: OrderTrackingData?
)
