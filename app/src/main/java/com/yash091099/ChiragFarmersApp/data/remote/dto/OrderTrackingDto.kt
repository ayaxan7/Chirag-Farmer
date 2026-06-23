package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OrderTrackingDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: OrderTrackingData
)

data class OrderTrackingData(
    @SerializedName("deliveryAddress")
    val deliveryAddress: DeliveryAddress?,
    @SerializedName("deliveryDate")
    val deliveryDate: String?,
    @SerializedName("orderStatus")
    val orderStatus: String?,
    @SerializedName("orderPlacedAt")
    val orderPlacedAt: String?,
    @SerializedName("packedAt")
    val packedAt: String?,
    @SerializedName("shippedAt")
    val shippedAt: String?,
    @SerializedName("outForDeliveryAt")
    val outForDeliveryAt: String?,
    @SerializedName("deliveredAt")
    val deliveredAt: String?,
    @SerializedName("cancelledAt")
    val cancelledAt: String? = null,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("productName")
    val productName: String?,
    @SerializedName("orderNumber")
    val orderNumber: String?,
    @SerializedName("sellerName")
    val sellerName: String?,
    @SerializedName("amountPaid")
    val amountPaid: Double?,
    @SerializedName("quantity")
    val quantity: String?,
    @SerializedName("subtotal")
    val subtotal: Double?,
    @SerializedName("deliveryFee")
    val deliveryFee: Double?,
    @SerializedName("discount")
    val discount: Double?,
    @SerializedName("paymentMethod")
    val paymentMethod: String?,
    @SerializedName("orderDate")
    val orderDate: String?,
    @SerializedName("productId")
    val productId: String? = null
)

data class DeliveryAddress(
    @SerializedName("name")
    val name: String,
    @SerializedName("completeAddress")
    val completeAddress: String,
    @SerializedName("pincode")
    val pincode: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)
