package com.ayaan.chiragfarmer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AddProductRequest(
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
    @SerializedName("price")
    val price: Double,
    @SerializedName("discount")
    val discount: Double? = null,
    @SerializedName("deliveryFee")
    val deliveryFee: Double? = null,
    @SerializedName("location")
    val location: LocationRequestDto
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

