package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BuyNowResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: BuyNowData? = null,
    @SerializedName("code")
    val code: Int = 0
)

data class BuyNowData(
    @SerializedName("productId")
    val productId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    @SerializedName("sellerName")
    val sellerName: String,
    @SerializedName("finalPrice")
    val finalPrice: Double,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("subtotal")
    val subtotal: Double,
    @SerializedName("deliveryFee")
    val deliveryFee: Double,
    @SerializedName("discount")
    val discount: Double,
    @SerializedName("totalCost")
    val totalCost: Double,
    @SerializedName("defaultLocation")
    val defaultLocation: DefaultLocation? = null
)

data class DefaultLocation(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("completeAddress")
    val completeAddress: String? = null,
    @SerializedName("pincode")
    val pincode: String? = null,
    @SerializedName("coordinates")
    val coordinates: List<Double>? = null
)

