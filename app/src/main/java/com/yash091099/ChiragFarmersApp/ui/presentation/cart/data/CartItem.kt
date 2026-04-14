package com.yash091099.ChiragFarmersApp.ui.presentation.cart.data
data class CartItem(
    val id: String,
    val imageRes: Int,
    val productName: String,
    val sellerName: String,
    val price: Double,
    val deliveryDate: String,
    var quantity: String
)