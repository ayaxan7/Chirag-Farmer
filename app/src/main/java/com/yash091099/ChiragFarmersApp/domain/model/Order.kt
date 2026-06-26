package com.yash091099.ChiragFarmersApp.domain.model

data class Order(
    val orderObjectId: String,
    val orderId: String,
    val productId: String,
    val productName: String,
    val productImage: String,
    val buyerName: String,
    val buyerContact: String? = null,
    val quantity: String,
    val amountPaid: Double,
    val location: String,
    val status: String
)

data class OrdersData(
    val orders: List<Order>,
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int
)

