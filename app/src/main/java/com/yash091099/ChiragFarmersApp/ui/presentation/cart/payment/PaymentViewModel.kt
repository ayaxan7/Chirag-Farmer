package com.yash091099.ChiragFarmersApp.ui.presentation.cart.payment

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.net.toUri
import com.yash091099.ChiragFarmersApp.data.CartDataCache
import com.yash091099.ChiragFarmersApp.data.local.OrderResponseCache
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderRequest
import com.yash091099.ChiragFarmersApp.data.remote.dto.PlaceOrderResponse
import com.yash091099.ChiragFarmersApp.domain.usecase.PlaceOrderUseCase

data class UpiAppInfo(
    val name: String,
    val packageName: String,
    val icon: Drawable?
)

sealed class PaymentUiState {
    object Idle : PaymentUiState()
    object Loading : PaymentUiState()
    data class Success(val response: PlaceOrderResponse) : PaymentUiState()
    data class Error(val message: String) : PaymentUiState()
}

@HiltViewModel
class PaymentViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val orderResponseCache: OrderResponseCache,
    private val cartDataCache: CartDataCache
) : ViewModel() {

    private val _installedUpiApps = MutableStateFlow<List<UpiAppInfo>>(emptyList())
    val installedUpiApps: StateFlow<List<UpiAppInfo>> = _installedUpiApps.asStateFlow()

    private val _paymentState = MutableStateFlow<PaymentUiState>(PaymentUiState.Idle)
    val paymentState: StateFlow<PaymentUiState> = _paymentState.asStateFlow()


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
        viewModelScope.launch {
            _paymentState.value = PaymentUiState.Loading
            try {
                // Get cart data from cache
                val cachedCartData = cartDataCache.getCartData()
                if (cachedCartData == null) {
                    _paymentState.value = PaymentUiState.Error("Cart data not found. Please try again.")
                    return@launch
                }

                val items = cachedCartData.items.map { cartItem ->
                    com.yash091099.ChiragFarmersApp.data.remote.dto.OrderItemRequest(
                        product = cartItem.productId,
                        quantity = cartItem.quantity
                    )
                }

                if (items.isEmpty()) {
                    _paymentState.value = PaymentUiState.Error("No items in cart to order.")
                    return@launch
                }

                val request = PlaceOrderRequest(
                    items = items,
                    shippingAddress = cachedCartData.shippingAddress,
                    paymentMethod = paymentMethod
                )

                Log.d("PaymentViewModel", "Placing order with request: $request")

                placeOrderUseCase(request).fold(
                    onSuccess = { response ->
                        Log.d("PaymentViewModel", "Order placed successfully: ${response.data?.order?.orderId}")
                        orderResponseCache.setOrderResponse(response)
                        cartDataCache.clearCartData() // Clear cart data after successful order
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

    fun resetPaymentState() {
        _paymentState.value = PaymentUiState.Idle
    }
}


