package com.yash091099.ChiragFarmersApp.ui.presentation.sell.screens.orderstatus

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.*

@Composable
fun OrderStatusScreen(
    navController: NavHostController,
    orderId: String? = null
) {
    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = "Order Status",
                icon = R.drawable.ic_arrow,
                buttonText = "Sell Product",
                buttonIcon = Icons.Default.Add,
                onButtonClick = { /* Navigate to sell product */ }
            )
        },
        containerColor = BGWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Search Bar Placeholder
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                placeholder = { Text("Search for Products", color = TextGray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextGray) },
                trailingIcon = { Icon(Icons.Default.FilterList, contentDescription = null, tint = Color.Black) },
                shape = RoundedCornerShape(28.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = BorderColour,
                    unfocusedIndicatorColor = BorderColour,
                ),
                readOnly = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs Placeholder
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TabItem("Active Products", false)
                TabItem("Products Sold Out", false)
                TabItem("Active Orders", true)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Order Summary Card
            OrderSummaryCard()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Update Progress",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1A1C1E)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Timeline
            ProgressTimeline()
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TabItem(text: String, isSelected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.Black else TextGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(3.dp)
                    .background(Color.Black, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
            )
        }
    }
}

@Composable
fun OrderSummaryCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColour, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Product Image Placeholder
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFDE7D2)) // Mocking the image color from screenshot
                ) {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center).size(40.dp),
                        tint = Color.White.copy(alpha = 0.5f)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "HYBRID TOMATO\nSEEDS",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            color = Color(0xFF1A1C1E)
                        )
                        Text(
                            text = "₹850",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Teal
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Order ID: TRK456898268", fontSize = 13.sp, color = TextGray)
                    Text(text = "Quantity : 2kg", fontSize = 13.sp, color = TextGray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BorderColour.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Delivery Address", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextGray)
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextGray)
            }
        }
    }
}

@Composable
fun ProgressTimeline() {
    Column {
        TimelineItem(
            title = "Delivery Date",
            subtitle = "Estimated delivery",
            icon = Icons.Default.ShoppingCart,
            isCompleted = true,
            showLine = true
        ) {
            CustomDateField("10/12/2023")
        }

        TimelineItem(
            title = "Packing Stage",
            subtitle = "Packing Date",
            icon = Icons.Default.Inventory,
            isCompleted = true,
            showLine = true
        ) {
            Column {
                CustomDateField("10/12/2023")
                Spacer(modifier = Modifier.height(12.dp))
                Text("Notes (Optional)", fontSize = 13.sp, color = TextGray)
                Spacer(modifier = Modifier.height(4.dp))
                CustomTextField("Add specific packing notes...")
            }
        }

        TimelineItem(
            title = "Shipping Stage",
            subtitle = "Shipping Date",
            icon = Icons.Default.Inventory2,
            isCompleted = false,
            isActive = true,
            showLine = true
        ) {
            Column {
                CustomDateField("mm/dd/yyyy")
                Spacer(modifier = Modifier.height(12.dp))
                Text("Tracking Number", fontSize = 13.sp, color = TextGray)
                Spacer(modifier = Modifier.height(4.dp))
                CustomTextField("Enter tracking ID")
            }
        }

        TimelineItem(
            title = "Out for Delivery",
            subtitle = "Delivery Date",
            icon = Icons.Default.LocalShipping,
            isCompleted = false,
            isFaded = true,
            showLine = true
        ) {
            CustomDateField("mm/dd/yyyy")
        }

        TimelineItem(
            title = "Delivered",
            subtitle = "Confirmation Delivered Date",
            icon = Icons.Default.CheckCircleOutline,
            isCompleted = false,
            isFaded = true,
            showLine = false
        ) {
            CustomDateField("mm/dd/yyyy")
        }
    }
}

@Composable
fun TimelineItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isCompleted: Boolean,
    isActive: Boolean = false,
    isFaded: Boolean = false,
    showLine: Boolean = true,
    content: @Composable () -> Unit
) {
    val contentColor = if (isFaded) TextGray.copy(alpha = 0.5f) else Color.Black
    val iconBgColor = if (isFaded) Color(0xFFF5F5F5) else Color(0xFFF5F5F5)
    val borderColor = if (isActive) Teal else BorderColour

    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (isFaded) TextGray.copy(alpha = 0.5f) else Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
            if (showLine) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .weight(1f, fill = false)
                        .height(IntrinsicSize.Min)
                        .defaultMinSize(minHeight = 40.dp)
                        .background(BorderColour, shape = RoundedCornerShape(1.dp))
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp)
                .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = contentColor
                        )
                        Text(
                            text = subtitle,
                            fontSize = 12.sp,
                            color = if (isFaded) TextGray.copy(alpha = 0.5f) else TextGray
                        )
                    }
                    if (isCompleted) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color.Black, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(1.dp, BorderColour, CircleShape)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                content()
            }
        }
    }
}

@Composable
fun CustomDateField(value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFFF9F9F9), RoundedCornerShape(8.dp))
            .border(1.dp, BorderColour.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = value, color = if (value.contains("m/d")) TextGray else Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CustomTextField(placeholder: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFFF9F9F9), RoundedCornerShape(8.dp))
            .border(1.dp, BorderColour.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = placeholder, color = TextGray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
