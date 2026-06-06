package com.yash091099.ChiragFarmersApp.ui.presentation.orders.screens

import android.content.ClipData
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderDeliveryAddress
import com.yash091099.ChiragFarmersApp.data.remote.dto.OrderDetailsData
import com.yash091099.ChiragFarmersApp.data.remote.dto.StatusTimeline
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import com.yash091099.ChiragFarmersApp.data.remote.dto.CancellationDetailsDto
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.presentation.orders.CancelOrderState
import com.yash091099.ChiragFarmersApp.ui.presentation.orders.OrderDetailsUiState
import com.yash091099.ChiragFarmersApp.ui.presentation.orders.OrderDetailsViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun MyOrderDetailsScreen(
    navController: NavHostController,
    orderId: String,
    productId: String? = null,
    viewModel: OrderDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cancelOrderState by viewModel.cancelOrderState.collectAsStateWithLifecycle()
    var showCancelDialog by remember { mutableStateOf(false) }
    var selectedProductId by remember { mutableStateOf("") }
    var cancelReason by remember { mutableStateOf("") }

    LaunchedEffect(orderId) {
        if (orderId.isNotBlank()) {
            viewModel.loadOrderDetails(orderId)
        }
    }

    LaunchedEffect(cancelOrderState) {
        if (cancelOrderState is CancelOrderState.Success) {
            showCancelDialog = false
            // Reload order details after successful cancellation
            if (orderId.isNotBlank()) {
                viewModel.loadOrderDetails(orderId)
            }
            viewModel.resetCancelState()
        }
    }

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
            val primaryItem = (uiState as? OrderDetailsUiState.Success)?.data?.items?.firstOrNull()
            val actionMode = resolveBottomActionMode(
                (uiState as? OrderDetailsUiState.Success)?.data?.orderStatus
            )

            if (actionMode != BottomActionMode.NONE) {
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
                        border = BorderStroke(1.dp,BGBlack),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BGBlack)
                    ) {
                        Text("Reorder", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    }

                    if (actionMode == BottomActionMode.BOTH) {
                        Button(
                            onClick = {
                                navController.navigate(
                                    Route.DropReview.createRoute(
                                        orderId = orderId,
                                        productId = productId ?: primaryItem?.productId.orEmpty(),
                                        imageUrl = primaryItem?.imageUrl,
                                        productName = primaryItem?.productName,
                                        sellerName = primaryItem?.sellerName,
                                        pricePaid = primaryItem?.pricePaid?.toString()
                                    )
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                        ) {
                            Text(
                                "Drop Review",
                                color = BGWhite,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is OrderDetailsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BGBlack)
                }
            }

            is OrderDetailsUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = state.message, color = Color.Red, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { if (orderId.isNotBlank()) viewModel.loadOrderDetails(orderId) },
                        colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                    ) {
                        Text("Retry", color = BGWhite)
                    }
                }
            }

            is OrderDetailsUiState.Success -> {
                OrderDetailsContent(
                    data = state.data,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    orderId = orderId,
                    onCancelItem = { productId ->
                        selectedProductId = productId
                        cancelReason = ""
                        showCancelDialog = true
                    }
                )
            }
        }
    }

    if (showCancelDialog) {
        CancelOrderDialog(
            onDismiss = { showCancelDialog = false },
            onConfirm = { reason ->
                viewModel.cancelOrderItem(orderId, selectedProductId, reason)
            },
            isLoading = cancelOrderState is CancelOrderState.Loading
        )
    }
}

private enum class BottomActionMode {
    NONE,
    REORDER_ONLY,
    BOTH
}

private fun resolveBottomActionMode(orderStatus: String?): BottomActionMode {
    val normalized = orderStatus?.trim()?.lowercase(Locale.getDefault()).orEmpty()
    return when {
        normalized.contains("delivered") || normalized.contains("complete") -> BottomActionMode.BOTH
        normalized.contains("cancel") -> BottomActionMode.REORDER_ONLY
        else -> BottomActionMode.NONE
    }
}

