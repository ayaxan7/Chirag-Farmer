package com.yash091099.ChiragFarmersApp.domain.repository

import com.yash091099.ChiragFarmersApp.data.remote.dto.CropAnalysisDataDto

interface CropAnalysisRepository {
    suspend fun analyzeCrop(imageUri: String): Result<CropAnalysisDataDto>
}

