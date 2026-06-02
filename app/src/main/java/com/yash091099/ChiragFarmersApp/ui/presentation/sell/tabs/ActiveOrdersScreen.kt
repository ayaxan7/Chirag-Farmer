package com.yash091099.ChiragFarmersApp.ui.presentation.sell.tabs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.domain.model.Order
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.screens.orderstatus.OrderSummaryCard
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.screens.orderstatus.ProgressTimeline
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderColour
import com.yash091099.ChiragFarmersApp.ui.theme.ErrorRed
import com.yash091099.ChiragFarmersApp.ui.theme.LightGray
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray

@Composable
fun ActiveOrdersScreen(
    navController: NavHostController,
    selectedOrderId: String? = null,
    onOrderClick: (String?) -> Unit = {},
    viewModel: ActiveOrdersViewModel = hiltViewModel()
) {
    val orders = viewModel.activeOrders.collectAsLazyPagingItems()
    val orderTrackingState by viewModel.orderTrackingState.collectAsState()

    LaunchedEffect(selectedOrderId) {
        if (selectedOrderId != null) {
            viewModel.selectOrder(selectedOrderId)
        }
    }

    BackHandler(enabled = selectedOrderId != null) {
        onOrderClick(null)
    }

    if (selectedOrderId == null) {
        ActiveOrdersContent(
            orders = orders, navController = navController, onOrderClick = onOrderClick
        )
    } else {
        when (val state = orderTrackingState) {
            is OrderTrackingState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BGBlack)
                }
            }

            is OrderTrackingState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = state.message, color = Color.Red, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onOrderClick(selectedOrderId) },
                        colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                    ) {
                        Text("Retry", color = BGWhite)
                    }
                }
            }

            is OrderTrackingState.Success -> {
                OrderDetailsView(
                    orderId = selectedOrderId,
                    data = state.data,
                    navController = navController,
                    onStatusUpdate = { status ->
                        viewModel.updateOrderStatus(
                            selectedOrderId, state.data.productId.orEmpty(), status
                        )
                    },
                    onNavigateBack = {
                        onOrderClick(null)
                    }
                )
            }

            is OrderTrackingState.Idle -> Unit
        }
    }
}

@Composable
fun ActiveOrdersContent(
    orders: LazyPagingItems<Order>,
    navController: NavHostController,
    onOrderClick: (String?) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Scrollable Content
        Box(
            modifier = Modifier.weight(1f)
        ) {
            when {

                orders.loadState.refresh is LoadState.Loading && orders.itemCount == 0 -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BGBlack)
                    }
                }

                orders.loadState.refresh is LoadState.Error && orders.itemCount == 0 -> {
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
                            color = BGBlack,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { orders.retry() }, colors = ButtonDefaults.buttonColors(
                                containerColor = BGBlack
                            )
                        ) {
                            Text("Retry", color = BGWhite)
                        }
                    }
                }

                orders.itemCount == 0 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No active orders yet",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(
                            top = 16.dp, bottom = 16.dp
                        ), verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            count = orders.itemCount, key = { index ->
                                orders[index]?.orderObjectId ?: index
                            }) { index ->

                            orders[index]?.let { order ->
                                OrderCard(
                                   order= order, onOrderClick = onOrderClick
                                )
                            }
                        }
                        when (orders.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = BGBlack)
                                    }
                                }
                            }

                            is LoadState.Error -> {
                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Couldn't load more orders",
                                            color = BGBlack,
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = { orders.retry() },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = BGBlack
                                            )
                                        ) {
                                            Text("Retry", color = BGWhite)
                                        }
                                    }
                                }
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, onOrderClick: (String?) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColour, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BGWhite)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
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
                        contentDescription = "Product name " + order.productName,
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
                        color = BGBlack
                    )
                    Row {
                        Text(
                            text = "Buyer",
                            fontSize = 14.sp,
                            color = TextGray,
                            modifier = Modifier.width(60.dp)
                        )
                        Text(
                            text = ": ${order.buyerName}", fontSize = 14.sp, color = TextGray
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
                            text = ": ${order.buyerContact}", fontSize = 14.sp, color = TextGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = BorderColour.copy(alpha = 0.5f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            // Grid Details
            Row(modifier = Modifier.fillMaxWidth()) {
                DetailItem(
                    label = "ORDER ID", value = order.orderId, modifier = Modifier.weight(1f)
                )
                DetailItem(
                    label = "QUANTITY", value = order.quantity, modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                DetailItem(
                    label = "AMOUNT",
                    value = "₹${order.amountPaid}",
                    valueColor = Color(0xFF3BB69A),
                    modifier = Modifier.weight(1f)
                )
                DetailItem(
                    label = "LOCATION", value = order.location, modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = LightGray
            )
            Spacer(modifier = Modifier.height(8.dp))
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

            Spacer(modifier = Modifier.height(8.dp))
            ChiragButton(
                text = "Update Order Status",
                onClick = {
                    onOrderClick(order.orderObjectId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp),
                fontSize = 14.sp
            )
        }
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
fun OrderDetailsView(
    orderId: String,
    data: com.yash091099.ChiragFarmersApp.data.remote.dto.OrderTrackingData,
    navController: NavHostController,
    onStatusUpdate: (String) -> Unit,
    onNavigateBack: () -> Unit = {},
    viewModel: ActiveOrdersViewModel = hiltViewModel()
) {
    var showCancelDialog by remember { mutableStateOf(false) }
    val cancelOrderState by viewModel.cancelOrderState.collectAsState()
    val canCancelOrder = !orderStatusIsCancelled(data.orderStatus)

    // Handle success and navigate back
    LaunchedEffect(cancelOrderState) {
        if (cancelOrderState is CancelOrderState.Success) {
            onNavigateBack()
            viewModel.resetCancelState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BGWhite)
    ) {

        // Scrollable Content
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                OrderSummaryCard(data = data)
            }
            item {
                Text(
                    text = "Update Progress",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BGBlack,
                )
            }
            item {
                ProgressTimeline(
                    orderId = orderId,
                    data = data,
                    onStatusUpdate = onStatusUpdate
                )
            }
        }
        // Fixed Bottom Button
        if (canCancelOrder) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BGWhite)
            ) {
                OutlinedButton(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = BGBlack
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = BGWhite,
                        contentColor = BGBlack
                    ),
                    enabled = cancelOrderState !is CancelOrderState.Loading
                ) {
                    if (cancelOrderState is CancelOrderState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = BGBlack)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel Order",
                        tint = BGBlack,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cancel Order",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    // Cancel Dialog
    if (showCancelDialog) {
        CancelOrderDialog(
            onDismiss = { showCancelDialog = false },
            onConfirm = {
                viewModel.cancelOrder(orderId, data.productId.orEmpty(), "")
                showCancelDialog = false
            },
            isLoading = cancelOrderState is CancelOrderState.Loading,
            errorMessage = if (cancelOrderState is CancelOrderState.Error) {
                (cancelOrderState as CancelOrderState.Error).message
            } else null
        )
    }
}

@Composable
fun CancelOrderDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = {
            Text(
                text = "Cancel Order",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Are you sure you want to cancel this order?",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                if (!errorMessage.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage,
                        fontSize = 12.sp,
                        color = Color.Red
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = BGWhite)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Cancel Order", color = BGWhite)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
                Text("Keep Order", color = BGBlack)
            }
        }
    )
}

@Composable
fun DetailItem(
    modifier: Modifier = Modifier, label: String, value: String, valueColor: Color = BGBlack
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
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun orderStatusIsCancelled(status: String?): Boolean {
    return status?.trim()?.lowercase() in listOf("cancelled", "delivered")
}