@Composable
private fun OrderDetailsContent(
    data: OrderDetailsData,
    modifier: Modifier = Modifier,
    orderId: String = "",
    onCancelItem: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        data.items.forEach { item ->
            OrderProductCard(
                imageUrl = item.imageUrl,
                orderId = item.orderNumber.orEmpty(),
                productName = item.productName.orEmpty(),
                sellerName = item.sellerName.orEmpty(),
                price = formatCurrency(item.pricePaid),
                quantity = item.quantity.orEmpty(),
                itemStatus = item.itemStatus,
                cancellationDetails = item.cancellationDetails,
                productId = item.productId.orEmpty(),
                onCancelClick = { onCancelItem(item.productId.orEmpty()) },
                deliveryAddress=data.deliveryAddress
            )
        }
        DeliveredDateSection(data.statusTimeline?.delivered)
        OrderStatusSection(data.statusTimeline, data.orderStatus)
        PriceBreakdownSection(data)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun OrderProductCard(
    imageUrl: String?,
    orderId: String,
    productName: String,
    sellerName: String,
    price: String,
    quantity: String,
    itemStatus: String? = null,
    cancellationDetails: CancellationDetailsDto? = null,
    productId: String = "",
    onCancelClick: () -> Unit = {},
    deliveryAddress: OrderDeliveryAddress? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColour, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BGWhite),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
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
                        text = "#${orderId.ifBlank { "--" }}",
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

//            // Show item status if available
//            if (!itemStatus.isNullOrBlank()) {
//                HorizontalDivider(color = BorderColour.copy(alpha = 0.3f), thickness = 0.5.dp)
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(12.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Column {
//                        Text("Item Status", fontSize = 12.sp, color = TextGray)
//                        Text(
//                            text = itemStatus,
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = BGBlack
//                        )
//                    }
//
//                    // Show cancel button if item is not already cancelled
//                    if (itemStatus.lowercase() != "cancelled") {
//                        Button(
//                            onClick = onCancelClick,
//                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
//                            shape = RoundedCornerShape(6.dp),
//                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
//                            modifier = Modifier.height(32.dp)
//                        ) {
//                            Text("Cancel Item", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
//                        }
//                    }
//                }
//            }
        }
    }
    // Show cancellation details if item was cancelled
    if (!cancellationDetails?.cancelledAt.isNullOrBlank()) {
        val cancelledText = parseAndFormatDate(cancellationDetails.cancelledAt) ?: return

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.cancelled),
                contentDescription = "Cancelled",
                modifier = Modifier.size(20.dp),
                tint = Color(0xFFF95353)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Cancelled On: $cancelledText",
                color = Color(0xFFF95353),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
    DeliveryAddressSection(deliveryAddress)
}

@Composable
fun DeliveryAddressSection(address: OrderDeliveryAddress?) {
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
                    text = address?.name?.ifBlank { "--" } ?: "--",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = BGBlack
                )
                Text(
                    text = address?.completeAddress?.ifBlank { "--" } ?: "--",
                    fontSize = 13.sp,
                    color = TextGray,
                    lineHeight = 13.sp
                )
                Text(
                    text = "Pin : ${address?.pincode?.ifBlank { "--" } ?: "--"}",
                    fontSize = 13.sp,
                    lineHeight = 13.sp,
                    color = TextGray
                )
            }
        }
    }
}

