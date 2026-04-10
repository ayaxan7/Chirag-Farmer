package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDetailsResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: ProductDetailsData?
)

data class ProductDetailsData(
    @SerializedName("_id")
    val id: String,
    @SerializedName("farmer")
    val farmer: FarmerInfo,
    @SerializedName("title")
    val title: String,
    @SerializedName("images")
    val images: List<String>,
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

data class FarmerInfo(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("mobileNumber")
    val mobileNumber: String
)

data class ProductLocation(
    @SerializedName("name")
    val name: String,
    @SerializedName("coordinates")
    val coordinates: List<Double>
)

