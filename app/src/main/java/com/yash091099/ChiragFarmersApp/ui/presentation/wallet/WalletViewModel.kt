package com.yash091099.ChiragFarmersApp.ui.presentation.wallet

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.remote.dto.WalletVerifyPaymentRequest
import com.yash091099.ChiragFarmersApp.domain.repository.WalletRepository
import com.yash091099.ChiragFarmersApp.utils.getErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
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

sealed class WalletUiState {
    object Idle : WalletUiState()
    object Loading : WalletUiState()
    data class Success(val balance: Double, val message: String = "") : WalletUiState()
    data class Error(val message: String) : WalletUiState()
}

@HiltViewModel
class WalletViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val walletRepository: WalletRepository,
    private val chiragDataStore: ChiragDataStore
) : ViewModel() {

    private val _walletState = MutableStateFlow<WalletUiState>(WalletUiState.Idle)
    val walletState: StateFlow<WalletUiState> = _walletState.asStateFlow()

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()

    private val _razorpayCheckoutRequest = MutableStateFlow<RazorpayCheckoutOptions?>(null)
    val razorpayCheckoutRequest: StateFlow<RazorpayCheckoutOptions?> = _razorpayCheckoutRequest.asStateFlow()

    init {
        loadBalance()
    }

    fun loadBalance() {
        viewModelScope.launch {
            walletRepository.getBalance().fold(
                onSuccess = { response ->
                    if (response.success) {
                        _balance.value = response.data?.balance ?: 0.0
                    }
                },
                onFailure = { Timber.e(it, "Failed to load wallet balance") }
            )
        }
    }

    fun addMoney(amount: Double) {
        if (amount <= 0) {
            _walletState.value = WalletUiState.Error(
                context.getString(R.string.wallet_invalid_amount)
            )
            return
        }

        viewModelScope.launch {
            _walletState.value = WalletUiState.Loading
            walletRepository.addMoney(amount).fold(
                onSuccess = { response ->
                    if (!response.success || response.data == null) {
                        _walletState.value = WalletUiState.Error(
                            response.message.ifBlank { context.getString(R.string.error_payment_initialization_failed) }
                        )
                        return@fold
                    }
                    val data = response.data
                    val userPhone = chiragDataStore.getUserPhone().first()
                    _walletState.value = WalletUiState.Idle
                    _razorpayCheckoutRequest.value = RazorpayCheckoutOptions(
                        key = data.razorpayKeyId,
                        amount = (data.amount * 100).toInt(),
                        currency = data.currency,
                        orderId = data.razorpayOrderId,
                        name = "Chirag",
                        description = context.getString(R.string.wallet_top_up_description),
                        prefillEmail = null,
                        prefillContact = userPhone,
                        themeColor = "#3399cc"
                    )
                },
                onFailure = { exception ->
                    Timber.e(exception, "Failed to create wallet Razorpay order")
                    _walletState.value = WalletUiState.Error(
                        exception.message ?: context.getString(R.string.error_payment_initialization_failed)
                    )
                }
            )
        }
    }

    fun onPaymentSuccess(razorpayPaymentId: String?, razorpayOrderId: String?, razorpaySignature: String?) {
        if (razorpayOrderId == null || razorpayPaymentId == null || razorpaySignature == null) {
            _walletState.value = WalletUiState.Error(
                context.getString(R.string.error_payment_verification_failed)
            )
            return
        }
        verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature)
    }

    fun onPaymentError(code: Int, description: String?) {
        Timber.e("Razorpay wallet payment error: code=$code description=$description")
        _razorpayCheckoutRequest.value = null
        val message = when {
            code == 0 -> context.getString(R.string.error_payment_cancelled)
            description != null -> description
            else -> context.getString(R.string.error_payment_verification_failed)
        }
        _walletState.value = WalletUiState.Error(message)
    }

    fun clearRazorpayCheckoutRequest() {
        _razorpayCheckoutRequest.value = null
    }

    fun resetWalletState() {
        _walletState.value = WalletUiState.Idle
    }

    private fun verifyPayment(razorpayOrderId: String, razorpayPaymentId: String, razorpaySignature: String) {
        viewModelScope.launch {
            _walletState.value = WalletUiState.Loading
            val verifyRequest = WalletVerifyPaymentRequest(
                razorpayOrderId = razorpayOrderId,
                razorpayPaymentId = razorpayPaymentId,
                razorpaySignature = razorpaySignature
            )
            walletRepository.verifyPayment(verifyRequest).fold(
                onSuccess = { response ->
                    if (response.success) {
                        val newBalance = response.data?.balance ?: _balance.value
                        _balance.value = newBalance
                        _razorpayCheckoutRequest.value = null
                        _walletState.value = WalletUiState.Success(
                            balance = newBalance,
                            message = response.message.ifBlank { context.getString(R.string.wallet_top_up_success) }
                        )
                    } else {
                        _walletState.value = WalletUiState.Error(
                            response.message.ifBlank { context.getString(R.string.error_payment_verification_failed) }
                        )
                    }
                },
                onFailure = { exception ->
                    Timber.e(exception, "Wallet payment verification failed")
                    _walletState.value = WalletUiState.Error(
                        exception.message ?: context.getString(R.string.error_payment_verification_failed)
                    )
                }
            )
        }
    }
}