@Composable
fun DeliveredDateSection(deliveredAt: String?) {
    val deliveredText = parseAndFormatDate(deliveredAt)
    if (deliveredText == null) return

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
            text = "Delivered Date: $deliveredText",
            color = Color(0xFFF1614B),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun OrderStatusSection(statusTimeline: StatusTimeline?, currentStatus: String?) {
    val steps = listOf(
        "Order\nPlaced" to (statusTimeline?.placed != null),
        "Packed" to (statusTimeline?.packed != null),
        "Shipped" to (statusTimeline?.shipped != null),
        "Out for\nDelivery" to (statusTimeline?.outForDelivery != null),
        "Delivered" to (statusTimeline?.delivered != null)
    )
    val activeIndex = getActiveStatusIndex(currentStatus, steps)

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
            steps.forEachIndexed { index, step ->
                val label = step.first
                val isCompleted = step.second
                val isActive = !isCompleted && activeIndex == index
                val filled = isCompleted || isActive
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(if (filled) BGBlack else LightGray, CircleShape),
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
                            tint = if (filled) BGWhite else TextGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    if (index < steps.size - 1) {
                        val nextCompleted = steps[index + 1].second
                        val connectorColor = if (isCompleted && nextCompleted) BGBlack else Color(0xFFB0B0B0)

                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .offset(x = 20.dp)
                        ) {
                            drawLine(
                                color = connectorColor,
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
                        color = if (filled) BGBlack else TextGray,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        lineHeight = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PriceBreakdownSection(data: OrderDetailsData) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColour, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PriceRow("Sub-Total", formatCurrency(data.subtotal))
        HorizontalDivider(color = BorderColour.copy(alpha = 0.5f), thickness = 0.5.dp)
        PriceRow("Delivery Fee", formatCurrency(data.deliveryFee))
        HorizontalDivider(color = BorderColour.copy(alpha = 0.5f), thickness = 0.5.dp)
        PriceRow("Discount", formatCurrency(data.discount))
        HorizontalDivider(color = BorderColour.copy(alpha = 0.5f), thickness = 0.5.dp)
        PriceRow("Payment Method", data.paymentMethod?.ifBlank { "--" } ?: "--")
        HorizontalDivider(color = BorderColour.copy(alpha = 0.5f), thickness = 0.5.dp)
        PriceRow("Order Date", parseAndFormatDate(data.orderDate) ?: "--")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(!data.transactionId.isNullOrBlank()) {
                HorizontalDivider(color = BorderColour.copy(alpha = 0.5f), thickness = 0.5.dp)

                Text("Transaction ID", fontSize = 14.sp, color = TextGray)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val transactionId = data.transactionId.ifBlank { "--" }
                    Text(
                        transactionId,
                        fontSize = 14.sp,
                        color = BGBlack,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = TextGray,
                        modifier = Modifier
                            .size(16.dp).clickable(enabled = transactionId != "--") {
                                scope.launch {
                                    clipboard.setClipEntry(
                                        ClipEntry(
                                            ClipData.newPlainText(
                                                "transaction_id",
                                                transactionId
                                            )
                                        )                                    )
                                }
                            }
                    )
                }
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

private fun formatCurrency(value: Double?): String {
    return value?.let { "₹${String.format(Locale.US, "%.2f", it)}" } ?: "--"
}

private fun parseAndFormatDate(dateString: String?): String? {
    if (dateString.isNullOrBlank()) return null

    val inputFormats = listOf(
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    )

    inputFormats.drop(1).forEach { it.timeZone = TimeZone.getTimeZone("UTC") }
    val outputFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

    inputFormats.forEach { format ->
        runCatching { format.parse(dateString) }.getOrNull()?.let { parsed ->
            return outputFormat.format(parsed)
        }
    }

    return dateString
}

private fun getActiveStatusIndex(currentStatus: String?, steps: List<Pair<String, Boolean>>): Int {
    val normalizedCurrent = currentStatus?.trim()?.lowercase(Locale.getDefault()).orEmpty()
    val mappedIndex = when (normalizedCurrent) {
        "order placed" -> 0
        "packed", "in progress" -> 1
        "shipped" -> 2
        "out for delivery" -> 3
        "delivered" -> 4
        else -> -1
    }

    return if (mappedIndex >= 0) mappedIndex else steps.indexOfFirst { !it.second }
}

@Composable
fun CancelOrderDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    isLoading: Boolean = false
) {
    var reason by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = {
            Text(
                text = "Cancel Item",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Please provide a reason for cancellation:",
                    fontSize = 14.sp,
                    color = TextGray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                TextField(
                    value = reason,
                    onValueChange = { reason = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    label = { Text("Reason") },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = BGBlack,
                        unfocusedIndicatorColor = BorderColour
                    ),
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(reason) },
                enabled = reason.isNotBlank() && !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Confirm", color = Color.White)
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !isLoading,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = BGBlack)
            ) {
                Text("Cancel")
            }
        }
    )
}
