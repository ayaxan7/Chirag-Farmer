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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import com.yash091099.ChiragFarmersApp.R
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
    @ApplicationContext private val context: Context,
    private val getActiveOrdersUseCase: GetActiveOrdersUseCase,
    private val getOrderTrackingUseCase: GetOrderTrackingUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase,
    private val repository: com.yash091099.ChiragFarmersApp.domain.repository.OrderRepository
) : ViewModel() {

    private val _selectedStatus = MutableStateFlow<String?>(null)

    private val _orderTrackingState = MutableStateFlow<OrderTrackingState>(OrderTrackingState.Idle)
    val orderTrackingState: StateFlow<OrderTrackingState> = _orderTrackingState.asStateFlow()

    private val _cancelOrderState = MutableStateFlow<CancelOrderState>(CancelOrderState.Idle)
    val cancelOrderState: StateFlow<CancelOrderState> = _cancelOrderState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeOrders: Flow<PagingData<Order>> = _selectedStatus.flatMapLatest { status ->
        Timber.tag("ActiveOrdersVM").d("fetching orders status=%s", status)
        getActiveOrdersUseCase(status)
    }.cachedIn(viewModelScope)

    fun setFilterStatus(status: String?) {
        _selectedStatus.value = status
    }

    fun selectOrder(orderId: String?, productId: String? = null) {
        Timber.tag("ActiveOrdersVM").d("selectOrder orderId=%s productId=%s", orderId, productId)
        if (orderId != null) {
            fetchOrderTracking(orderId, productId)
        } else {
            _orderTrackingState.value = OrderTrackingState.Idle
        }
    }

    private fun fetchOrderTracking(id: String, productId: String? = null) {
        Timber.tag("ActiveOrdersVM").d("fetchOrderTracking id=%s productId=%s", id, productId)
        viewModelScope.launch {
            _orderTrackingState.value = OrderTrackingState.Loading
            getOrderTrackingUseCase(id, productId).fold(
                onSuccess = { response ->
                    Timber.tag("ActiveOrdersVM").d("fetchOrderTracking success id=%s", id)
                    _orderTrackingState.value = OrderTrackingState.Success(response.data)
                },
                onFailure = { exception ->
                    Timber.tag("ActiveOrdersVM").e(exception, "fetchOrderTracking failed id=%s", id)
                    _orderTrackingState.value = OrderTrackingState.Error(
                        exception.message ?: context.getString(R.string.error_failed_load_tracking)
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
                    fetchOrderTracking(id, productId)
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
                        error.message ?: context.getString(R.string.error_failed_cancel_order)
                    )
                }
            )
        }
    }

    fun resetCancelState() {
        _cancelOrderState.value = CancelOrderState.Idle
    }
}

