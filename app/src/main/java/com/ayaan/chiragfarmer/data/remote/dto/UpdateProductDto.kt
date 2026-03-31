package com.ayaan.chiragfarmer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateProductRequest(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("images")
    val images: List<String>? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("availableStockWeight")
    val availableStockWeight: Double? = null,
    @SerializedName("price")
    val price: Double? = null,
    @SerializedName("discount")
    val discount: Double? = null,
    @SerializedName("deliveryFee")
    val deliveryFee: Double? = null,
    @SerializedName("location")
    val location: LocationRequestDto? = null
)

data class UpdateProductResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: UpdatedProductData?
)

data class UpdatedProductData(
    @SerializedName("_id")
    val id: String,
    @SerializedName("farmer")
    val farmer: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("category")
    val category: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("availableStockWeight")
    val availableStockWeight: Double?,
    @SerializedName("location")
    val location: ProductLocation,
    @SerializedName("price")
    val price: Double,
    @SerializedName("discount")
    val discount: Double? = null,
    @SerializedName("deliveryFee")
    val deliveryFee: Double? = null,
    @SerializedName("isAvailable")
    val isAvailable: Boolean,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

