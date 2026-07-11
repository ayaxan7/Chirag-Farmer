package com.yash091099.ChiragFarmersApp.ui.presentation.cart.payment

import android.app.Activity
import android.content.Context
import com.yash091099.ChiragFarmersApp.R
import timber.log.Timber
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.CartDataCache
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.local.OrderResponseCache
import com.yash091099.ChiragFarmersApp.data.local.PendingPayment
import com.yash091099.ChiragFarmersApp.data.local.PendingPaymentStorage
import com.yash091099.ChiragFarmersApp.data.remote.dto.CartItemDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderItemRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.VerifyRazorpayPaymentRequest
import com.yash091099.ChiragFarmersApp.domain.usecase.CancelCheckoutSessionUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.CreateRazorpayOrderUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.PlaceOrderUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.VerifyRazorpayPaymentUseCase
import com.yash091099.ChiragFarmersApp.utils.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import javax.inject.Inject

data class RazorpayCheckoutOptions(
    val key: String,
    val amount: Int,
    val currency: String,
    val orderId: String,
    val name: String,
    val description: String,
    val prefillEmail: String?,
    val prefillContact: String?,
    val themeColor: String
)

sealed class PaymentUiState {
    object Idle : PaymentUiState()
    object Loading : PaymentUiState()
    data class Success(
        val response: PlaceOrderResponse? = null,
        val message: String = ""
    ) : PaymentUiState()
    data class Error(val message: String) : PaymentUiState()
}

private const val CASH_ON_DELIVERY = "Cash On Delivery"
private const val ONLINE_PAYMENT = "Online"

