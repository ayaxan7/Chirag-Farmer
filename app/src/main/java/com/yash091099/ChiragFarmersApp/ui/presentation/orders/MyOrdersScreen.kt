package com.yash091099.ChiragFarmersApp.ui.presentation.orders

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.*
import kotlinx.coroutines.launch

data class OrderItem(
    val id: String,
    val title: String,
    val seller: String,
    val price: String,
    val deliveryDate: String,
    val imageUrl: String
)

@Composable
fun MyOrdersScreen(
    navController: NavHostController
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    val tabs = listOf("Active", "Complete", "Cancelled")

    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = "Orders",
                icon = R.drawable.ic_arrow
            )
        },
        containerColor = BGWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = BGWhite,
                contentColor = BGBlack,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        height = 3.dp,
                        color = BGBlack
                    )
                },
                divider = {
                    HorizontalDivider(color = LightGray, thickness = 1.dp)
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = 15.sp,
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (pagerState.currentPage == index) Color.Black else TextGray
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top
            ) { page ->
                when (page) {
                    0 -> OrderList(status = "Active")
                    1 -> OrderList(status = "Complete")
                    2 -> OrderList(status = "Cancelled")
                }
            }
        }
    }
}

@Composable
fun OrderList(status: String) {
    // Mock data based on status
    val orders = when (status) {
        "Complete" -> listOf(
            OrderItem("1", "NEPTUNE BATTERY", "Geolife Agritech India Pvt Ltd", "Rs.1999.00", "Delivery by 7 June 2025", "https://images.unsplash.com/photo-1590400541360-b20340809382?q=80&w=200&auto=format&fit=crop"),
            OrderItem("2", "TOMATO - FARM FRESH", "Siddharth kisan", "Rs.49.00", "Delivery by 7 June 2025", "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?q=80&w=200&auto=format&fit=crop"),
            OrderItem("3", "ROUND POTATO", "Siddharth kisan", "Rs.29.00", "Delivery by 7 June 2025", "https://images.unsplash.com/photo-1518977676601-b53f02bad675?q=80&w=200&auto=format&fit=crop")
        )
        "Active" -> listOf(
             OrderItem("4", "POWER SPRAYER", "Agro Tools", "Rs.4500.00", "Arriving by 12 June 2025", "https://images.unsplash.com/photo-1622383563227-04401ab4e5ea?q=80&w=200&auto=format&fit=crop")
        )
        else -> emptyList()
    }

    if (orders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No $status Orders", color = TextGray, fontSize = 16.sp)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(orders) { order ->
                OrderCard(order)
            }
        }
    }
}

@Composable
fun OrderCard(order: OrderItem) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = order.imageUrl,
                contentDescription = order.title,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, BorderColour, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = order.seller,
                    fontSize = 13.sp,
                    color = TextGray,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = order.price,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = order.deliveryDate,
                    fontSize = 12.sp,
                    color = TextGray
                )
            }

            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1C1E)),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(text = "Drop Review", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        HorizontalDivider(color = LightGray.copy(alpha = 0.5f), thickness = 1.dp)
    }
}
