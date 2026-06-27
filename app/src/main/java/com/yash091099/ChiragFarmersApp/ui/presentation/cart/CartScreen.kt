package com.yash091099.ChiragFarmersApp.ui.presentation.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.razorpay.Checkout
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.components.CartItemCard
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.components.SwipeToRevealItem
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.payment.PaymentUiState
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.payment.PaymentViewModel
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import com.yash091099.ChiragFarmersApp.ui.theme.TextDarkGray
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray
import org.json.JSONObject

@Composable
fun CartScreen(
    navController: NavHostController,
    viewModel: CartViewModel,
    isBuyNow: Boolean = false,
    productId: String? = null,
    quantity: Int = 1,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val cartState by viewModel.cartState.collectAsState()
    val isOperationInProgress by viewModel.isOperationInProgress.collectAsState()

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, isBuyNow, productId, quantity) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (isBuyNow && productId != null) {
                    viewModel.initBuyNow(productId, quantity)
                } else {
                    viewModel.initLoadCart()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    when (val state = cartState) {
        is CartUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BGWhite),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BGBlack)
            }
        }

        is CartUiState.Error -> {
            Scaffold(
                modifier = modifier,
                containerColor = BGWhite,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    ChiragTopBar(
                        navController = navController,
                        icon = R.drawable.ic_arrow,
                        title = if (isBuyNow) stringResource(R.string.cart_top_bar_checkout) else stringResource(R.string.cart_top_bar_my_cart)
                    )
                }) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.cart_error_loading),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BGBlack
                        )
                        Text(
                            text = state.message, fontSize = 14.sp, color = Color.Gray
                        )
                        Button(
                            onClick = { viewModel.retry() },
                            colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                        ) {
                            Text(stringResource(R.string.cart_retry), color = BGWhite)
                        }
                    }
                }
            }
        }

        is CartUiState.Empty -> {
            Scaffold(
                modifier = modifier,
                containerColor = BGWhite,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    ChiragTopBar(
                        navController = navController,
                        icon = R.drawable.ic_arrow,
                        title = if (isBuyNow) stringResource(R.string.cart_top_bar_checkout) else stringResource(R.string.cart_top_bar_my_cart)
                    )
                }) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.cart_empty_title),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BGBlack
                        )
                        Text(
                            text = stringResource(R.string.cart_empty_subtitle), fontSize = 14.sp, color = Color.Gray
                        )
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                        ) {
                            Text(stringResource(R.string.cart_continue_shopping), color = BGWhite)
                        }
                    }
                }
            }
        }

        is CartUiState.Success -> {
            val cartItems = state.cartItems
            val summary = state.summary
            val address = state.address

            val paymentViewModel: PaymentViewModel = hiltViewModel()
            val paymentState by paymentViewModel.paymentState.collectAsState()
            val razorpayCheckoutRequest by paymentViewModel.razorpayCheckoutRequest.collectAsState()
            var selectedPaymentMethod by remember { mutableStateOf("Cash On Delivery") }
            var showPaymentDropdown by remember { mutableStateOf(false) }

            val razorpayCheckout = remember { Checkout() }
            val razorpayListener = remember {
                object : com.razorpay.PaymentResultWithDataListener {
                    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: com.razorpay.PaymentData?) {
                        paymentViewModel.onPaymentSuccess(paymentData?.paymentId, paymentData?.orderId, paymentData?.signature)
                    }
                    override fun onPaymentError(code: Int, response: String?, paymentData: com.razorpay.PaymentData?) {
                        paymentViewModel.onPaymentError(code, response)
                    }
                }
            }

            LaunchedEffect(razorpayCheckoutRequest) {
                val options = razorpayCheckoutRequest ?: return@LaunchedEffect
                val activity = context as? android.app.Activity ?: return@LaunchedEffect
                razorpayCheckout.merchantActivityResult(activity, 0, 0, null, razorpayListener, null)
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
                    razorpayCheckout.open(activity, jsonOptions)
                } catch (e: Exception) {
                    paymentViewModel.onPaymentError(-1, e.message ?: "Failed to launch Razorpay checkout")
                } finally {
                    paymentViewModel.clearRazorpayCheckoutRequest()
                }
            }

            LaunchedEffect(paymentState) {
                when (val state = paymentState) {
                    is PaymentUiState.Success -> {
                        navController.navigate(Route.PaymentSuccess.path) {
                            popUpTo(navController.currentDestination?.id ?: 0) { inclusive = false }
                        }
                        paymentViewModel.resetPaymentState()
                    }
                    is PaymentUiState.Error -> {
                        snackbarHostState.showSnackbar(state.message)
                        paymentViewModel.resetPaymentState()
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
                        navController = navController,
                        icon = R.drawable.ic_arrow,
                        title = if (isBuyNow) stringResource(R.string.cart_top_bar_checkout) else stringResource(R.string.cart_top_bar_my_cart)
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
                        // Shipping Address Section - Only shown if address is available
                        if (address != null) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.cart_shipping_address),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BGBlack
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.location),
                                        contentDescription = stringResource(R.string.cart_location_description),
                                        modifier = Modifier.size(24.dp),
                                        tint = BGBlack
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = address.name,
                                            fontSize = 14.sp,
                                            color = TextDarkGray,
                                            lineHeight = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = address.addressString,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = BGBlack,
                                            lineHeight = 16.sp,
                                            maxLines = 2
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row {
                                            Text(
                                                text = stringResource(R.string.cart_pin_label), fontSize = 13.sp, color = TextGray
                                            )
                                            Text(
                                                text = address.pincode,
                                                fontSize = 13.sp,
                                                color = BGBlack
                                            )
                                        }
                                    }
                                    Button(
                                        onClick = { navController.navigate(Route.AddressList.path) },
                                        colors = ButtonDefaults.buttonColors(containerColor = BGBlack),
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = PaddingValues(
                                            horizontal = 12.dp,
                                            vertical = 4.dp
                                        ),
                                        modifier = Modifier.defaultMinSize(
                                            minWidth = 1.dp,
                                            minHeight = 1.dp
                                        )
                                    ) {
                                        Text(
                                            text = stringResource(R.string.cart_change),
                                            fontSize = 12.sp,
                                            color = BGWhite,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(0.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            // Listed Products Section
                            Text(
                                text = stringResource(R.string.cart_listed_products),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BGBlack,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }


//                        Spacer(modifier = Modifier.height(16.dp))

                        // Cart Items
                        cartItems.forEach { item ->
                            SwipeToRevealItem(
                                onDelete = {
                                    viewModel.deleteItem(item.productId)
                                }) {
                                CartItemCard(
                                    imageRes = R.drawable.sprayer,
                                    imageUrl = item.productImage ?: "",
                                    productName = item.productName,
                                    sellerName = item.sellerName,
                                    price = stringResource(R.string.product_price_format, item.finalPrice),
                                    deliveryDate = stringResource(R.string.cart_delivery_by, "7 June 2025"),
                                    quantity = "${item.quantity}",
                                    onQuantityDecrease = {
                                        viewModel.decrementQuantity(item.productId)
                                    },
                                    onQuantityIncrease = {
                                        viewModel.incrementQuantity(item.productId)
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .align(Alignment.CenterHorizontally), thickness = (0.5f).dp
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Price Summary (Fixed at bottom)
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(BGWhite)
                            .padding(16.dp)
                            .border(
                                width = 1.dp,
                                color = BorderColour,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .padding(16.dp)
                    ) {

//                        Spacer(modifier = Modifier.height(16.dp))

                        // Sub-Total
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.cart_subtotal),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = BGBlack
                            )
                            Text(
                                text = stringResource(R.string.product_price_format, summary.subtotal),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = BGBlack
                            )
                        }

//                        Spacer(modifier = Modifier.height(8.dp))

                        // Delivery Fee
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.cart_delivery_fee),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = BGBlack
                            )
                            Text(
                                text = stringResource(R.string.product_price_format, summary.totalDeliveryFee),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = BGBlack
                            )
                        }

//                        Spacer(modifier = Modifier.height(8.dp))

                        // Discount
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.cart_discount),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = BGBlack
                            )
                            Text(
                                text = stringResource(R.string.product_price_format, "%.2f".format(summary.totalDiscount)),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = BGBlack
                            )
                        }