@HiltViewModel
class PaymentViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val createRazorpayOrderUseCase: CreateRazorpayOrderUseCase,
    private val verifyRazorpayPaymentUseCase: VerifyRazorpayPaymentUseCase,
    private val cancelCheckoutSessionUseCase: CancelCheckoutSessionUseCase,
    private val orderResponseCache: OrderResponseCache,
    private val cartDataCache: CartDataCache,
    private val pendingPaymentStorage: PendingPaymentStorage,
    private val chiragDataStore: ChiragDataStore
) : ViewModel() {

    private val _paymentState = MutableStateFlow<PaymentUiState>(PaymentUiState.Idle)
    val paymentState: StateFlow<PaymentUiState> = _paymentState.asStateFlow()

    private val _razorpayCheckoutRequest = MutableStateFlow<RazorpayCheckoutOptions?>(null)
    val razorpayCheckoutRequest: StateFlow<RazorpayCheckoutOptions?> = _razorpayCheckoutRequest.asStateFlow()

    private var pendingRazorpayOrderId: String? = null

    fun placeOrder(paymentMethod: String) {
        if (paymentMethod.equals(CASH_ON_DELIVERY, ignoreCase = true)) {
            placeCashOnDeliveryOrder()
        } else {
            startRazorpayCheckout()
        }
    }

    private fun placeCashOnDeliveryOrder() {
        viewModelScope.launch {
            _paymentState.value = PaymentUiState.Loading
            try {
                val cachedCartData = cartDataCache.getCartData()
                if (cachedCartData == null) {
                    _paymentState.value = PaymentUiState.Error(context.getString(R.string.error_cart_data_not_found))
                    return@launch
                }

                val request = buildPlaceOrderRequest(
                    paymentMethod = CASH_ON_DELIVERY,
                    cachedCartDataItems = cachedCartData.items,
                    shippingAddress = cachedCartData.shippingAddress
                )

                if (request.items.isEmpty()) {
                    _paymentState.value = PaymentUiState.Error(context.getString(R.string.error_no_items_in_cart))
                    return@launch
                }

                Timber.d("Placing COD order with request: $request")

                placeOrderUseCase(request).fold(
                    onSuccess = { response ->
                        Timber.d("COD order placed successfully: ${response.data?.order?.orderId}")
                        orderResponseCache.setOrderResponse(response)
                        cartDataCache.clearCartData()
                        _paymentState.value = PaymentUiState.Success(response)
                    },
                    onFailure = { exception ->
                        Timber.e(exception, "COD order placement failed")
                        _paymentState.value = PaymentUiState.Error(
                            exception.message ?: context.getString(R.string.error_order_placement_failed)
                        )
                    }
                )
            } catch (e: Exception) {
                Timber.e(e, "Exception in placeOrder")
                _paymentState.value = PaymentUiState.Error(getErrorMessage(e))
            }
        }
    }

    private fun startRazorpayCheckout() {
        viewModelScope.launch {
            _paymentState.value = PaymentUiState.Loading
            try {
                val cachedCartData = cartDataCache.getCartData()
                if (cachedCartData == null) {
                    _paymentState.value = PaymentUiState.Error(context.getString(R.string.error_cart_data_not_found))
                    return@launch
                }

                val request = buildPlaceOrderRequest(
                    paymentMethod = ONLINE_PAYMENT,
                    cachedCartDataItems = cachedCartData.items,
                    shippingAddress = cachedCartData.shippingAddress,
                    keepWallet = cachedCartData.keepWallet
                )

                if (request.items.isEmpty()) {
                    _paymentState.value = PaymentUiState.Error(context.getString(R.string.error_no_items_in_cart))
                    return@launch
                }

                Timber.d("Creating Razorpay checkout with request: $request")

                createRazorpayOrderUseCase(request).fold(
                    onSuccess = { response ->
                        if (!response.success) {
                            _paymentState.value = PaymentUiState.Error(
                                response.message.ifBlank { context.getString(R.string.error_payment_initialization_failed) }
                            )
                            return@fold
                        }

                        val data = response.data ?: run {
                            _paymentState.value = PaymentUiState.Error(
                                context.getString(R.string.error_payment_initialization_failed)
                            )
                            return@fold
                        }

                        Timber.d("Checkout response code=%d, razorpayOrderId=%s, amount=%s, walletContribution=%s",
                            response.code, data.razorpayOrderId, data.amount, data.walletContribution)

                        // code 201 = pure wallet success (order placed directly, no Razorpay order)
                        if (response.code == 201) {
                            Timber.d("Pure wallet payment - order placed directly")
                            cartDataCache.clearCartData()
                            _paymentState.value = PaymentUiState.Success(
                                message = response.message.ifBlank { "Order placed successfully" }
                            )
                            return@fold
                        }

                        // code 200 = Razorpay order (split or full online)
                        // For split payment, amount is already the gateway contribution
                        val razorpayOrderId = data.razorpayOrderId ?: run {
                            _paymentState.value = PaymentUiState.Error(
                                context.getString(R.string.error_payment_initialization_failed)
                            )
                            return@fold
                        }
                        val razorpayKeyId = data.razorpayKeyId ?: run {
                            _paymentState.value = PaymentUiState.Error(
                                context.getString(R.string.error_payment_initialization_failed)
                            )
                            return@fold
                        }
                        val currency = data.currency ?: "INR"
                        val razorpayAmount = data.amount ?: run {
                            _paymentState.value = PaymentUiState.Error(
                                context.getString(R.string.error_payment_initialization_failed)
                            )
                            return@fold
                        }

                        pendingRazorpayOrderId = razorpayOrderId

                        pendingPaymentStorage.savePendingPayment(
                            PendingPayment(
                                razorpayOrderId = razorpayOrderId,
                                amount = razorpayAmount,
                                paymentType = ONLINE_PAYMENT
                            )
                        )

                        _paymentState.value = PaymentUiState.Idle

                        val userPhone = chiragDataStore.getUserPhone().first()

                        Timber.d("Launching Razorpay: amount=%d (%.2f rupees), orderId=%s",
                            (razorpayAmount * 100).toInt(), razorpayAmount, razorpayOrderId)

                        _razorpayCheckoutRequest.value = RazorpayCheckoutOptions(
                            key = razorpayKeyId,
                            amount = (razorpayAmount * 100).toInt(),
                            currency = currency,
                            orderId = razorpayOrderId,
                            name = "Chirag",
                            description = "Order Payment",
                            prefillEmail = null,
                            prefillContact = userPhone,
                            themeColor = "#3399cc"
                        )
                    },
                    onFailure = { exception ->
                        Timber.e(exception, "Razorpay checkout creation failed")
                        _paymentState.value = PaymentUiState.Error(
                            exception.message ?: context.getString(R.string.error_payment_initialization_failed)
                        )
                    }
                )
            } catch (e: Exception) {
                Timber.e(e, "Exception in startRazorpayCheckout")
                _paymentState.value = PaymentUiState.Error(getErrorMessage(e))
            }
        }
    }

    fun onPaymentSuccess(razorpayPaymentId: String?, razorpayOrderId: String?, razorpaySignature: String?) {
        val orderId = razorpayOrderId ?: pendingRazorpayOrderId
        if (orderId == null || razorpayPaymentId == null || razorpaySignature == null) {
            _paymentState.value = PaymentUiState.Error(
                context.getString(R.string.error_payment_verification_failed)
            )
            return
        }
        verifyPayment(orderId, razorpayPaymentId, razorpaySignature)
    }

    fun onPaymentError(code: Int, description: String?) {
        Timber.e("Razorpay payment error: code=$code description=$description")
        pendingRazorpayOrderId?.let { orderId ->
            pendingPaymentStorage.markFailed(orderId)
            viewModelScope.launch { cancelCheckoutSessionUseCase(orderId) }
        }
        pendingRazorpayOrderId = null
        _razorpayCheckoutRequest.value = null
        val message = when {
            code == 0 -> context.getString(R.string.error_payment_cancelled)
            description != null -> description
            else -> context.getString(R.string.error_payment_verification_failed)
        }
        _paymentState.value = PaymentUiState.Error(message)
    }

    fun clearRazorpayCheckoutRequest() {
        _razorpayCheckoutRequest.value = null
    }

    private fun verifyPayment(razorpayOrderId: String, razorpayPaymentId: String, razorpaySignature: String) {
        viewModelScope.launch {
            _paymentState.value = PaymentUiState.Loading

            val verifyRequest = VerifyRazorpayPaymentRequest(
                razorpayOrderId = razorpayOrderId,
                razorpayPaymentId = razorpayPaymentId,
                razorpaySignature = razorpaySignature
            )

            verifyRazorpayPaymentUseCase(verifyRequest).fold(
                onSuccess = { response ->
                    if (response.success) {
                        pendingPaymentStorage.markCompleted(razorpayOrderId)
                        pendingRazorpayOrderId = null
                        _razorpayCheckoutRequest.value = null
                        cartDataCache.clearCartData()
                        _paymentState.value = PaymentUiState.Success(
                            message = response.message.ifBlank { context.getString(R.string.success_payment) }
                        )
                    } else {
                        pendingPaymentStorage.markFailed(razorpayOrderId)
                        _paymentState.value = PaymentUiState.Error(
                            response.message.ifBlank { context.getString(R.string.error_payment_verification_failed) }
                        )
                    }
                },
                onFailure = { exception ->
                    Timber.e(exception, "Payment verification failed")
                    _paymentState.value = PaymentUiState.Error(
                        exception.message ?: context.getString(R.string.error_payment_verification_failed)
                    )
                }
            )
        }
    }

    private fun buildPlaceOrderRequest(
        paymentMethod: String,
        cachedCartDataItems: List<CartItemDto>,
        shippingAddress: String,
        keepWallet: Boolean = false
    ): PlaceOrderRequest {
        val items = cachedCartDataItems.map { cartItem ->
            OrderItemRequest(
                product = cartItem.productId,
                quantity = cartItem.quantity
            )
        }
        return PlaceOrderRequest(
            items = items,
            shippingAddress = shippingAddress,
            paymentMethod = paymentMethod,
            keepWallet = keepWallet
        )
    }

    fun resetPaymentState() {
        _paymentState.value = PaymentUiState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        pendingRazorpayOrderId?.let { orderId ->
            runBlocking {
                try { cancelCheckoutSessionUseCase(orderId) } catch (_: Exception) {}
            }
        }
    }
}
