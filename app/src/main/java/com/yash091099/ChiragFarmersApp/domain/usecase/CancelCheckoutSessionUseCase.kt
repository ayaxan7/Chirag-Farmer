package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.domain.repository.RazorpayCheckoutRepository
import timber.log.Timber
import javax.inject.Inject

class CancelCheckoutSessionUseCase @Inject constructor(
    private val repository: RazorpayCheckoutRepository
) {
    suspend operator fun invoke(razorpayOrderId: String) {
        repository.cancelCheckoutSession(razorpayOrderId)
            .onFailure { Timber.e(it, "Cancel checkout session failed for $razorpayOrderId") }
    }
}
