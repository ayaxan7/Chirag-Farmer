package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateProductRequest(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("price")
    val price: Double? = null,
    @SerializedName("quantity")
    val quantity: Int? = null,
    @SerializedName("images")
    val images: List<String>? = null,
    @SerializedName("discount")
    val discount: Double? = null,
    @SerializedName("deliveryFee")
    val deliveryFee: Double? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("subcategory")
    val subcategory: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("location")
    val location: LocationRequestDto? = null,
    @SerializedName("keyFeatures")
    val keyFeatures: List<String>? = null
)

data class UpdateProductResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: UpdatedProductData?
)

data class UpdatedProductData(
    @SerializedName("_id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("availableStockWeight")
    val availableStockWeight: Double?,
    @SerializedName("discount")
    val discount: Double? = null,
    @SerializedName("description")
    val description: String?,
    @SerializedName("farmer")
    val farmer: String,
    @SerializedName("images")
    val images: List<String> = emptyList(),
    @SerializedName("category")
    val category: String?,
    @SerializedName("subcategory")
    val subcategory: String?,
    @SerializedName("isAvailable")
    val isAvailable: Boolean,
    @SerializedName("location")
    val location: ProductLocation,
    @SerializedName("keyFeatures")
    val keyFeatures: List<String> = emptyList(),
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

