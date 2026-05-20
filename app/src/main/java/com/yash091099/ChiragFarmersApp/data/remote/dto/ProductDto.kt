package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.yash091099.ChiragFarmersApp.domain.model.Product
import com.google.gson.annotations.SerializedName

data class ProductResponseDto(
    val success: Boolean,
    val message: String,
    val data: ProductsDataDto
)

data class ProductsDataDto(
    val page: Int,
    val limit: Int,
    val total: Int,
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
    val rating:String
)

fun ProductDto.toDomain(): Product {
    val effectivePrice = discountedPrice ?: regularPrice ?: price ?: 0.0
    val thumbUrl = imageUrl ?: images?.firstOrNull() ?: ""

    return Product(
        productId = productId,
        productName = productName,
        imageUrl = thumbUrl,
        sellerName = sellerName,
        effectivePrice = effectivePrice.toInt(),
        availableQuantity = availableQuantity,
        originalPrice = regularPrice?.toInt() ?: 0,
        rating = rating
    )
}
