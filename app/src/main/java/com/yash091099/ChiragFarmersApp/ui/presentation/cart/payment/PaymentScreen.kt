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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite

@Composable
fun PaymentScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedPayment by remember { mutableStateOf("Google pay") }

    Scaffold(
        modifier = modifier,
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ChiragTopBar(
                navController = navController,
                icon = R.drawable.ic_back_arrow,
                title = "Payment"
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
                    iconRes = R.drawable.ic_card,
                    title = "Add Card",
                    isSelected = false,
                    showChevron = true,
                    onClick = { /* Navigate to add card */ },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // UPI Payments Section
                Text(
                    text = "Upi Payments",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Google Pay with special treatment
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    PaymentOptionItem(
                        iconRes = R.drawable.ic_card,
                        title = "Google pay",
                        offersText = "2 Offers",
                        isSelected = selectedPayment == "Google pay",
                        onClick = { selectedPayment = "Google pay" }
                    )

                    // Show button when Google Pay is selected
                    if (selectedPayment == "Google pay") {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { /* Pay with Google Pay */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BGBlack
                            )
                        ) {
                            Text(
                                text = "Pay using Google Pay",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                PaymentOptionItem(
                    iconRes = R.drawable.ic_card,
                    title = "Paytm",
                    isSelected = selectedPayment == "Paytm",
                    onClick = { selectedPayment = "Paytm" },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PaymentOptionItem(
                    iconRes = R.drawable.ic_card,
                    title = "Phonepe",
                    offersText = "2 Offers",
                    isSelected = selectedPayment == "Phonepe",
                    onClick = { selectedPayment = "Phonepe" },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

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
                    iconRes = R.drawable.ic_card,
                    title = "Add Kisan Card",
                    isSelected = selectedPayment == "Add Kisan Card",
                    onClick = { selectedPayment = "Add Kisan Card" },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PaymentOptionItem(
                    iconRes = R.drawable.ic_card,
                    title = "Kisan Wallet",
                    isSelected = selectedPayment == "Kisan Wallet",
                    onClick = { selectedPayment = "Kisan Wallet" },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PaymentOptionItem(
                    iconRes = R.drawable.ic_card,
                    title = "Net Banking",
                    isSelected = selectedPayment == "Net Banking",
                    onClick = { selectedPayment = "Net Banking" },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PaymentOptionItem(
                    iconRes = R.drawable.ic_card,
                    title = "Slice",
                    isSelected = selectedPayment == "Slice",
                    onClick = { selectedPayment = "Slice" },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PaymentOptionItem(
                    iconRes = R.drawable.ic_card,
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
                        text = "₹3,135.00",
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
                        text = "₹100.00",
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
                        text = "₹875.00",
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
                        text = "₹2,378.00",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pay Now Button
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
                        text = "Pay Now",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
@Composable
fun PaymentOptionItem(
    iconRes: Int?,
    title: String,
    offersText: String? = null,
    isSelected: Boolean,
    showChevron: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Icon
        if (iconRes != null) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF8F8F8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.size(20.dp),
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
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            if (offersText != null) {
                Text(
                    text = offersText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF00BCD4)
                )
            }
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
                    .background(if (isSelected) BGBlack else Color.Transparent)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) BGBlack else BorderColour,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }
        }
    }
}