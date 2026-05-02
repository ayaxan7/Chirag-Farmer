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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.domain.model.Order
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray

@Composable
fun ActiveOrdersScreen(
    viewModel: ActiveOrdersViewModel = hiltViewModel()
) {
    val ordersState by viewModel.ordersState.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()

    ActiveOrdersContent(
        state = ordersState,
        currentPage = currentPage,
        onRetry = { viewModel.retry() },
        onPreviousPage = { viewModel.previousPage() },
        onNextPage = { viewModel.nextPage() }
    )
}

@Composable
fun ActiveOrdersContent(
    state: ActiveOrdersState,
    currentPage: Int,
    onRetry: () -> Unit,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit
) {
    when (state) {
        is ActiveOrdersState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BGBlack)
            }
        }

        is ActiveOrdersState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Error Loading Orders",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BGBlack
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.message,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                ) {
                    Text("Retry", color = Color.White)
                }
            }
        }

        is ActiveOrdersState.Success -> {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.orders) { order ->
                        OrderCard(order)
                    }
                }

                // Pagination Controls
                if (state.totalPages > 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onPreviousPage,
                            enabled = currentPage > 1,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                        ) {
                            Text("← Previous", color = Color.White)
                        }

                        Text(
                            text = "Page $currentPage/${state.totalPages}",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            onClick = onNextPage,
                            enabled = currentPage < state.totalPages,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                        ) {
                            Text("Next →", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
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
                        model = order.productImage,
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
                            text = ": ${order.buyerContact}",
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
                    value = "₹${order.amountPaid}",
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

@Preview(showBackground = true)
@Composable
fun OrderCardPreview() {
    val mockOrder = Order(
        orderObjectId = "1",
        orderId = "ORD12345",
        productName = "Organic Tomatoes",
        productImage = "",
        buyerName = "John Doe",
        buyerContact = "+91 9876543210",
        quantity = "10 kg",
        amountPaid = 500,
        location = "Mumbai, Maharashtra",
        status = "Pending"
    )
    Box(modifier = Modifier.padding(16.dp)) {
        OrderCard(order = mockOrder)
    }
}

@Preview(showBackground = true)
@Composable
fun ActiveOrdersScreenPreview() {
    val mockOrders = listOf(
        Order(
            orderObjectId = "1",
            orderId = "ORD12345",
            productName = "Organic Tomatoes",
            productImage = "",
            buyerName = "John Doe",
            buyerContact = "+91 9876543210",
            quantity = "10 kg",
            amountPaid = 500,
            location = "Mumbai, Maharashtra",
            status = "Pending"
        ),
        Order(
            orderObjectId = "2",
            orderId = "ORD12346",
            productName = "Fresh Potatoes",
            productImage = "",
            buyerName = "Jane Smith",
            buyerContact = "+91 9123456789",
            quantity = "20 kg",
            amountPaid = 800,
            location = "Pune, Maharashtra",
            status = "Pending"
        )
    )
    ActiveOrdersContent(
        state = ActiveOrdersState.Success(
            orders = mockOrders,
            total = 2,
            page = 1,
            totalPages = 1
        ),
        currentPage = 1,
        onRetry = {},
        onPreviousPage = {},
        onNextPage = {}
    )
}
