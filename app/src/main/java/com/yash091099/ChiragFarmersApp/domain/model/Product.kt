package com.yash091099.ChiragFarmersApp.domain.model

data class Product(
    val productId: String,
    val productName: String,
    val imageUrl: String,
    val sellerName: String,
    val effectivePrice: Int,
    val availableQuantity: Int,
    val originalPrice:Int
)
