package com.yash091099.ChiragFarmersApp.data.local

import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderResponseCache @Inject constructor() {
    private val _orderResponse = MutableStateFlow<PlaceOrderResponse?>(null)
    val orderResponse: StateFlow<PlaceOrderResponse?> = _orderResponse.asStateFlow()

    fun setOrderResponse(response: PlaceOrderResponse) {
        _orderResponse.value = response
    }

    fun clearOrderResponse() {
        _orderResponse.value = null
    }

    fun getOrderResponse(): PlaceOrderResponse? {
        return _orderResponse.value
    }
}

