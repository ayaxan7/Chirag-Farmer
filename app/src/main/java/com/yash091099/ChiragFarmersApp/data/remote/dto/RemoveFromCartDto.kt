package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RemoveFromCartRequest(
    @SerializedName("productId")
    val productId: String
)

data class RemoveFromCartResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: RemoveFromCartData? = null
)

data class RemoveFromCartData(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("cartItemsCount")
    val cartItemsCount: Int
)

