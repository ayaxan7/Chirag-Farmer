package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.DefaultLocationData
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import javax.inject.Inject

class GetDefaultDeliveryLocationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<DefaultLocationData?> {
        return authRepository.getDefaultDeliveryLocation()
    }
}

