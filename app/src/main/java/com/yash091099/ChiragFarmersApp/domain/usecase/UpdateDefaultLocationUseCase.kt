package com.yash091099.ChiragFarmersApp.domain.usecase

import com.yash091099.ChiragFarmersApp.data.remote.dto.UpdateDefaultLocationResponse
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import javax.inject.Inject

class UpdateDefaultLocationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<UpdateDefaultLocationResponse> {
        return authRepository.updateDefaultLocation(latitude, longitude)
    }
}
