package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ActiveOrdersResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: OrdersDataWrapper,
    @SerializedName("message")
    val message: String
)

data class OrdersDataWrapper(
    @SerializedName("orders")
    val orders: List<OrderItemDto>,
    @SerializedName("pagination")
    val pagination: PaginationDto
)

data class OrderItemDto(
    @SerializedName("orderObjectId")
    val orderObjectId: String,
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("productId")
    val productId: String,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("productImage")
    val productImage: String,
    @SerializedName("buyerName")
    val buyerName: String,
    @SerializedName("buyerContact")
    val buyerContact: String,
    @SerializedName("quantity")
    val quantity: String,
    @SerializedName("amountPaid")
    val amountPaid: Double,
    @SerializedName("location")
    val location: String,
    @SerializedName("status")
    val status: String
)

data class PaginationDto(
    @SerializedName("total")
    val total: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("pages")
    val pages: Int
)

