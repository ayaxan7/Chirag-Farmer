package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SearchProductResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val data: List<SearchProductItem>,
    @SerializedName("message")
    val message: String
)

data class SearchProductItem(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("imageUrl")
    val imageUrl: String
)
