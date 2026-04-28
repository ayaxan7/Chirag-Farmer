package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.FarmerAddressDto
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import javax.inject.Inject

class GetFarmerAddressesUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<List<FarmerAddressDto>> {
        return authRepository.getFarmerAddresses()
    }
}

