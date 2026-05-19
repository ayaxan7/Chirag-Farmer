package com.yash091099.ChiragFarmersApp.ui.presentation.orders.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.LightGray
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropReviewScreen(
    navController: NavHostController, orderId: String
) {
    var rating by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController, title = "Drop Review", icon = R.drawable.ic_arrow
            )
        }, containerColor = BGWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BGWhite)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f), contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    // Item 1
                    ReviewItemCard(
                        imageRes = R.drawable.ic_launcher_background, // Replace with actual placeholder if needed
                        title = "NEPTUNE BATTERY",
                        seller = "Geolife Agritech India Pvt Ltd",
                        price = "Rs.1999.00",
                        deliveryDate = "Delivery by 7 June 2025"
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Item 2
                    ReviewItemCard(
                        imageRes = R.drawable.ic_launcher_background, // Replace with actual placeholder if needed
                        title = "TOMATO - FARM FRESH",
                        seller = "Siddharth kisan",
                        price = "Rs.49.00",
                        deliveryDate = "Delivery by 7 June 2025"
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Text(
                        text = "Your Over All Rating",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (i in 1..5) {
                            Icon(
                                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = "Star $i",
                                tint = if (i <= rating) Color(0xFFFFC107) else TextGray,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(4.dp)
                                    .clickable { rating = i })
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Text(
                        text = "Write Your Review",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    var reviewText by remember { mutableStateOf("") }
                    val maxChar = 400

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .drawBehind {
                            drawRoundRect(
                                color = Color.LightGray, style = Stroke(
                                    width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(
                                            10f, 10f
                                        ), 0f
                                    )
                                ), cornerRadius = CornerRadius(8.dp.toPx())
                            )
                        }
                        .padding(2.dp)) {
                        TextField(
                            value = reviewText, onValueChange = {
                            if (it.length <= maxChar) reviewText = it
                        }, placeholder = {
                            Text(
                                "Drop Your Review Here",
                                color = Color.DarkGray,
                                fontSize = 12.sp
                            )
                        }, colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ), modifier = Modifier.fillMaxSize()
                        )
                    }

                    Text(
                        text = "${maxChar - reviewText.length} characters Remaining",
                        fontSize = 10.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        textAlign = TextAlign.End
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // Bottom Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text("Cancel", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }

                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BGBlack
                    ),
                    enabled = rating > 0
                ) {
                    Text(
                        "Yes, Submit",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewItemCard(
    imageRes: Int, title: String, seller: String, price: String, deliveryDate: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, LightGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, LightGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black
            )
            Text(
                text = seller,
                fontSize = 12.sp,
                color = TextGray,
                modifier = Modifier.padding(top = 2.dp)
            )
            Text(
                text = price,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = deliveryDate,
                fontSize = 10.sp,
                color = TextGray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Button(
            onClick = { /* Re-Order logic */ },
            colors = ButtonDefaults.buttonColors(containerColor = BGBlack),
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.height(36.dp)
        ) {
            Text(
                text = "Re-Order",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
