package com.yash091099.ChiragFarmersApp.ui.presentation.cart.payment

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import org.json.JSONObject

@Composable
fun PaymentScreen(
    navController: NavHostController,
    viewModel: PaymentViewModel,
    modifier: Modifier = Modifier,
    subtotal: Double = 0.0,
    totalDiscount: Double = 0.0,
    totalDeliveryFee: Double = 0.0,
    totalAmount: Double = 0.0
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val activity = context as? Activity
    val paymentState by viewModel.paymentState.collectAsState()
    val razorpayCheckoutRequest by viewModel.razorpayCheckoutRequest.collectAsState()
    var selectedPayment by remember { mutableStateOf<String?>(null) }

    val razorpayCheckout = remember { Checkout() }
    val razorpayListener = remember {
        object : com.razorpay.PaymentResultWithDataListener {
            override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
                viewModel.onPaymentSuccess(paymentData?.paymentId, paymentData?.orderId, paymentData?.signature)
            }
            override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
                viewModel.onPaymentError(code, response)
            }
        }
    }

    LaunchedEffect(razorpayCheckoutRequest) {
        val options = razorpayCheckoutRequest ?: return@LaunchedEffect
        try {
            val jsonOptions = JSONObject().apply {
                put("key", options.key)
                put("amount", options.amount)
                put("currency", options.currency)
                put("order_id", options.orderId)
                put("name", options.name)
                put("description", options.description)
                put("theme.color", options.themeColor)
                val prefill = JSONObject()
                options.prefillEmail?.let { prefill.put("email", it) }
                options.prefillContact?.let { prefill.put("contact", it) }
                put("prefill", prefill)
            }
            razorpayCheckout.setKeyID(options.key)
            activity?.let {
                razorpayCheckout.merchantActivityResult(it, 0, 0, null, razorpayListener, null)
                razorpayCheckout.open(it, jsonOptions)
            }
        } catch (e: Exception) {
            viewModel.onPaymentError(-1, e.message ?: "Failed to launch Razorpay checkout")
        } finally {
            viewModel.clearRazorpayCheckoutRequest()
        }
    }

    LaunchedEffect(paymentState) {
        when (val state = paymentState) {
            is PaymentUiState.Success -> {
                navController.navigate(Route.PaymentSuccess.path) {
                    popUpTo(navController.currentDestination?.id ?: 0) { inclusive = false }
                }
                viewModel.resetPaymentState()
            }
            is PaymentUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetPaymentState()
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ChiragTopBar(
                navController = navController, icon = R.drawable.ic_arrow, title = stringResource(R.string.payment_title)
            )
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 280.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.payment_more_options),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PaymentOptionItem(
                    icon = R.drawable.ic_card,
                    title = stringResource(R.string.payment_online_payment),
                    isSelected = selectedPayment == "Online",
                    onClick = { selectedPayment = "Online" },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PaymentOptionItem(
                    icon = R.drawable.icon_cod,
                    title = stringResource(R.string.payment_cash_on_delivery),
                    isSelected = selectedPayment == "Cash On Delivery",
                    onClick = { selectedPayment = "Cash On Delivery" },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(BGWhite)
                    .padding(16.dp)
                    .border(
                        width = 1.dp, color = BorderColour, shape = RoundedCornerShape(24.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.payment_subtotal),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = BGBlack
                    )
                    Text(
                        text = stringResource(R.string.product_price_format, "%.2f".format(subtotal)),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = BGBlack
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.payment_delivery_fee),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = BGBlack
                    )
                    Text(
                        text = stringResource(R.string.product_price_format, "%.2f".format(totalDeliveryFee)),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = BGBlack
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.payment_discount),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = BGBlack
                    )
                    Text(
                        text = stringResource(R.string.product_price_format, "%.2f".format(totalDiscount)),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = BGBlack
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.payment_total_cost),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BGBlack
                    )
                    Text(
                        text = stringResource(R.string.product_price_format, "%.2f".format(totalAmount)),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BGBlack
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (paymentState is PaymentUiState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(BGBlack, shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = BGWhite,
                            strokeWidth = 2.dp
                        )
                    }
                } else {
                    ChiragButton(
                        text = stringResource(R.string.payment_pay_now),
                        onClick = {
                            if (selectedPayment != null) {
                                viewModel.placeOrder(
                                    paymentMethod = selectedPayment ?: "Cash On Delivery"
                                )
                            }
                        },
                        enabled = selectedPayment != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentOptionItem(
    icon: Any?,
    title: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isSelected) BGBlack else BorderColour,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = coil.compose.rememberAsyncImagePainter(model = icon),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.Unspecified
                )
            }
        }

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black
            )
        }

        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (isSelected) BGWhite else Color.Transparent)
                .border(
                    width = 1.dp,
                    color = if (isSelected) BGBlack else BorderColour,
                    shape = CircleShape
                ), contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(BGBlack)
                )
            }
        }
    }
}
