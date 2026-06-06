package com.yash091099.ChiragFarmersApp.ui.presentation.cart.payment

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.CartDataCache
import com.yash091099.ChiragFarmersApp.data.local.OrderResponseCache
import com.yash091099.ChiragFarmersApp.data.remote.dto.CartItemDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderItemRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePeCheckoutData
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePePaymentStatusResponse
import com.yash091099.ChiragFarmersApp.data.remote.dto.PhonePeVerifyRequest
import com.yash091099.ChiragFarmersApp.domain.usecase.GetPhonePePaymentStatusUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.InitiatePhonePeCheckoutUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.PlaceOrderUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.VerifyPhonePePaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.Locale

data class UpiAppInfo(
    val name: String,
    val packageName: String,
    val icon: Drawable?
)

data class PhonePeLaunchRequest(
    val token: String,
    val orderId: String,
    val merchantOrderId: String
)

sealed class PaymentUiState {
    object Idle : PaymentUiState()
    object Loading : PaymentUiState()
    data class Success(
        val response: PlaceOrderResponse? = null,
        val message: String = "Payment successful"
    ) : PaymentUiState()
    data class Error(val message: String) : PaymentUiState()
}

private const val CASH_ON_DELIVERY = "Cash On Delivery"
private const val PHONEPE_ONLINE_PAYMENT = "Online"
private const val PHONEPE_STATUS_COMPLETED = "COMPLETED"
private const val PHONEPE_STATUS_SUCCESS = "SUCCESS"
private const val PHONEPE_STATUS_SUCCEEDED = "SUCCEEDED"
private const val PHONEPE_STATUS_PAID = "PAID"
private const val PHONEPE_STATUS_PENDING = "PENDING"
private const val PHONEPE_STATUS_PROCESSING = "PROCESSING"
private const val PHONEPE_STATUS_IN_PROGRESS = "IN_PROGRESS"
private const val PHONEPE_STATUS_INITIATED = "INITIATED"
private const val PHONEPE_STATUS_FAILED = "FAILED"
private const val PHONEPE_STATUS_CANCELLED = "CANCELLED"
private const val PHONEPE_POLL_INTERVAL_MS = 15_000L
private const val PHONEPE_POLL_MAX_ATTEMPTS = 10

