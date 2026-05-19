package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RateProductRequest(
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("productId")
    val productId: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("review")
    val review: String? = null
)

data class RateProductResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: RatedProductData? = null,
    @SerializedName("message")
    val message: String
)

data class RatedProductData(
    @SerializedName("_id")
    val id: String,
    @SerializedName("farmerId")
    val farmerId: String,
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("productId")
    val productId: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("review")
    val review: String?,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("dislikes")
    val dislikes: Int,
    @SerializedName("createdAt")
    val createdAt: String
)

