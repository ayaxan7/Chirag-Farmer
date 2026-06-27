package com.yash091099.ChiragFarmersApp.domain.repository

import com.yash091099.ChiragFarmersApp.data.remote.dto.CreateRazorpayOrderResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.VerifyRazorpayPaymentRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.VerifyRazorpayPaymentResponse

interface RazorpayCheckoutRepository {
    suspend fun createCheckoutOrder(request: PlaceOrderRequest): Result<CreateRazorpayOrderResponse>
    suspend fun verifyPayment(request: VerifyRazorpayPaymentRequest): Result<VerifyRazorpayPaymentResponse>
    suspend fun getPaymentStatus(razorpayOrderId: String): Result<VerifyRazorpayPaymentResponse>
    suspend fun cancelCheckoutSession(razorpayOrderId: String): Result<Unit>
}
