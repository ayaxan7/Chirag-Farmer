package com.yash091099.ChiragFarmersApp.ui.presentation.home.screens.notifications

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.data.remote.dto.NotificationItemDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.NotificationPagination
import com.yash091099.ChiragFarmersApp.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class NotificationUiModel(
    val id: String,
    val title: String,
    val body: String,
    val imageUrl: String?,
    val receivedAt: String,
    val tag: String,
    val actionButtonText: String,
    val orderId: String? = null,
    val bookingId: String? = null,
    val notificationType: String? = null,
    val status: String? = null,
    val url: String? = null
)

sealed class NotificationsUiState {
    data object Loading : NotificationsUiState()
    data class Success(
        val notifications: List<NotificationUiModel>,
        val pagination: NotificationPagination
    ) : NotificationsUiState()
    data class Error(val message: String) : NotificationsUiState()
}

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotificationsUiState>(NotificationsUiState.Loading)
    val uiState: StateFlow<NotificationsUiState> = _uiState.asStateFlow()

    private var lastType: String? = null
    private var lastPage: Int = 1
    private var lastLimit: Int = 20

    fun loadNotifications(type: String? = null, page: Int = 1, limit: Int = 20) {
        lastType = type
        lastPage = page
        lastLimit = limit

        viewModelScope.launch {
            _uiState.value = NotificationsUiState.Loading
            notificationRepository.getUserNotifications(type = type, page = page, limit = limit)
                .fold(
                    onSuccess = { data ->
                        _uiState.value = NotificationsUiState.Success(
                            notifications = data.notifications.map { it.toUiModel() },
                            pagination = data.pagination
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = NotificationsUiState.Error(
                            exception.message ?: context.getString(R.string.error_failed_load_notifications)
                        )
                    }
                )
        }
    }

    fun retry() {
        loadNotifications(lastType, lastPage, lastLimit)
    }

    private fun NotificationItemDto.toUiModel(): NotificationUiModel {
        val resolvedType = data?.type?.uppercase()?.trim().orEmpty()
        val resolvedTag = tag.lowercase().trim()
        val resolvedImageUrl = imageUrl ?: data?.image

        return NotificationUiModel(
            id = id,
            title = title,
            body = body,
            imageUrl = resolvedImageUrl,
            receivedAt = receivedAt,
            tag = resolvedTag,
            actionButtonText = resolveActionButtonText(resolvedTag, resolvedType, data?.bookingId, data?.orderId),
            orderId = data?.orderId,
            bookingId = data?.bookingId,
            notificationType = resolvedType,
            status = data?.status,
            url = data?.url
        )
    }

    private fun resolveActionButtonText(
        tag: String,
        type: String,
        bookingId: String?,
        orderId: String?
    ): String {
        return when {
            !bookingId.isNullOrBlank() || tag == "service" || type.contains("BOOKING") -> context.getString(R.string.notification_view_booking)
            !orderId.isNullOrBlank() || tag == "buy" || tag == "sell" || type.contains("ORDER") -> context.getString(R.string.notification_view_order)
            else -> context.getString(R.string.notification_view_details)
        }
    }
}