//                        Spacer(modifier = Modifier.height(12.dp))

//                        HorizontalDivider(
//                            color = BorderColour, thickness = 1.dp
//                        )

//                        Spacer(modifier = Modifier.height(12.dp))

                        // Total Cost
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.cart_total_cost),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BGBlack
                            )
                            Text(
                                text = stringResource(R.string.product_price_format, "%.2f".format(summary.totalAmount)),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BGBlack
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val isPaymentLoading = paymentState is PaymentUiState.Loading
                        if (isOperationInProgress || isPaymentLoading) {
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(modifier = Modifier.weight(0.4f)) {
                                    OutlinedButton(
                                        onClick = { showPaymentDropdown = true },
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, BGBlack),
                                        contentPadding = PaddingValues(horizontal = 12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = when (selectedPaymentMethod) {
                                                "Cash On Delivery" -> "Cash on Delivery"
                                                else -> "Online Payment"
                                            },
                                            fontSize = 12.sp,
                                            color = BGBlack
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = showPaymentDropdown,
                                        onDismissRequest = { showPaymentDropdown = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Cash on Delivery") },
                                            onClick = {
                                                selectedPaymentMethod = "Cash On Delivery"
                                                showPaymentDropdown = false
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Online Payment") },
                                            onClick = {
                                                selectedPaymentMethod = "Online"
                                                showPaymentDropdown = false
                                            }
                                        )
                                    }
                                }
                                ChiragButton(
                                    text = stringResource(R.string.cart_proceed_checkout),
                                    onClick = {
                                        if (address == null) {
                                            navController.navigate(Route.AddressList.path)
                                            return@ChiragButton
                                        }

                                        viewModel.cacheCartDataForPayment(
                                            cartItems = cartItems,
                                            address = address.addressString
                                        )
                                        paymentViewModel.placeOrder(selectedPaymentMethod)
                                    },
                                    modifier = Modifier.weight(0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
