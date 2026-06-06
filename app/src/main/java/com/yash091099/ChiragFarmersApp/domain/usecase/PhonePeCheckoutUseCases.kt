package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePeCheckoutResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePePaymentStatusResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePeVerifyRequest
import com.yash091099.ChiragFarmersApp.domain.repository.PhonePeCheckoutRepository
import javax.inject.Inject

class InitiatePhonePeCheckoutUseCase @Inject constructor(
    private val repository: PhonePeCheckoutRepository
) {
    suspend operator fun invoke(request: PlaceOrderRequest): Result<PhonePeCheckoutResponse> {
        return repository.createCheckout(request)
    }
}

class VerifyPhonePePaymentUseCase @Inject constructor(
    private val repository: PhonePeCheckoutRepository
) {
    suspend operator fun invoke(request: PhonePeVerifyRequest): Result<PhonePePaymentStatusResponse> {
        return repository.verifyPayment(request)
    }
}

class GetPhonePePaymentStatusUseCase @Inject constructor(
    private val repository: PhonePeCheckoutRepository
) {
    suspend operator fun invoke(merchantOrderId: String): Result<PhonePePaymentStatusResponse> {
        return repository.getPaymentStatus(merchantOrderId)
    }
}

