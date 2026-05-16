package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Request DTO for cancelling a specific product in an order
data class CancelOrderRequest(
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("productId")
    val productId: String,
    @SerializedName("reason")
    val reason: String
)

// Response DTO for cancellation
data class CancelOrderResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: CancellationData?
)

data class CancellationData(
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("productId")
    val productId: String,
    @SerializedName("sellerId")
    val sellerId: String,
    @SerializedName("cancelledAt")
    val cancelledAt: String,
    @SerializedName("previousStatus")
    val previousStatus: String
)

