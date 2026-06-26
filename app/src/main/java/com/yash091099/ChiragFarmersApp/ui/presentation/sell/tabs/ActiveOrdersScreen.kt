package com.yash091099.ChiragFarmersApp.ui.presentation.sell.tabs

import android.os.Build
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.yash091099.ChiragFarmersApp.R
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
import timber.log.Timber

private data class FilterOption(val labelRes: Int, val apiValue: String?)

@Composable
fun ActiveOrdersScreen(
    navController: NavHostController,
    selectedOrder: Pair<String, String>? = null,
    onOrderClick: (Pair<String, String>?) -> Unit = {},
    viewModel: ActiveOrdersViewModel = hiltViewModel()
) {
    val orders = viewModel.activeOrders.collectAsLazyPagingItems()
    val orderTrackingState by viewModel.orderTrackingState.collectAsState()

    LaunchedEffect(orders.loadState) {
        val refresh = orders.loadState.refresh
        val append = orders.loadState.append
        if (refresh is LoadState.Error) {
            Timber.tag("ActiveOrdersUI").e(
                refresh.error,
                "refresh error itemCount=%s device=%s",
                orders.itemCount,
                Build.MODEL
            )
        }
        if (append is LoadState.Error) {
            Timber.tag("ActiveOrdersUI")
                .e(append.error, "append error itemCount=%s", orders.itemCount)
        }
    }

    LaunchedEffect(selectedOrder) {
        if (selectedOrder != null) {
            val (orderId, productId) = selectedOrder
            Timber.tag("ActiveOrdersUI").d("selectedOrderId=%s productId=%s", orderId, productId)
            viewModel.selectOrder(orderId, productId.ifEmpty { null })
        }
    }

    BackHandler(enabled = selectedOrder != null) {
        viewModel.selectOrder(null)
        onOrderClick(null)
    }

    if (selectedOrder == null) {
        ActiveOrdersContent(
            orders = orders, navController = navController, onOrderClick = { id, pid ->
                viewModel.selectOrder(id, pid)
                onOrderClick(Pair(id, pid))
            }
        )
    } else {
        val (currentOrderId, currentProductId) = selectedOrder
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
                        onClick = {
                            viewModel.selectOrder(currentOrderId, currentProductId.ifEmpty { null })
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                    ) {
                        Text(stringResource(R.string.active_orders_retry), color = BGWhite)
                    }
                }
            }

            is OrderTrackingState.Success -> {
                OrderDetailsView(
                    orderId = currentOrderId,
                    data = state.data,
                    navController = navController,
                    onStatusUpdate = { status ->
                        viewModel.updateOrderStatus(
                            currentOrderId, state.data.productId.orEmpty(), status
                        )
                    },
                    onNavigateBack = {
                        viewModel.selectOrder(null)
                        onOrderClick(null)
                    })
            }

            is OrderTrackingState.Idle -> Unit
        }
    }
}

