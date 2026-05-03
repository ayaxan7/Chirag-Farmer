package com.yash091099.ChiragFarmersApp.ui.presentation.cart.payment

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour

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
    val installedUpiApps by viewModel.installedUpiApps.collectAsState()
    var selectedPayment by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = modifier,
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ChiragTopBar(
                navController = navController, icon = R.drawable.ic_arrow, title = "Payment"
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
                    .padding(bottom = 280.dp) // Space for price summary
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                // Credit & Debit Cards Section
                Text(
                    text = "Credit & Debit Cards",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PaymentOptionItem(
                    icon = R.drawable.ic_card,
                    title = "Add Card",
                    isSelected = false,
                    showChevron = true,
                    onClick = { /* Navigate to add card */ },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // UPI Payments Section
                if (installedUpiApps.isNotEmpty()) {
                    Text(
                        text = "UPI Payments",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    installedUpiApps.forEach { app ->
                        PaymentOptionItem(
                            icon = app.icon,
                            title = app.name,
                            isSelected = selectedPayment == app.packageName,
                            onClick = { selectedPayment = app.packageName },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // More Payment Options Section
                Text(
                    text = "More Payments Options",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PaymentOptionItem(
                    icon = R.drawable.ic_card,
                    title = "Add Kisan Card",
                    isSelected = selectedPayment == "Add Kisan Card",
                    onClick = { selectedPayment = "Add Kisan Card" },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PaymentOptionItem(
                    icon = R.drawable.ic_card,
                    title = "Kisan Wallet",
                    isSelected = selectedPayment == "Kisan Wallet",
                    onClick = { selectedPayment = "Kisan Wallet" },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PaymentOptionItem(
                    icon = R.drawable.ic_card,
                    title = "Net Banking",
                    isSelected = selectedPayment == "Net Banking",
                    onClick = { selectedPayment = "Net Banking" },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PaymentOptionItem(
                    icon = R.drawable.ic_card,
                    title = "Slice",
                    isSelected = selectedPayment == "Slice",
                    onClick = { selectedPayment = "Slice" },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PaymentOptionItem(
                    icon = R.drawable.ic_card,
                    title = "Cash On Delivery",
                    isSelected = selectedPayment == "Cash On Delivery",
                    onClick = { selectedPayment = "Cash On Delivery" },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

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
                        text = "₹%.2f".format(subtotal),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = BGBlack
                    )
                }
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
                        text = "₹%.2f".format(totalDeliveryFee),
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
                        text = "₹%.2f".format(totalDiscount),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = BGBlack
                    )
                }
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
                        text = "₹%.2f".format(totalAmount),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BGBlack
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Show loading indicator or button based on operation status
                ChiragButton(
                    text = "Pay Now",
                    onClick = { navController.navigate(Route.Payment.path) },
                    enabled = selectedPayment != null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun PaymentOptionItem(
    icon: Any?,
    title: String,
    modifier: Modifier = Modifier,
    offersText: String? = null,
    isSelected: Boolean,
    showChevron: Boolean = false,
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
        // Icon
        if (icon != null) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(
                        width = 1.dp,
                        color = if (isSelected) BGBlack else BorderColour,
                        shape = RoundedCornerShape(6.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = coil.compose.rememberAsyncImagePainter(model = icon),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(0.8f),
                    tint = Color.Unspecified
                )
            }
        }

        // Title and Offers
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black
            )
        }

        // Right Icon (Chevron or Radio)
        if (showChevron) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        } else {
            // Radio button
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
}