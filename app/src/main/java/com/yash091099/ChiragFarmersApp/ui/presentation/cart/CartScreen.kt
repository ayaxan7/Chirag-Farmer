package com.yash091099.ChiragFarmersApp.ui.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.components.CartItemCard
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.components.SwipeToRevealItem
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour

@Composable
fun CartScreen(
    navController: NavHostController, viewModel: CartViewModel, modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val cartState by viewModel.cartState.collectAsState()

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
                        navController = navController, icon = R.drawable.ic_arrow, title = "My Cart"
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
                            text = "Error Loading Cart",
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
                            Text("Retry", color = BGWhite)
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
                        navController = navController, icon = R.drawable.ic_arrow, title = "My Cart"
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
                            text = "Your Cart is Empty",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BGBlack
                        )
                        Text(
                            text = "Add items to get started", fontSize = 14.sp, color = Color.Gray
                        )
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                        ) {
                            Text("Continue Shopping", color = BGWhite)
                        }
                    }
                }
            }
        }

        is CartUiState.Success -> {
            val cartItems = state.cartItems
            val summary = state.summary

            Scaffold(
                modifier = modifier,
                containerColor = BGWhite,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    ChiragTopBar(
                        navController = navController, icon = R.drawable.ic_arrow, title = "My Cart"
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
                        // Cart Items
                        cartItems.forEach { item ->
                            SwipeToRevealItem(
                                onDelete = {
                                    viewModel.deleteItem(item.productId)
                                }
                            ) {
                                CartItemCard(
                                    imageRes = R.drawable.sprayer,
                                    imageUrl = item.productImage ?: "",
                                    productName = item.productName,
                                    sellerName = item.sellerName,
                                    price = "₹${item.finalPrice}",
                                    deliveryDate = "Delivery by 7 June 2025",
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
                                    .align(Alignment.CenterHorizontally),
                                thickness = (0.5f).dp
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
                                width = 1.dp, color = BorderColour, shape = RoundedCornerShape(24.dp)
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
                                text = "Sub-Total",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = BGBlack
                            )
                            Text(
                                text = "₹${summary.subtotal}",
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
                                text = "Delivery Fee",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = BGBlack
                            )
                            Text(
                                text = "₹${summary.totalDeliveryFee}",
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
                                text = "Discount",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = BGBlack
                            )
                            Text(
                                text = "₹%.2f".format(summary.totalDiscount),
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
                                text = "Total Cost",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BGBlack
                            )
                            Text(
                                text = "₹%.2f".format(summary.totalAmount),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BGBlack
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        ChiragButton(
                            text = "Proceed to Checkout",
                            onClick = { /* Handle checkout */ },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
