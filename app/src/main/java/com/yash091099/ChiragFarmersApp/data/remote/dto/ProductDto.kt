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
    val productId: String,
    val productName: String,
    val images: List<String>,
    val sellerName: String,
    @SerializedName("price")
    val price: Double? = null,
    @SerializedName("regularPrice")
    val regularPrice: Double? = null,
    @SerializedName("discountedPrice")
    val discountedPrice: Double? = null,
    val availableQuantity: Int
)

fun ProductDto.toDomain(): Product {
    val effectivePrice = discountedPrice ?: regularPrice ?: price ?: 0.0

    return Product(
        productId = productId,
        productName = productName,
        imageUrl = images.firstOrNull() ?: "", // Use first image from array
        sellerName = sellerName,
        effectivePrice = effectivePrice.toInt(),
        availableQuantity = availableQuantity,
        originalPrice = regularPrice?.toInt() ?: 0
    )
}
