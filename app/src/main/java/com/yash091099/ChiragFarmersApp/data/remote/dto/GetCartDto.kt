package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GetCartResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<CartItemDto> = emptyList()
)

data class CartItemDto(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("productImage")
    val productImage: String? = null,
    @SerializedName("sellerName")
    val sellerName: String,
    @SerializedName("finalPrice")
    val finalPrice: Double,
    @SerializedName("quantity")
    val quantity: Int
)

