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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderTrackingData
import com.yash091099.ChiragFarmersApp.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrderStatusScreen(
    navController: NavHostController,
    orderId: String? = null,
    viewModel: OrderStatusViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(orderId) {
        orderId?.let { viewModel.getOrderTracking(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BGWhite)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Order Status",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        when (val currentState = state) {
            is OrderStatusState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Teal)
                }
            }
            is OrderStatusState.Success -> {
                val data = currentState.data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    // Tabs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        TabItem(text = "Order Tracking", isSelected = true)
                        TabItem(text = "Order Details", isSelected = false)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    OrderSummaryCard(data)

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                        ProgressTimeline(
                            orderId = orderId ?: "",
                            data = data,
                            onStatusUpdate = { status ->
                                orderId?.let {
                                    viewModel.updateOrderStatus(it, data.productId.orEmpty(), status)
                                }
                            }
                        )
                    }
                }
            }
            is OrderStatusState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = currentState.message, color = Color.Red, textAlign = TextAlign.Center)
                }
            }
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
                    .width(100.dp)
                    .height(3.dp)
                    .background(Color.Black, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
            )
        }
    }
}

@Composable
fun OrderSummaryCard(data: OrderTrackingData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColour, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Product Image
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    AsyncImage(
                        model = data.imageUrl,
                        contentDescription = data.productName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
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
                            text = (data.productName ?: "").uppercase(),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            color = Color(0xFF1A1C1E)
                        )
                        Text(
                            text = "₹${data.amountPaid?.toInt() ?: 0}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Teal
                        )
                    }
                    Text(text = "Order ID: ${data.orderNumber ?: ""}", fontSize = 13.sp, color = TextGray, lineHeight = 18.sp)
                    Text(text = "Quantity : ${data.quantity ?: ""}", fontSize = 13.sp, color = TextGray, lineHeight = 18.sp)
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Delivery Address",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextGray
                    )
                    Text(
                        text = data.deliveryAddress?.completeAddress ?: "",
                        fontSize = 13.sp,
                        color = TextGray,
                        lineHeight = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextGray)
            }
        }
    }
}

@Composable
fun ProgressTimeline(
    orderId: String,
    data: OrderTrackingData,
    onStatusUpdate: (String) -> Unit
) {
    val statuses = listOf(
        Triple("Order Placed", "Placed On", data.orderPlacedAt),
        Triple("Packed", "Packing Date", data.packedAt),
        Triple("Shipped", "Shipping Date", data.shippedAt),
        Triple("Out for Delivery", "Delivery Date", data.outForDeliveryAt),
        Triple("Delivered", "Confirmation Delivered Date", data.deliveredAt)
    )

    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        statuses.forEachIndexed { index, (status, subtitle, timestamp) ->
            TimelineItem(
                title = status,
                subtitle = subtitle,
                icon = when (status) {
                    "Order Placed" -> Icons.Default.ShoppingCart
                    "Packed" -> Icons.Default.Inventory
                    "Shipped" -> Icons.Default.Inventory2
                    "Out for Delivery" -> Icons.Default.LocalShipping
                    "Delivered" -> Icons.Default.CheckCircleOutline
                    else -> Icons.Default.ShoppingCart
                },
                isCompleted = timestamp != null,
                isActive = timestamp == null && (index == 0 || statuses[index - 1].third != null),
                isFaded = timestamp == null && (index > 0 && statuses[index - 1].third == null),
                showLine = index < statuses.size - 1,
                onCheckClick = { if (timestamp == null) onStatusUpdate(status) }
            ) {
                CustomDateField(timestamp?.let { formatDate(it) } ?: "Pending")
            }
        }
    }
}

fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: "Pending"
    } catch (e: Exception) {
        try {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormat.timeZone = TimeZone.getTimeZone("UTC")
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = isoFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: "Pending"
        } catch (e2: Exception) {
            dateString
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
    onCheckClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val contentColor = if (isFaded) TextGray.copy(alpha = 0.5f) else Color.Black
    val iconBgColor = if (isFaded) Color(0xFFF5F5F5) else Color(0xFFF5F5F5)
    val borderColor = if (isActive) Teal else if (isCompleted) Color.Black else BorderColour

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
                        .height(60.dp)
                        .background(BorderColour)
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = contentColor,
                            lineHeight = 16.sp
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
                                .background(Color.Black, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Checked",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else if (isActive) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(1.dp, Color.Black, RoundedCornerShape(6.dp))
                                .clickable { onCheckClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            // Empty box for checking
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(1.dp, BorderColour, RoundedCornerShape(6.dp))
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
        Text(
            text = value,
            color = if (value == "Pending") TextGray else Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
