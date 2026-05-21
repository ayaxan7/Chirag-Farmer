package com.yash091099.ChiragFarmersApp.domain.repository

import com.yash091099.ChiragFarmersApp.data.remote.dto.NotificationsData

interface NotificationRepository {
    suspend fun getUserNotifications(
        type: String? = null,
        page: Int = 1,
        limit: Int = 20
    ): Result<NotificationsData>
}

