package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.AddDeliveryLocationRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.AddDeliveryLocationResponse
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import javax.inject.Inject

class AddDeliveryLocationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: AddDeliveryLocationRequest): Result<AddDeliveryLocationResponse> {
        return authRepository.addDeliveryLocation(request)
    }
}
