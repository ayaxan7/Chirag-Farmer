package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AddToCartRequest(
    @SerializedName("productId")
    val productId: String
)

data class AddToCartResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: CartData? = null
)

data class CartData(
    @SerializedName("cartItemsCount")
    val cartItemsCount: Int
)

