package com.yash091099.ChiragFarmersApp.ui.presentation.cart

import androidx.compose.foundation.background
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.cart.components.CartItemCard
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour

data class CartItem(
    val id: String,
    val imageRes: Int,
    val productName: String,
    val sellerName: String,
    val price: Double,
    val deliveryDate: String,
    var quantity: String
)

@Composable
fun CartScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Sample cart items
    var cartItems by remember {
        mutableStateOf(
            listOf(
                CartItem(
                    id = "1",
                    imageRes = R.drawable.sprayer,
                    productName = "NEPTUNE BATTERY OPERATED",
                    sellerName = "Geolite Agritech India Pvt Ltd",
                    price = 1999.00,
                    deliveryDate = "Delivery by 7 June 2025",
                    quantity = "1"
                ),
                CartItem(
                    id = "2",
                    imageRes = R.drawable.sprayer,
                    productName = "TOMATO - FARM FRESH",
                    sellerName = "Siddharth kisan",
                    price = 49.00,
                    deliveryDate = "Delivery by 7 June 2025",
                    quantity = "5 kgs"
                ),
                CartItem(
                    id = "3",
                    imageRes = R.drawable.sprayer,
                    productName = "ROUND POTATO",
                    sellerName = "Siddharth kisan",
                    price = 29.00,
                    deliveryDate = "Delivery by 7 June 2025",
                    quantity = "1"
                )
            )
        )
    }

    // Calculate totals
    val subTotal = remember(cartItems) {
        cartItems.sumOf { it.price }
    }
    val deliveryFee = 100.00
    val discount = 875.00
    val totalCost = subTotal + deliveryFee - discount

    Scaffold(
        modifier = modifier,
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ChiragTopBar(
                navController = navController,
                icon = R.drawable.ic_arrow,
                title = "My Cart"
            )
        }
    ) { paddingValues ->
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

                // Cart Items
                cartItems.forEach { item ->
                    CartItemCard(
                        imageRes = item.imageRes,
                        productName = item.productName,
                        sellerName = item.sellerName,
                        price = "Rs.${item.price.toInt()}.00",
                        deliveryDate = item.deliveryDate,
                        quantity = item.quantity,
                        onQuantityDecrease = {
                            // Handle decrease
                        },
                        onQuantityIncrease = {
                            // Handle increase
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
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
            ) {
                HorizontalDivider(
                    color = BorderColour,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Sub-Total
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Sub-Total",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                    Text(
                        text = "₹${subTotal.toInt()}.00",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Delivery Fee
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Delivery Fee",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                    Text(
                        text = "₹${deliveryFee.toInt()}.00",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Discount
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Discount",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                    Text(
                        text = "₹${discount.toInt()}.00",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(
                    color = BorderColour,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Total Cost
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Cost",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "₹${totalCost.toInt()}.00",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Proceed to Checkout Button
                Button(
                    onClick = { /* Navigate to checkout */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BGBlack
                    )
                ) {
                    Text(
                        text = "Proceed to Checkout",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}