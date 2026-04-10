package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DeleteProductRequest(
    @SerializedName("productId")
    val productId: String
)

data class DeleteProductResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String
)

