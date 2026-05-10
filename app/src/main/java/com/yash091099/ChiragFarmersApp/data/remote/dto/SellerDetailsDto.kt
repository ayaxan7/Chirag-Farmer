package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.yash091099.ChiragFarmersApp.domain.model.Product

data class SellerDetailsResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val data: SellerDetailsData?
)

data class SellerDetailsData(
    val seller: SellerInfo,
    val stats: SellerStats,
    val products: List<SellerProductDto>,
    val pagination: SellerPagination
)

data class SellerInfo(
    val userId: String,
    val name: String,
    val profileImageUrl: String?
)

data class SellerStats(
    val totalListings: Int,
    val soldOutProducts: Int
)

data class SellerProductDto(
    val productId: String,
    val name: String,
    val imageUrl: String?,
    val sellerName: String,
    val originalPrice: Double,
    val discountedPrice: Double
)

data class SellerPagination(
    val total: Int,
    val page: Int,
    val limit: Int,
    val pages: Int
)

fun SellerProductDto.toDomain(): Product {
    return Product(
        productId = productId,
        productName = name,
        imageUrl = imageUrl ?: "",
        sellerName = sellerName,
        effectivePrice = discountedPrice.toInt(),
        availableQuantity = 0,
        originalPrice = originalPrice.toInt()
    )
}
