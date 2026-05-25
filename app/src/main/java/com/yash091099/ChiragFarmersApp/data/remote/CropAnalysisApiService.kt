package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.remote.dto.CropAnalysisResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

@Suppress("unused")
interface CropAnalysisApiService {

    @Multipart
    @POST("api/farmers/ai/analyze-crop")
    suspend fun analyzeCrop(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): CropAnalysisResponseDto
}


