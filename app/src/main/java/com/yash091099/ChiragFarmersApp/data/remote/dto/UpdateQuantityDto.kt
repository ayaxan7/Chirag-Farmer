package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateQuantityRequest(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("action")
    val action: String  // "increment" or "decrement"
)

data class UpdateQuantityResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: UpdateQuantityData? = null
)

data class UpdateQuantityData(
    @SerializedName("productId")
    val productId: String? = null,
    @SerializedName("productName")
    val productName: String? = null,
    @SerializedName("productImage")
    val productImage: String? = null,
    @SerializedName("sellerName")
    val sellerName: String? = null,
    @SerializedName("finalPrice")
    val finalPrice: Double? = null,
    @SerializedName("quantity")
    val quantity: Int? = null,
    @SerializedName("removed")
    val removed: Boolean? = false,
    @SerializedName("cartItemsCount")
    val cartItemsCount: Int? = null
)

