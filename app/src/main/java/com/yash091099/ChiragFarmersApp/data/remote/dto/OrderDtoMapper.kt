package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.yash091099.ChiragFarmersApp.domain.model.Order
import com.yash091099.ChiragFarmersApp.domain.model.OrdersData

fun OrderItemDto.toDomain(): Order = Order(
    orderObjectId = orderObjectId,
    orderId = orderId,
    productName = productName,
    productImage = productImage,
    buyerName = buyerName,
    buyerContact = buyerContact,
    quantity = quantity,
    amountPaid = amountPaid,
    location = location,
    status = status
)

fun ActiveOrdersResponse.toDomain(): OrdersData = OrdersData(
    orders = data.orders.map { it.toDomain() },
    total = data.pagination.total,
    page = data.pagination.page,
    limit = data.pagination.limit,
    totalPages = data.pagination.pages
)

