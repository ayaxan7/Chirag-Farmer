package com.ayaan.chiragfarmer.data.remote.dto

import com.ayaan.chiragfarmer.domain.model.Product
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
    val imageUrl: String,
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
        imageUrl = imageUrl,
        sellerName = sellerName,
        effectivePrice = effectivePrice.toInt(),
        availableQuantity = availableQuantity,
        originalPrice = regularPrice?.toInt() ?: 0
    )
}
