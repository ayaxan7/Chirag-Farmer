package com.yash091099.ChiragFarmersApp.ui.presentation.sell.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray

@Composable
fun ActiveOrdersScreen() {
    val mockOrders = listOf(
        OrderItem(
            orderId = "ORD-458926",
            productName = "ORGANIC BEAN SEEDS",
            buyerName = "Ramesh Kumar",
            contact = "7858 **** **",
            quantity = "5 Kg",
            amount = "₹1,999",
            location = "Sagar, MP",
            status = "Shipping Pending",
            imageUrl = "https://images.unsplash.com/photo-1592919016327-50503e41b04e?q=80&w=200&auto=format&fit=crop"
        ),
        OrderItem(
            orderId = "ORD-458926",
            productName = "HYBRID TOMATO SEEDS",
            buyerName = "Anita Sharma",
            contact = "7858 **** **",
            quantity = "5 Kg",
            amount = "₹850",
            location = "Bhopal , MP",
            status = "Packing in Progress",
            imageUrl = "https://images.unsplash.com/photo-1592919016327-50503e41b04e?q=80&w=200&auto=format&fit=crop"
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(mockOrders) { order ->
            OrderCard(order)
        }
    }
}

@Composable
fun OrderCard(order: OrderItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColour, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mock Image
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E0E0))
                ) {
                    AsyncImage(
                        model = order.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = order.productName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Row {
                        Text(
                            text = "Buyer",
                            fontSize = 14.sp,
                            color = TextGray,
                            modifier = Modifier.width(60.dp)
                        )
                        Text(
                            text = ": ${order.buyerName}",
                            fontSize = 14.sp,
                            color = TextGray
                        )
                    }
                    Row {
                        Text(
                            text = "Contact",
                            fontSize = 14.sp,
                            color = TextGray,
                            modifier = Modifier.width(60.dp)
                        )
                        Text(
                            text = ": ${order.contact}",
                            fontSize = 14.sp,
                            color = TextGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BorderColour.copy(alpha = 0.5f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Grid Details
            Row(modifier = Modifier.fillMaxWidth()) {
                DetailItem(label = "ORDER ID", value = order.orderId, modifier = Modifier.weight(1f))
                DetailItem(label = "QUANTITY", value = order.quantity, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                DetailItem(
                    label = "AMOUNT",
                    value = order.amount,
                    valueColor = Color(0xFF3BB69A),
                    modifier = Modifier.weight(1f)
                )
                DetailItem(label = "LOCATION", value = order.location, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFF4ED))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(0xFFF1614B), CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = order.status,
                    color = Color(0xFFF1614B),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* Handle click */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
            ) {
                Text(
                    text = "Update Order Status",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun DetailItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueColor: Color = Color.Black
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextGray,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.5.sp
        )
        Text(
            text = value,
            fontSize = 15.sp,
            color = valueColor,
            fontWeight = FontWeight.Bold
        )
    }
}

data class OrderItem(
    val orderId: String,
    val productName: String,
    val buyerName: String,
    val contact: String,
    val quantity: String,
    val amount: String,
    val location: String,
    val status: String,
    val imageUrl: String
)
