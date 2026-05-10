package com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.seller

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.CommonProductCard
import com.yash091099.ChiragFarmersApp.ui.presentation.common.data.CommonProductCardData
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerProfileScreen(
    navController: NavHostController,
    sellerId: String,
    sellerName: String,
    sellerImage: String? = null
) {
    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController, title = "Seller Profile", icon = R.drawable.ic_arrow
            )
        }, containerColor = BGWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {

                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Image
                    AsyncImage(
                        model = sellerImage ?: R.drawable.sell_category_spices,
                        contentDescription = "Seller Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color.White, CircleShape),
                        contentScale = ContentScale.FillBounds
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = sellerName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BGBlack
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.size(18.dp)
                        )
                    }
//
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Icon(
//                            imageVector = Icons.Default.Star,
//                            contentDescription = null,
//                            tint = Color(0xFFFFC107),
//                            modifier = Modifier.size(16.dp)
//                        )
//                        Text(
//                            text = "4.8 (120 Reviews)",
//                            fontSize = 14.sp,
//                            color = Color.LightGray,
//                            modifier = Modifier.padding(start = 4.dp)
//                        )
//                    }
                }
            }

            // Stats Section

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatItem(value = "4.8 ⭐", label = "100 ratings")
                StatItem(label = "Products", value = "45")
                StatItem(label = "Followers", value = "1.2k")
                StatItem(label = "Share", icon = R.drawable.ic_share)
//                    StatItem(label = "Exp", value = "3 yrs")
            }

            // Products Section
            Text(
                text = "All Products",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(6) { index ->
                    CommonProductCard(
                        product = CommonProductCardData(
                            imageUrl = null,
                            imageRes = R.drawable.sprayer,
                            productName = "Product $index",
                            brandName = sellerName,
                            currentPrice = "1200",
                            originalPrice = "1500",
                            rating = "4.5"
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String? = null, icon: Int? = null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Share",
                tint = Color.Unspecified
            )
        } else if (value != null) {
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BGBlack)

        }
        Text(text = label, fontSize = 12.sp, color = TextGray)
    }
}
