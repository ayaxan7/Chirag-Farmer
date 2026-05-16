package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Represents cancellation details within an order item
data class CancellationDetailsDto(
    @SerializedName("cancelledAt")
    val cancelledAt: String?,
    @SerializedName("previousStatus")
    val previousStatus: String?,
    @SerializedName("reason")
    val reason: String?
)

// Updated Order Item with item-level status and cancellation details
data class OrderItemWithStatus(
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("productName")
    val productName: String?,
    @SerializedName("sellerName")
    val sellerName: String?,
    @SerializedName("orderNumber")
    val orderNumber: String?,
    @SerializedName("pricePaid")
    val pricePaid: Double?,
    @SerializedName("quantity")
    val quantity: String?,
    @SerializedName("productId")
    val productId: String?,
    @SerializedName("itemStatus")
    val itemStatus: String?,
    @SerializedName("cancellationDetails")
    val cancellationDetails: CancellationDetailsDto?
)

