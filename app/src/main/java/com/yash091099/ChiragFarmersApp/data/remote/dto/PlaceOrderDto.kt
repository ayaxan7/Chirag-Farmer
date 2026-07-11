package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Request DTOs
data class PlaceOrderRequest(
    @SerializedName("items")
    val items: List<OrderItemRequest>,
    @SerializedName("shippingAddress")
    val shippingAddress: String,
    @SerializedName("paymentMethod")
    val paymentMethod: String,
    @SerializedName("transactionId")
    val transactionId: String? = null,
    @SerializedName("keepWallet")
    val keepWallet: Boolean = false
)

data class OrderItemRequest(
    @SerializedName("product")
    val product: String,
    @SerializedName("quantity")
    val quantity: Int
)

// Response DTOs
data class PlaceOrderResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: PlaceOrderData? = null
)

data class PlaceOrderData(
    @SerializedName("order")
    val order: OrderDetailsDto,
    @SerializedName("receipt")
    val receipt: ReceiptDto
)

data class OrderDetailsDto(
    @SerializedName("buyer")
    val buyer: String,
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("items")
    val items: List<OrderItemDetailsDto>,
    @SerializedName("shippingAddress")
    val shippingAddress: ShippingAddressDto,
    @SerializedName("paymentMethod")
    val paymentMethod: String,
    @SerializedName("paymentStatus")
    val paymentStatus: String? = null,
    @SerializedName("transactionId")
    val transactionId: String? = null,
    @SerializedName("orderStatus")
    val orderStatus: String,
    @SerializedName("statusTimestamps")
    val statusTimestamps: StatusTimestampsDto,
    @SerializedName("subtotal")
    val subtotal: Double,
    @SerializedName("deliveryFee")
    val deliveryFee: Double,
    @SerializedName("discount")
    val discount: Double,
    @SerializedName("totalAmount")
    val totalAmount: Double,
    @SerializedName("_id")
    val id: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("__v")
    val version: Int? = null
)

data class ShippingAddressDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("completeAddress")
    val completeAddress: String,
    @SerializedName("pincode")
    val pincode: String,
    @SerializedName("coordinates")
    val coordinates: List<Double>? = null
)

data class OrderItemDetailsDto(
    @SerializedName("product")
    val product: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("priceAtPurchase")
    val priceAtPurchase: Double,
    @SerializedName("totalItemPrice")
    val totalItemPrice: Double,
    @SerializedName("_id")
    val id: String
)

data class StatusTimestampsDto(
    @SerializedName("orderPlacedAt")
    val orderPlacedAt: String
)

data class ReceiptDto(
    @SerializedName("items")
    val items: List<ReceiptItemDto>,
    @SerializedName("subtotal")
    val subtotal: Double,
    @SerializedName("deliveryFee")
    val deliveryFee: Double,
    @SerializedName("totalDeliveryFee")
    val totalDeliveryFee: Double,
    @SerializedName("totalDiscount")
    val totalDiscount: Double,
    @SerializedName("paymentMethod")
    val paymentMethod: String,
    @SerializedName("orderDate")
    val orderDate: String,
    @SerializedName("transactionId")
    val transactionId: String? = null,
    @SerializedName("totalCost")
    val totalCost: Double
)

data class ReceiptItemDto(
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    @SerializedName("name")
    val name: String,
    @SerializedName("sellerName")
    val sellerName: String,
    @SerializedName("amountPaid")
    val amountPaid: Double,
    @SerializedName("quantity")
    val quantity: String
)
