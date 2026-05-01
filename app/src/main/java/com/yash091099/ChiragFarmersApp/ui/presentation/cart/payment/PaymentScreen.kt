package com.yash091099.ChiragFarmersApp.ui.presentation.cart.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray

@Composable
fun PaymentScreen(
    navController: NavHostController
) {
    var selectedPaymentMethod by remember { mutableStateOf("Cash on Delivery") }

    Scaffold(
        containerColor = BGWhite,
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = "Payment Method",
                icon = R.drawable.ic_arrow
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 120.dp)
            ) {
                // Progress Steps
                PaymentProgressSteps(currentStep = 1)

                Spacer(modifier = Modifier.height(24.dp))

                // Select Payment Method Section
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Select Payment Method",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BGBlack
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    PaymentOptionItem(
                        title = "Cash on Delivery",
                        isSelected = selectedPaymentMethod == "Cash on Delivery",
                        onClick = { selectedPaymentMethod = "Cash on Delivery" }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    PaymentOptionItem(
                        title = "UPI",
                        isSelected = selectedPaymentMethod == "UPI",
                        onClick = { selectedPaymentMethod = "UPI" }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    PaymentOptionItem(
                        title = "Credit / Debit Card",
                        isSelected = selectedPaymentMethod == "Credit / Debit Card",
                        onClick = { selectedPaymentMethod = "Credit / Debit Card" }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Price Details Section
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .border(1.dp, BorderColour, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Price Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BGBlack
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    PriceRow(label = "Item Total", value = "₹ 3,456")
                    PriceRow(label = "Delivery Fee", value = "₹ 50")
                    PriceRow(label = "Discount", value = "- ₹ 100", valueColor = Color(0xFF3BB69A))
                    PriceRow(label = "Handling Fee", value = "₹ 10")

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = BorderColour, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Payable",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = BGBlack
                        )
                        Text(
                            text = "₹ 3,456",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = BGBlack
                        )
                    }
                }
            }

            // Bottom Bar
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(BGWhite)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderColour, RoundedCornerShape(24.dp))
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Total", fontSize = 12.sp, color = TextGray)
                        Text(
                            text = "₹ 3,456",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BGBlack
                        )
                    }
                    ChiragButton(
                        text = "Place Order",
                        onClick = { /* Handle click */ },
                        modifier = Modifier.width(150.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentProgressSteps(currentStep: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Step 1: Address
        ProgressStep(label = "Address", isCompleted = true, isActive = false)
        
        // Line 1
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(2.dp)
                .background(BGBlack)
        )
        
        // Step 2: Payment
        ProgressStep(label = "Payment", isCompleted = false, isActive = true)
        
        // Line 2
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(2.dp)
                .background(Color(0xFFE0E0E0))
        )
        
        // Step 3: Summary
        ProgressStep(label = "Summary", isCompleted = false, isActive = false)
    }
}

@Composable
fun ProgressStep(label: String, isCompleted: Boolean, isActive: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (isCompleted || isActive) BGBlack else Color(0xFFE0E0E0))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isActive || isCompleted) BGBlack else Color(0xFF9E9E9E),
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun PaymentOptionItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 1.dp else 1.dp,
                color = if (isSelected) BGBlack else BorderColour,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Placeholder
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            // Icon will go here
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = BGBlack
        )
        
        // Selector
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (isSelected) BGBlack else Color.Transparent)
                .border(1.dp, if (isSelected) BGBlack else BorderColour, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun PriceRow(label: String, value: String, valueColor: Color = BGBlack) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = TextGray,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = valueColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}
