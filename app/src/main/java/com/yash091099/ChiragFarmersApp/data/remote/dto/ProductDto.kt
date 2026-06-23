package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.yash091099.ChiragFarmersApp.domain.model.Product
import com.google.gson.annotations.SerializedName

data class ProductResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: ProductsDataDto
)

data class ProductsDataDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("products")
    val products: List<ProductDto>
)

data class ProductDto(
    @SerializedName("_id", alternate = ["productId"])
    val productId: String,
    @SerializedName("title", alternate = ["productName"])
    val productName: String,
    @SerializedName("images")
    val images: List<String>? = null, // Made nullable
    @SerializedName("imageUrl")
    val imageUrl: String? = null,    // Added lightweight fallback
    @SerializedName("sellerName", alternate = ["farmer"])
    val sellerName: String,
    @SerializedName("price")
    val price: Double? = null,
    @SerializedName("regularPrice", alternate = ["originalPrice"])
    val regularPrice: Double? = null,
    @SerializedName("discountedPrice", alternate = ["finalPrice"])
    val discountedPrice: Double? = null,
    @SerializedName("quantity", alternate = ["availableQuantity"])
    val availableQuantity: Int,
    @SerializedName("rating")
    val rating: Double? = null
)

fun ProductDto.toDomain(): Product {
    val effectivePrice = discountedPrice ?: regularPrice ?: price ?: 0.0
    val thumbUrl = imageUrl ?: images?.firstOrNull() ?: ""

    return Product(
        productId = productId,
        productName = productName,
        imageUrl = thumbUrl,
        sellerName = sellerName,
        effectivePrice = effectivePrice,
        availableQuantity = availableQuantity,
        originalPrice = regularPrice ?: 0.0,
        rating = rating?.toString() ?: "0.0"
    )
}