@HiltViewModel
class PaymentViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val initiatePhonePeCheckoutUseCase: InitiatePhonePeCheckoutUseCase,
    private val verifyPhonePePaymentUseCase: VerifyPhonePePaymentUseCase,
    private val getPhonePePaymentStatusUseCase: GetPhonePePaymentStatusUseCase,
    private val orderResponseCache: OrderResponseCache,
    private val cartDataCache: CartDataCache
) : ViewModel() {

    private val _installedUpiApps = MutableStateFlow<List<UpiAppInfo>>(emptyList())
    val installedUpiApps: StateFlow<List<UpiAppInfo>> = _installedUpiApps.asStateFlow()

    private val _paymentState = MutableStateFlow<PaymentUiState>(PaymentUiState.Idle)
    val paymentState: StateFlow<PaymentUiState> = _paymentState.asStateFlow()

    private val _phonePeLaunchRequest = MutableStateFlow<PhonePeLaunchRequest?>(null)
    val phonePeLaunchRequest: StateFlow<PhonePeLaunchRequest?> = _phonePeLaunchRequest.asStateFlow()

    private var phonePePollingJob: Job? = null
    private var pendingPhonePeCheckout: PhonePeCheckoutData? = null


    init {
        fetchInstalledUpiApps()
    }

    private fun fetchInstalledUpiApps() {
        val upiIntent = Intent(Intent.ACTION_VIEW).apply {
            data = "upi://pay?pa=dummy@upi".toUri()
        }
        
        val packageManager = context.packageManager
        val resolveInfoList = packageManager.queryIntentActivities(upiIntent, PackageManager.MATCH_ALL)
        Log.d("PaymentViewModel", "Found ${resolveInfoList.size} UPI apps")
        var upiApps = resolveInfoList.map { resolveInfo ->
            UpiAppInfo(
                name = resolveInfo.loadLabel(packageManager).toString(),
                packageName = resolveInfo.activityInfo.packageName,
                icon = resolveInfo.loadIcon(packageManager)
            )
        }.distinctBy { it.packageName }

        // Fallback: If no apps found via intent, check for common UPI apps by package name
        if (upiApps.isEmpty()) {
            Log.d("PaymentViewModel", "No apps found via intent, trying fallback...")
            val commonUpiPackages = listOf(
                "com.google.android.apps.nbu.paisa.user" to "Google Pay",
                "com.phonepe.app" to "PhonePe",
                "net.one97.paytm" to "Paytm",
                "in.org.npci.upiapp" to "BHIM",
                "com.mobikwik_new" to "Mobikwik",
                "com.freecharge.android" to "Freecharge"
            )

            val fallbackApps = mutableListOf<UpiAppInfo>()
            for ((pkg, name) in commonUpiPackages) {
                try {
                    val appInfo = packageManager.getApplicationInfo(pkg, 0)
                    if (appInfo.enabled) {
                        fallbackApps.add(UpiAppInfo(
                            name = name,
                            packageName = pkg,
                            icon = packageManager.getApplicationIcon(appInfo)
                        ))
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    // App not installed
                    Log.d("PaymentViewModel", "App not installed: $pkg with ${e.stackTrace}")
                }
            }
            upiApps = fallbackApps
        }

        Log.d("PaymentViewModel", "Installed UPI apps: $upiApps")
        _installedUpiApps.value = upiApps
    }

    fun placeOrder(paymentMethod: String) {
        if (paymentMethod.equals(CASH_ON_DELIVERY, ignoreCase = true)) {
            placeCashOnDeliveryOrder(paymentMethod)
        } else {
            startPhonePeCheckout()
        }
    }

    private fun placeCashOnDeliveryOrder(paymentMethod: String) {
        viewModelScope.launch {
            _paymentState.value = PaymentUiState.Loading
            phonePePollingJob?.cancel()
            clearPhonePeCheckoutState()
            try {
                val cachedCartData = cartDataCache.getCartData()
                if (cachedCartData == null) {
                    _paymentState.value = PaymentUiState.Error("Cart data not found. Please try again.")
                    return@launch
                }

                val request = buildPlaceOrderRequest(
                    paymentMethod = paymentMethod,
                    cachedCartDataItems = cachedCartData.items,
                    shippingAddress = cachedCartData.shippingAddress
                )

                if (request.items.isEmpty()) {
                    _paymentState.value = PaymentUiState.Error("No items in cart to order.")
                    return@launch
                }

                Log.d("PaymentViewModel", "Placing order with request: $request")

                placeOrderUseCase(request).fold(
                    onSuccess = { response ->
                        Log.d("PaymentViewModel", "Order placed successfully: ${response.data?.order?.orderId}")
                        orderResponseCache.setOrderResponse(response)
                        cartDataCache.clearCartData() // Clear cart data after successful order
                        clearPhonePeCheckoutState()
                        _paymentState.value = PaymentUiState.Success(response)
                    },
                    onFailure = { exception ->
                        Log.e("PaymentViewModel", "Order placement failed", exception)
                        _paymentState.value = PaymentUiState.Error(exception.message ?: "Order placement failed")
                    }
                )
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "Exception in placeOrder", e)
                _paymentState.value = PaymentUiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    private fun startPhonePeCheckout() {
        viewModelScope.launch {
            _paymentState.value = PaymentUiState.Loading
            phonePePollingJob?.cancel()
            clearPhonePeCheckoutState()

            try {
                val cachedCartData = cartDataCache.getCartData()
                if (cachedCartData == null) {
                    _paymentState.value = PaymentUiState.Error("Cart data not found. Please try again.")
                    return@launch
                }

                val request = buildPlaceOrderRequest(
                    paymentMethod = PHONEPE_ONLINE_PAYMENT,
                    cachedCartDataItems = cachedCartData.items,
                    shippingAddress = cachedCartData.shippingAddress
                )

                if (request.items.isEmpty()) {
                    _paymentState.value = PaymentUiState.Error("No items in cart to order.")
                    return@launch
                }

                Log.d("PaymentViewModel", "Creating PhonePe checkout with request: $request")

                initiatePhonePeCheckoutUseCase(request).fold(
                    onSuccess = { response ->
                        if (!response.success) {
                            _paymentState.value = PaymentUiState.Error(response.message)
                            return@fold
                        }

                        val checkoutData = response.data
                        val token = checkoutData?.token
                        val orderId = checkoutData?.phonePeOrderId
                            ?: checkoutData?.orderId
                            ?: checkoutData?.merchantOrderId
                        val merchantOrderId = checkoutData?.merchantOrderId
                            ?: checkoutData?.orderId

                        if (token.isNullOrBlank() || orderId.isNullOrBlank() || merchantOrderId.isNullOrBlank()) {
                            _paymentState.value = PaymentUiState.Error(
                                response.message.ifBlank { "PhonePe checkout token was not returned by the server." }
                            )
                            return@fold
                        }

                        pendingPhonePeCheckout = checkoutData
                        _phonePeLaunchRequest.value = PhonePeLaunchRequest(
                            token = token,
                            orderId = orderId,
                            merchantOrderId = merchantOrderId
                        )
                    },
                    onFailure = { exception ->
                        Log.e("PaymentViewModel", "PhonePe checkout failed", exception)
                        _paymentState.value = PaymentUiState.Error(
                            exception.message ?: "Unable to start PhonePe checkout"
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "Exception in startPhonePeCheckout", e)
                _paymentState.value = PaymentUiState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun verifyPhonePePayment() {
        val pendingCheckout = pendingPhonePeCheckout ?: run {
            _paymentState.value = PaymentUiState.Error("PhonePe checkout data not found. Please try again.")
            return
        }

        val merchantOrderId = pendingCheckout.merchantOrderId
        if (merchantOrderId.isNullOrBlank()) {
            _paymentState.value = PaymentUiState.Error("PhonePe merchant order id is missing.")
            return
        }

        phonePePollingJob?.cancel()
        phonePePollingJob = viewModelScope.launch {
            _paymentState.value = PaymentUiState.Loading

            val verifyRequest = PhonePeVerifyRequest(
                merchantOrderId = merchantOrderId,
                orderId = pendingCheckout.orderId
            )

            verifyPhonePePaymentUseCase(verifyRequest).fold(
                onSuccess = { verificationResponse ->
                    when (normalizePhonePeStatus(verificationResponse)) {
                        PHONEPE_STATUS_COMPLETED,
                        PHONEPE_STATUS_SUCCESS,
                        PHONEPE_STATUS_SUCCEEDED,
                        PHONEPE_STATUS_PAID -> handlePhonePeSuccess(verificationResponse)

                        PHONEPE_STATUS_PENDING,
                        PHONEPE_STATUS_PROCESSING,
                        PHONEPE_STATUS_IN_PROGRESS,
                        PHONEPE_STATUS_INITIATED -> pollPhonePePaymentStatus(merchantOrderId)

                        PHONEPE_STATUS_FAILED,
                        PHONEPE_STATUS_CANCELLED -> handlePhonePeFailure(verificationResponse.message)

                        else -> pollPhonePePaymentStatus(merchantOrderId)
                    }
                },
                onFailure = { exception ->
                    handlePhonePeFailure(exception.message ?: "Unable to verify PhonePe payment")
                }
            )
        }
    }

    private suspend fun pollPhonePePaymentStatus(merchantOrderId: String) {
        repeat(PHONEPE_POLL_MAX_ATTEMPTS) { attempt ->
            delay(PHONEPE_POLL_INTERVAL_MS)

            getPhonePePaymentStatusUseCase(merchantOrderId).fold(
                onSuccess = { statusResponse ->
                    when (normalizePhonePeStatus(statusResponse)) {
                        PHONEPE_STATUS_COMPLETED,
                        PHONEPE_STATUS_SUCCESS,
                        PHONEPE_STATUS_SUCCEEDED,
                        PHONEPE_STATUS_PAID -> {
                            handlePhonePeSuccess(statusResponse)
                            return
                        }

                        PHONEPE_STATUS_FAILED,
                        PHONEPE_STATUS_CANCELLED -> {
                            handlePhonePeFailure(statusResponse.message)
                            return
                        }

                        else -> {
                            _paymentState.value = PaymentUiState.Loading
                        }
                    }
                },
                onFailure = { exception ->
                    if (attempt == PHONEPE_POLL_MAX_ATTEMPTS - 1) {
                        handlePhonePeFailure(exception.message ?: "PhonePe payment verification timed out.")
                        return
                    }
                }
            )
        }

        if (_paymentState.value !is PaymentUiState.Success) {
            handlePhonePeFailure("Payment is still pending. Please check again after some time.")
        }
    }

    private fun handlePhonePeSuccess(response: PhonePePaymentStatusResponse) {
        phonePePollingJob?.cancel()
        cartDataCache.clearCartData()
        orderResponseCache.clearOrderResponse()
        clearPhonePeCheckoutState()
        _paymentState.value = PaymentUiState.Success(message = response.message.ifBlank { "Payment successful" })
    }

    private fun handlePhonePeFailure(message: String) {
        phonePePollingJob?.cancel()
        clearPhonePeCheckoutState()
        _paymentState.value = PaymentUiState.Error(message.ifBlank { "PhonePe payment failed" })
    }

    private fun clearPhonePeCheckoutState() {
        _phonePeLaunchRequest.value = null
        pendingPhonePeCheckout = null
    }

    fun clearPhonePeLaunchRequest() {
        _phonePeLaunchRequest.value = null
    }

    private fun buildPlaceOrderRequest(
        paymentMethod: String,
        cachedCartDataItems: List<CartItemDto>,
        shippingAddress: String
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
            paymentMethod = paymentMethod
        )
    }

    private fun normalizePhonePeStatus(response: PhonePePaymentStatusResponse): String {
        val rawStatus = response.data?.state
            ?: response.data?.paymentStatus
            ?: response.message
        return rawStatus.trim().replace(' ', '_').uppercase(Locale.US)
    }

    fun resetPaymentState() {
        phonePePollingJob?.cancel()
        clearPhonePeCheckoutState()
        _paymentState.value = PaymentUiState.Idle
    }
}


