package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AddProductRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("category")
    val category: String,
    @SerializedName("subcategory")
    val subcategory: String? = null,
    @SerializedName("price")
    val price: Double,
    @SerializedName("discount")
    val discount: Double? = null,
    @SerializedName("deliveryFee")
    val deliveryFee: Double? = null,
    @SerializedName("unit")
    val unit: String? = null,
    @SerializedName("quantity")
    val quantity: Int? = null,
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("location")
    val location: LocationRequestDto,
    @SerializedName("keyFeatures")
    val keyFeatures: List<String>? = null
)

data class LocationRequestDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("coordinates")
    val coordinates: List<Double>
)

data class AddProductResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: ProductIdData?
)

data class ProductIdData(
    @SerializedName("_id")
    val id: String
)

