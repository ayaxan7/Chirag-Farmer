package com.yash091099.ChiragFarmersApp.data.repository

import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.remote.NotificationApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.NotificationsData
import com.yash091099.ChiragFarmersApp.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val apiService: NotificationApiService,
    private val chiragDataStore: ChiragDataStore
) : NotificationRepository {

    override suspend fun getUserNotifications(
        type: String?,
        page: Int,
        limit: Int
    ): Result<NotificationsData> {
        return try {
            val token = chiragDataStore.getAuthToken().first()
            if (token.isNullOrEmpty()) {
                return Result.failure(Exception("Authentication token not found"))
            }

            val response = apiService.getUserNotifications(
                token = "Bearer $token",
                type = type,
                page = page,
                limit = limit
            )

            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

