package com.yash091099.ChiragFarmersApp.domain.repository

import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePeCheckoutResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePePaymentStatusResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePeVerifyRequest

interface PhonePeCheckoutRepository {
    suspend fun createCheckout(request: PlaceOrderRequest): Result<PhonePeCheckoutResponse>
    suspend fun verifyPayment(request: PhonePeVerifyRequest): Result<PhonePePaymentStatusResponse>
    suspend fun getPaymentStatus(merchantOrderId: String): Result<PhonePePaymentStatusResponse>
}

