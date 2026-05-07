package com.yash091099.ChiragFarmersApp.data.remote.dto

data class OrderTrackingDto(
    val success: Boolean,
    val message: String,
    val data: OrderTrackingData
)

data class OrderTrackingData(
    val deliveryAddress: DeliveryAddress?,
    val deliveryDate: String?,
    val orderStatus: String?,
    val orderPlacedAt: String?,
    val packedAt: String?,
    val shippedAt: String?,
    val outForDeliveryAt: String?,
    val deliveredAt: String?,
    val imageUrl: String?,
    val productName: String?,
    val orderNumber: String?,
    val sellerName: String?,
    val amountPaid: Double?,
    val quantity: String?,
    val subtotal: Double?,
    val deliveryFee: Double?,
    val discount: Double?,
    val paymentMethod: String?,
    val orderDate: String?
)

data class DeliveryAddress(
    val name: String,
    val completeAddress: String,
    val pincode: String,
    val latitude: Double,
    val longitude: Double
)
