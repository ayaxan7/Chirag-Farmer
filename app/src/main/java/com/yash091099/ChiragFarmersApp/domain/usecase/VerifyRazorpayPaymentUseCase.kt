package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.VerifyRazorpayPaymentRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.VerifyRazorpayPaymentResponse
import com.yash091099.ChiragFarmersApp.domain.repository.RazorpayCheckoutRepository
import javax.inject.Inject

class VerifyRazorpayPaymentUseCase @Inject constructor(
    private val repository: RazorpayCheckoutRepository
) {
    suspend operator fun invoke(request: VerifyRazorpayPaymentRequest): Result<VerifyRazorpayPaymentResponse> {
        return repository.verifyPayment(request)
    }
}
