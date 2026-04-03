package com.ayaan.chiragfarmer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MixedProductsResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
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
    val id: String,
    val productName: String,
    val imageUrl: String,
    val sellerName: String,
    val originalPrice: Double,
    val finalPrice: Double
)

