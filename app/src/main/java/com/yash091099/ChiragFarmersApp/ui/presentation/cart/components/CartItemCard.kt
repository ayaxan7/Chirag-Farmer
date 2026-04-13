package com.yash091099.ChiragFarmersApp.ui.presentation.cart.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour

@Composable
fun CartItemCard(
    imageRes: Int,
    productName: String,
    sellerName: String,
    price: String,
    deliveryDate: String,
    quantity: String,
    onQuantityDecrease: () -> Unit,
    onQuantityIncrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = BGWhite
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Product Image
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = productName,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8F8F8)),
                contentScale = ContentScale.Crop
            )

            // Product Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Product Name
                Text(
                    text = productName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Seller Name
                Text(
                    text = sellerName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Price
                Text(
                    text = price,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Delivery Date
                Text(
                    text = deliveryDate,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF666666)
                )
            }

            // Quantity Controls
            Row(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = BorderColour,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Decrease Button
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Transparent)
                        .clickable { onQuantityDecrease() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "−",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }

                // Quantity Display
                Text(
                    text = quantity,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.width(40.dp)
                )

                // Increase Button
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(BGBlack)
                        .clickable { onQuantityIncrease() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = BGWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}