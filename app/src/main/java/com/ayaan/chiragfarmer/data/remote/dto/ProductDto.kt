package com.ayaan.chiragfarmer.data.remote.dto

import com.ayaan.chiragfarmer.domain.model.Product

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
    val price: Int,
    val availableQuantity: Int
)

fun ProductDto.toDomain(): Product {
    return Product(
        productId = productId,
        productName = productName,
        imageUrl = imageUrl,
        sellerName = sellerName,
        price = price,
        availableQuantity = availableQuantity
    )
}
