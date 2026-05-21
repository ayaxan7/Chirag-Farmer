package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.remote.dto.NotificationsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NotificationApiService {
    @GET("api/notifications/user")
    suspend fun getUserNotifications(
        @Header("Authorization") token: String,
        @Query("type") type: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): NotificationsResponse
}

