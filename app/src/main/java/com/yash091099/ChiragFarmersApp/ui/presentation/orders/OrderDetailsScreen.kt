package com.yash091099.ChiragFarmersApp.ui.presentation.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas

@Composable
fun OrderDetailsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = "Orders Details",
                icon = R.drawable.ic_arrow
            )
        },
        containerColor = BGWhite,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BGWhite)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BGBlack)
                ) {
                    Text("Reorder", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }
                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                ) {
                    Text("Drop Review", color = BGWhite, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Product Items ---
            OrderProductCard(
                imageUrl = "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?q=80&w=200",
                orderId = "#ORD-1245",
                productName = "NEPTUNE BATTERY",
                sellerName = "Geolife Agritech India Pvt Ltd",
                price = "₹49.00",
                quantity = "1 Kg"
            )
            OrderProductCard(
                imageUrl = "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?q=80&w=200",
                orderId = "#ORD-1245",
                productName = "NEPTUNE BATTERY",
                sellerName = "Geolife Agritech India Pvt Ltd",
                price = "₹49.00",
                quantity = "1 Kg"
            )

            // --- Delivery Address ---
            DeliveryAddressSection()

            // --- Delivered Date ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color(0xFFF1614B),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Delivered Date: 7 June 2025",
                    color = Color(0xFFF1614B),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // --- Order Status ---
            OrderStatusSection()

            // --- Price Breakdown ---
            PriceBreakdownSection()

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun OrderProductCard(
    imageUrl: String,
    orderId: String,
    productName: String,
    sellerName: String,
    price: String,
    quantity: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColour, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BGWhite),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = productName,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = orderId,
                    fontSize = 12.sp,
                    color = TextGray
                )
                Text(
                    text = productName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = BGBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = sellerName,
                    fontSize = 12.sp,
                    color = TextGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = price,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BGBlack
                    )
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF2F2F2), RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = quantity,
                            fontSize = 12.sp,
                            color = TextGray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DeliveryAddressSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Delivery Address",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BGBlack
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "Location Pin",
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Home",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = BGBlack
                )
                Text(
                    text = "123 MG Road, Indiranagar, Bengaluru, Karnataka",
                    fontSize = 13.sp,
                    color = TextGray,
                    lineHeight = 13.sp
                )
                Text(
                    text = "Pin : 560038",
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    color = TextGray
                )
            }
//            Spacer(modifier = Modifier.width(8.dp))
//            Button(
//                onClick = {},
//                shape = RoundedCornerShape(20.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = BGBlack),
//                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
//                modifier = Modifier.height(32.dp)
//            ) {
//                Text("Change", color = BGWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
//            }
        }
    }
}

@Composable
fun OrderStatusSection() {
    val steps = listOf("Order\nPlace", "In\nProgress", "Shipped", "Out for\nDelivery", "Delivered")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColour, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Order Status",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BGBlack
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            steps.forEachIndexed { index, label ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(BGBlack, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        val icons = listOf(
                            R.drawable.ic_ordersplaced,
                            R.drawable.ic_ordersplaced,
                            R.drawable.ic_ordersplaced,
                            R.drawable.ic_ordersplaced,
                            R.drawable.ic_ordersplaced
                        )
                        Icon(
                            painter = painterResource(id = icons[index]),
                            contentDescription = label,
                            tint = BGWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    if (index < steps.size - 1) {
                        // Dashed connector line drawn via Canvas
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .offset(x = 20.dp)
                        ) {
                            drawLine(
                                color = Color(0xFFB0B0B0),
                                start = Offset(0f, size.height / 2),
                                end = Offset(size.width, size.height / 2),
                                strokeWidth = 2f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        color = BGBlack,
                        fontWeight = FontWeight.Medium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PriceBreakdownSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColour, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PriceRow("Sub-Total", "₹3,135.00")
        HorizontalDivider(color = BorderColour.copy(alpha = 0.5f), thickness = 0.5.dp)
        PriceRow("Delivery Fee", "₹100.00")
        HorizontalDivider(color = BorderColour.copy(alpha = 0.5f), thickness = 0.5.dp)
        PriceRow("Discount", "₹875.00")
        HorizontalDivider(color = BorderColour.copy(alpha = 0.5f), thickness = 0.5.dp)
        PriceRow("Payment Method", "UPI")
        HorizontalDivider(color = BorderColour.copy(alpha = 0.5f), thickness = 0.5.dp)
        PriceRow("Order Date", "May 24  2025, 02:15 PM")
        HorizontalDivider(color = BorderColour.copy(alpha = 0.5f), thickness = 0.5.dp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Transaction ID", fontSize = 14.sp, color = TextGray)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "TRK123468268",
                    fontSize = 14.sp,
                    color = BGBlack,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    modifier = Modifier.size(16.dp),
                    tint = TextGray
                )
            }
        }
    }
}

@Composable
fun PriceRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = TextGray)
        Text(value, fontSize = 14.sp, color = BGBlack, fontWeight = FontWeight.Medium)
    }
}
