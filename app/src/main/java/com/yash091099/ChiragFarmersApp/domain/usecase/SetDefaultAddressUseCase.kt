package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.SetDefaultAddressResponse
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import javax.inject.Inject

class SetDefaultAddressUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(addressId: String): Result<SetDefaultAddressResponse> {
        return authRepository.setDefaultAddress(addressId)
    }
}
