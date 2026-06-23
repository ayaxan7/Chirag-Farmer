package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MixedProductsResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: MixedProductsData
)

data class MixedProductsData(
    @SerializedName("vendorProducts")
    val vendorProducts: List<MixedProductItem>,
    @SerializedName("directFromFarmersProducts")
    val directFromFarmersProducts: List<MixedProductItem>,
    @SerializedName("seedProducts")
    val seedProducts: List<MixedProductItem>,
    @SerializedName("popularProducts")
    val randomProducts: List<MixedProductItem>
)

data class MixedProductItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("sellerName")
    val sellerName: String,
    @SerializedName("originalPrice")
    val originalPrice: Double,
    @SerializedName("finalPrice")
    val finalPrice: Double,
    @SerializedName("rating")
    val rating: String
)
