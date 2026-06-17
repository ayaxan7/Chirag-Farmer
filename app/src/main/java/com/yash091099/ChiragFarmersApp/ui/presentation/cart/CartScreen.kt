package com.yash091099.ChiragFarmersApp.ui.presentation.cart

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.components.CartItemCard
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.components.SwipeToRevealItem
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import com.yash091099.ChiragFarmersApp.ui.theme.TextDarkGray
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray

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

                        // Show loading indicator or button based on operation status
                        if (isOperationInProgress) {
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
                                text = stringResource(R.string.cart_proceed_checkout),
                                onClick = {
                                    if (address == null) {
                                        navController.navigate(Route.AddressList.path)
                                        return@ChiragButton
                                    }

                                    // Cache cart items and address for payment screen
                                    viewModel.cacheCartDataForPayment(
                                        cartItems = cartItems,
                                        address = address.addressString
                                    )

                                    // Navigate to payment screen
                                    navController.navigate(
                                        Route.Payment.createRoute(
                                            subtotal = summary.subtotal,
                                            totalDiscount = summary.totalDiscount,
                                            totalDeliveryFee = summary.totalDeliveryFee,
                                            totalAmount = summary.totalAmount
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
