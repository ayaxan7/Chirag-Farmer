package com.yash091099.ChiragFarmersApp.ui.presentation.orders

import timber.log.Timber
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.RateProductRequest
import com.yash091099.ChiragFarmersApp.domain.usecase.GetOrderDetailsUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.GetUserPlacedOrdersUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.RateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DropReviewState {
	data object Idle : DropReviewState()
	data object Loading : DropReviewState()
	data class Success(val message: String) : DropReviewState()
	data class Error(val message: String) : DropReviewState()
}

@HiltViewModel
class DropReviewViewModel @Inject constructor(
	private val rateProductUseCase: RateProductUseCase,
	private val getOrderDetailsUseCase: GetOrderDetailsUseCase,
	private val getUserPlacedOrdersUseCase: GetUserPlacedOrdersUseCase
) : ViewModel() {
	private val _state = MutableStateFlow<DropReviewState>(DropReviewState.Idle)
	val state: StateFlow<DropReviewState> = _state.asStateFlow()

	fun submitReview(orderId: String, productId: String, rating: Int, review: String?) {
		viewModelScope.launch {
			_state.value = DropReviewState.Loading

			val resolvedProductId = resolveProductId(orderId = orderId, productId = productId)
			Timber.d("Resolved Product Id: $resolvedProductId")
			Timber.d("Order Id: $orderId, Product Id: $resolvedProductId, Rating: $rating, Review: $review")

			if (resolvedProductId.isBlank()) {
				_state.value = DropReviewState.Error("Product information is missing for this order. Please open order details and try again.")
				return@launch
			}
//			Timber.d("Order Id: $orderId, Product Id: $resolvedProductId, Rating: $rating, Review: $review")
			rateProductUseCase(
				RateProductRequest(
					orderId = orderId,
					productId = resolvedProductId,
					rating = rating,
					review = review?.takeIf { it.isNotBlank() }
				)
			).fold(
				onSuccess = { response ->
					_state.value = if (response.success) {
						DropReviewState.Success(response.message)
					} else {
						DropReviewState.Error(response.message)
					}
				},
				onFailure = { error ->
					_state.value = DropReviewState.Error(error.message ?: "Unable to submit review")
				}
			)
		}
	}

	private suspend fun resolveProductId(orderId: String, productId: String): String {
		if (productId.isNotBlank()) return productId

		Timber.d("Resolving product id for order: $orderId")

		val fromOrderDetails = getOrderDetailsUseCase(orderId).fold(
			onSuccess = { response ->
				try {
					val items = response.data.items
					Timber.d("OrderDetails returned ${'$'}{items.size} items")
					val pid = items.firstOrNull { !it.productId.isNullOrBlank() }?.productId.orEmpty()
					Timber.d("ProductId from order details: ${'$'}pid")
					pid
				} catch (e: Exception) {
					Timber.d("Error reading order details: ${'$'}{e.message}")
					""
				}
			},
			onFailure = { err ->
				Timber.d("getOrderDetails failed: ${'$'}{err.message}")
				""
			}
		)
		if (fromOrderDetails.isNotBlank()) return fromOrderDetails

		Timber.d("Falling back to user placed orders lookup (type=complete)")

		val fromPlacedOrders = getUserPlacedOrdersUseCase(type = "complete", page = 1, limit = 100).fold(
			onSuccess = { response ->
				try {
					val orders = response.data.orders
					Timber.d("PlacedOrders returned ${'$'}{orders.size} orders")
					val matched = orders.firstOrNull {
						it.orderObjectId == orderId && !it.productId.isNullOrBlank()
					}
					val pid = matched?.productId.orEmpty()
					Timber.d("ProductId from placed orders: ${'$'}pid")
					pid
				} catch (e: Exception) {
					Timber.d("Error reading placed orders: ${'$'}{e.message}")
					""
				}
			},
			onFailure = { err ->
				Timber.d("getUserPlacedOrders failed: ${'$'}{err.message}")
				""
			}
		)
		if (fromPlacedOrders.isNotBlank()) return fromPlacedOrders

		Timber.d("Unable to resolve product id for order: $orderId")
		return ""
	}

	fun resetState() {
		_state.value = DropReviewState.Idle
	}
}