@Composable
fun ActiveOrdersContent(
    orders: LazyPagingItems<Order>,
    navController: NavHostController,
    onOrderClick: (String, String) -> Unit,
    viewModel: ActiveOrdersViewModel = hiltViewModel()
) {
    var expanded by remember { mutableStateOf(false) }

    val filterOptions = remember {
        listOf(
            FilterOption(R.string.active_orders_filter_all, null),
            FilterOption(R.string.active_orders_filter_packed, "packed"),
            FilterOption(R.string.active_orders_filter_shipped, "shipped"),
            FilterOption(R.string.active_orders_filter_out_for_delivery, "out_for_delivery"),
        )
    }

    var selectedFilterIndex by remember { mutableIntStateOf(0) }
    val selectedFilterLabel = stringResource(filterOptions[selectedFilterIndex].labelRes)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.active_orders_total_format, orders.itemCount),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = BGBlack
            )

            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(BGBlack)
                        .clickable { expanded = true }
                        .padding(horizontal = 12.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = selectedFilterLabel,
                        color = BGWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(R.drawable.ic_down_arrow),
                        contentDescription = "Down Arrow",
                        tint = BGWhite
                    )
                }

                DropdownMenu(
                    expanded = expanded, onDismissRequest = { expanded = false }) {
                    filterOptions.forEachIndexed { index, option ->
                        DropdownMenuItem(
                            text = { Text(stringResource(option.labelRes)) },
                            onClick = {
                                selectedFilterIndex = index
                                viewModel.setFilterStatus(option.apiValue)
                                expanded = false
                            })
                    }
                }
            }
        }

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
                            text = stringResource(R.string.active_orders_error),
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
                            Text(stringResource(R.string.active_orders_retry), color = BGWhite)
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
                            text = stringResource(R.string.active_orders_empty),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            count = orders.itemCount, key = { index ->
                                orders[index]?.let { "${it.orderObjectId}_${it.productId}" } ?: index
                            }) { index ->

                            orders[index]?.let { order ->
                                Timber.tag("ActiveOrdersUI").d(
                                    "order[%s] orderObjectId=%s orderId=%s product=%s buyer=%s amount=%s status=%s",
                                    index,
                                    order.orderObjectId,
                                    order.orderId,
                                    order.productName,
                                    order.buyerName,
                                    order.amountPaid,
                                    order.status
                                )
                                OrderCard(
                                    order = order, onOrderClick = onOrderClick
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
                                            text = stringResource(R.string.active_orders_load_more_error),
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
                                            Text(
                                                stringResource(R.string.active_orders_retry),
                                                color = BGWhite
                                            )
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
fun OrderCard(order: Order, onOrderClick: (String, String) -> Unit) {
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
                        contentDescription = stringResource(
                            R.string.active_orders_product_description_format,
                            order.productName
                        ),
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
                            text = stringResource(R.string.active_orders_buyer_label),
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
                            text = stringResource(R.string.active_orders_contact_label),
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
                    label = stringResource(R.string.active_orders_order_id_label),
                    value = order.orderId,
                    modifier = Modifier.weight(1f)
                )
                DetailItem(
                    label = stringResource(R.string.active_orders_quantity_label),
                    value = order.quantity,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                DetailItem(
                    label = stringResource(R.string.active_orders_amount_label),
                    value = stringResource(R.string.product_price_format, order.amountPaid),
                    valueColor = Color(0xFF3BB69A),
                    modifier = Modifier.weight(1f)
                )
                DetailItem(
                    label = stringResource(R.string.active_orders_location_label),
                    value = order.location,
                    modifier = Modifier.weight(1f)
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
                text = stringResource(R.string.active_orders_update_status),
                onClick = {
                    onOrderClick(order.orderObjectId, order.productId)
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
            modifier = Modifier.weight(1f), contentPadding = PaddingValues(
                vertical = 16.dp
            ), verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                OrderSummaryCard(data = data)
            }
            item {
                Text(
                    text = stringResource(R.string.active_orders_update_progress),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BGBlack,
                )
            }
            item {
                ProgressTimeline(
                    orderId = orderId, data = data, onStatusUpdate = onStatusUpdate
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
                        width = 1.dp, color = BGBlack
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = BGWhite, contentColor = BGBlack
                    ),
                    enabled = cancelOrderState !is CancelOrderState.Loading
                ) {
                    if (cancelOrderState is CancelOrderState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = BGBlack)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.active_orders_cancel_order_description),
                        tint = BGBlack,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.active_orders_cancel_order_text),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    // Cancel Dialog
    if (showCancelDialog) {
        CancelOrderBottomSheet(
            onDismiss = { showCancelDialog = false },
            onConfirm = { reason ->
                viewModel.cancelOrder(orderId, data.productId.orEmpty(), reason)
                showCancelDialog = false
            },
            isLoading = cancelOrderState is CancelOrderState.Loading,
            errorMessage = if (cancelOrderState is CancelOrderState.Error) {
                (cancelOrderState as CancelOrderState.Error).message
            } else null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancelOrderBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val reasonResIds = listOf(
        R.string.cancel_reason_ordered_mistake,
        R.string.cancel_reason_unable_deliver,
        R.string.cancel_reason_quality_issue,
        R.string.cancel_reason_out_of_stock,
        R.string.cancel_reason_incorrect_listing,
        R.string.cancel_reason_other
    )
    val reasons = reasonResIds.map { stringResource(it) }
    val otherIndex = reasonResIds.lastIndex
    var selectedIndex by remember { mutableIntStateOf(0) }
    val selectedReason = reasons[selectedIndex]
    var otherReason by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val enterReasonPlaceholder = stringResource(R.string.cancel_reason_enter_reason)

    ModalBottomSheet(
        onDismissRequest = { if (!isLoading) onDismiss() },
        sheetState = sheetState,
        containerColor = BGWhite,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.cancel_order),
                    contentDescription = stringResource(R.string.active_orders_cancel_order_description),
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.active_orders_cancel_confirm),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BGBlack
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.active_orders_cancel_confirm_message),
                fontSize = 14.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.cancel_reason_label),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BGBlack
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedReason,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_down_arrow),
                                contentDescription = stringResource(R.string.cancel_reason_label),
                                tint = BGBlack
                            )
                        },
                        modifier = Modifier
                            .menuAnchor(PrimaryNotEditable, enabled = true)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BGBlack,
                            unfocusedBorderColor = BorderColour,
                            focusedContainerColor = BGWhite,
                            unfocusedContainerColor = BGWhite
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(BGWhite)
                    ) {
                        reasons.forEachIndexed { index, reason ->
                            DropdownMenuItem(text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = reason, color = BGBlack, fontSize = 14.sp)
                                    RadioButton(
                                        selected = selectedIndex == index,
                                        onClick = null,
                                        colors = RadioButtonDefaults.colors(selectedColor = BGBlack)
                                    )
                                }
                            }, onClick = {
                                selectedIndex = index
                                if (index != otherIndex) {
                                    expanded = false
                                }
                            })
                        }
                    }
                }

                if (selectedIndex == otherIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = otherReason,
                        onValueChange = { otherReason = it },
                        placeholder = { Text(enterReasonPlaceholder, color = TextGray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BGBlack, unfocusedBorderColor = BorderColour
                        )
                    )
                }
            }

            if (!errorMessage.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = errorMessage, fontSize = 12.sp, color = ErrorRed
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { if (!isLoading) onDismiss() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, BGBlack),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BGBlack)
                ) {
                    Text(stringResource(R.string.cancel_go_back), fontWeight = FontWeight.Medium)
                }

                Button(
                    onClick = {
                        val finalReason =
                            if (selectedIndex == otherIndex) otherReason else selectedReason
                        onConfirm(finalReason)
                    },
                    enabled = !isLoading && (selectedIndex != otherIndex || otherReason.isNotBlank()),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BGBlack,
                        contentColor = BGWhite,
                        disabledContainerColor = Color.Gray
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = BGWhite,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(stringResource(R.string.cancel_yes_cancel_order), fontWeight = FontWeight.Medium)
                }
            }
        }
    }
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

