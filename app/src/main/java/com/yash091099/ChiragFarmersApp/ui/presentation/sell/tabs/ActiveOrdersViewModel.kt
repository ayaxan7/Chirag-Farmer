package com.yash091099.ChiragFarmersApp.ui.presentation.sell.tabs

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderTrackingData
import com.yash091099.ChiragFarmersApp.domain.usecase.GetActiveOrdersUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.GetOrderTrackingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yash091099.ChiragFarmersApp.domain.model.Order
import com.yash091099.ChiragFarmersApp.domain.usecase.UpdateOrderStatusUseCase
import com.yash091099.ChiragFarmersApp.data.remote.dto.CancelOrderRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed class OrderTrackingState {
    data object Idle : OrderTrackingState()
    data object Loading : OrderTrackingState()
    data class Success(val data: OrderTrackingData) : OrderTrackingState()
    data class Error(val message: String) : OrderTrackingState()
}

sealed class CancelOrderState {
    data object Idle : CancelOrderState()
    data object Loading : CancelOrderState()
    data class Success(val message: String) : CancelOrderState()
    data class Error(val message: String) : CancelOrderState()
}

@HiltViewModel
class ActiveOrdersViewModel @Inject constructor(
    getActiveOrdersUseCase: GetActiveOrdersUseCase,
    private val getOrderTrackingUseCase: GetOrderTrackingUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase,
    private val repository: com.yash091099.ChiragFarmersApp.domain.repository.OrderRepository
) : ViewModel() {

    private val activeOrdersFlow: Flow<PagingData<Order>> = getActiveOrdersUseCase().cachedIn(viewModelScope).also {
        Timber.tag("ActiveOrdersVM").d("created device=%s SDK=%s", Build.MODEL, Build.VERSION.SDK_INT)
    }

    private val _orderTrackingState = MutableStateFlow<OrderTrackingState>(OrderTrackingState.Idle)
    val orderTrackingState: StateFlow<OrderTrackingState> = _orderTrackingState.asStateFlow()

    private val _cancelOrderState = MutableStateFlow<CancelOrderState>(CancelOrderState.Idle)
    val cancelOrderState: StateFlow<CancelOrderState> = _cancelOrderState.asStateFlow()

    val activeOrders: Flow<PagingData<Order>>
        get() = activeOrdersFlow

    fun selectOrder(orderId: String?) {
        Timber.tag("ActiveOrdersVM").d("selectOrder orderId=%s", orderId)
        if (orderId != null) {
            fetchOrderTracking(orderId)
        } else {
            _orderTrackingState.value = OrderTrackingState.Idle
        }
    }

    private fun fetchOrderTracking(id: String) {
        Timber.tag("ActiveOrdersVM").d("fetchOrderTracking id=%s", id)
        viewModelScope.launch {
            _orderTrackingState.value = OrderTrackingState.Loading
            getOrderTrackingUseCase(id).fold(
                onSuccess = { response ->
                    Timber.tag("ActiveOrdersVM").d("fetchOrderTracking success id=%s", id)
                    _orderTrackingState.value = OrderTrackingState.Success(response.data)
                },
                onFailure = { exception ->
                    Timber.tag("ActiveOrdersVM").e(exception, "fetchOrderTracking failed id=%s", id)
                    _orderTrackingState.value = OrderTrackingState.Error(
                        exception.message ?: "Failed to load tracking details"
                    )
                }
            )
        }
    }

    fun updateOrderStatus(id: String, productId: String, status: String) {
        Timber.tag("ActiveOrdersVM").d("updateOrderStatus id=%s productId=%s status=%s", id, productId, status)
        viewModelScope.launch {
            updateOrderStatusUseCase(id, productId, status).onSuccess { response ->
                Timber.tag("ActiveOrdersVM").d("updateOrderStatus success=%s", response.success)
                if (response.success) {
                    fetchOrderTracking(id)
                }
            }.onFailure { error ->
                Timber.tag("ActiveOrdersVM").e(error, "updateOrderStatus failed id=%s", id)
            }
        }
    }

    fun cancelOrder(orderId: String, productId: String, reason: String) {
        Timber.tag("ActiveOrdersVM").d("cancelOrder orderId=%s productId=%s", orderId, productId)
        viewModelScope.launch {
            _cancelOrderState.value = CancelOrderState.Loading
            repository.sellerCancelOrder(
                CancelOrderRequest(
                    orderId = orderId,
                    productId = productId,
                    reason = reason
                )
            ).fold(
                onSuccess = { response ->
                    Timber.tag("ActiveOrdersVM").d("cancelOrder success=%s message=%s", response.success, response.message)
                    if (response.success) {
                        _cancelOrderState.value = CancelOrderState.Success(response.message)
                    } else {
                        _cancelOrderState.value = CancelOrderState.Error(response.message)
                    }
                },
                onFailure = { error ->
                    Timber.tag("ActiveOrdersVM").e(error, "cancelOrder failed orderId=%s", orderId)
                    _cancelOrderState.value = CancelOrderState.Error(
                        error.message ?: "Failed to cancel order"
                    )
                }
            )
        }
    }

    fun resetCancelState() {
        _cancelOrderState.value = CancelOrderState.Idle
    }
}

