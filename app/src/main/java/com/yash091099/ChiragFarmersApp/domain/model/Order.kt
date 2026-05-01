package com.yash091099.ChiragFarmersApp.domain.model

data class Order(
    val orderObjectId: String,
    val orderId: String,
    val productName: String,
    val productImage: String,
    val buyerName: String,
    val buyerContact: String,
    val quantity: String,
    val amountPaid: Int,
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

