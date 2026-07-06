package com.yash091099.ChiragFarmersApp.ui.presentation.profile.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.razorpay.Checkout
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.wallet.WalletUiState
import com.yash091099.ChiragFarmersApp.ui.presentation.wallet.WalletViewModel
import org.json.JSONObject

@Composable
fun WalletScreen(
    navController: NavHostController,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val localeTag = LocalConfiguration.current.locales[0]
    val balance by viewModel.balance.collectAsState()
    val walletState by viewModel.walletState.collectAsState()
    val razorpayCheckoutRequest by viewModel.razorpayCheckoutRequest.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var topUpAmount by remember { mutableStateOf("") }
    val razorpayCheckout = remember { Checkout() }
    val razorpayListener = remember {
        object : com.razorpay.PaymentResultWithDataListener {
            override fun onPaymentSuccess(
                razorpayPaymentId: String?, paymentData: com.razorpay.PaymentData?
            ) {
                viewModel.onPaymentSuccess(
                    paymentData?.paymentId, paymentData?.orderId, paymentData?.signature
                )
            }

            override fun onPaymentError(
                code: Int, response: String?, paymentData: com.razorpay.PaymentData?
            ) {
                viewModel.onPaymentError(code, response)
            }
        }
    }

    LaunchedEffect(razorpayCheckoutRequest) {
        val options = razorpayCheckoutRequest ?: return@LaunchedEffect
        val activity = context as? android.app.Activity ?: return@LaunchedEffect
        val langTag = localeTag.toLanguageTag()
        razorpayCheckout.merchantActivityResult(
            activity, 0, 0, null, razorpayListener, null
        )
        try {
            val jsonOptions = JSONObject().apply {
                put("key", options.key)
                put("amount", options.amount)
                put("currency", options.currency)
                put("order_id", options.orderId)
                put("name", options.name)
                put("description", options.description)
                put("theme.color", options.themeColor)
                put("language", langTag)
                val prefill = JSONObject()
                options.prefillEmail?.let { prefill.put("email", it) }
                options.prefillContact?.let { prefill.put("contact", it) }
                put("prefill", prefill)
            }
            razorpayCheckout.setKeyID(options.key)
            razorpayCheckout.open(activity, jsonOptions)
        } catch (e: Exception) {
            viewModel.onPaymentError(
                -1, e.message ?: "Failed to launch Razorpay checkout"
            )
        } finally {
            viewModel.clearRazorpayCheckoutRequest()
        }
    }

    LaunchedEffect(walletState) {
        when (val state = walletState) {
            is WalletUiState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                topUpAmount = ""
                viewModel.resetWalletState()
            }
            is WalletUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetWalletState()
            }
            else -> {}
        }
    }

    val isProcessing = walletState is WalletUiState.Loading

    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = stringResource(R.string.wallet_title),
                icon = R.drawable.ic_arrow,
                onBackClick = { navController.popBackStack() }
            )
        },
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Balance Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF2C2C2C), Color(0xFF1E1E1E)
                                )
                            )
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 40.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(150.dp)
                                .background(
                                    Color.White.copy(alpha = 0.03f),
                                    RoundedCornerShape(100.dp)
                                )
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.wallet_balance_label),
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "₹%,.0f".format(balance),
                            color = Color.White,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = stringResource(R.string.wallet_withdraw_amount_label),
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                BasicTextField(
                    value = topUpAmount,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            topUpAmount = input
                        }
                    },
                    enabled = !isProcessing,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(
                        color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Normal
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(10.dp))
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (topUpAmount.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.wallet_top_up_hint),
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            // Loading overlay
            if (isProcessing) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            val buttonDescRes=stringResource(R.string.wallet_invalid_amount)
            // Bottom Button
            ChiragButton(
                text = stringResource(R.string.wallet_enter_button),
                onClick = {
                    val amount = topUpAmount.toDoubleOrNull()
                    if (amount == null || amount <= 0) {
                        viewModel.onPaymentError(-1, description = buttonDescRes)
                    } else {
                        viewModel.addMoney(amount)
                    }
                },
                enabled = !isProcessing,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
}
