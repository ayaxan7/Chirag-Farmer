package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.CreateRazorpayOrderResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.domain.repository.RazorpayCheckoutRepository
import javax.inject.Inject

class CreateRazorpayOrderUseCase @Inject constructor(
    private val repository: RazorpayCheckoutRepository
) {
    suspend operator fun invoke(request: PlaceOrderRequest): Result<CreateRazorpayOrderResponse> {
        return repository.createCheckoutOrder(request)
    }
}
