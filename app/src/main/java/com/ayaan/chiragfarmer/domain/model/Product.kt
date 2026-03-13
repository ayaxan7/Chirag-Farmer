package com.ayaan.chiragfarmer.domain.model

data class Product(
    val productId: String,
    val productName: String,
    val imageUrl: String,
    val sellerName: String,
    val effectivePrice: Int,
    val availableQuantity: Int,
    val originalPrice:Int
)
