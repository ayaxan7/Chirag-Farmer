package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.remote.dto.AppVersionRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.AppVersionResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AppVersionApiService {
    @POST("api/farmers/app-version")
    suspend fun updateAppVersion(
        @Header("Authorization") authorization: String,
        @Body request: AppVersionRequest
    ): AppVersionResponse
}
