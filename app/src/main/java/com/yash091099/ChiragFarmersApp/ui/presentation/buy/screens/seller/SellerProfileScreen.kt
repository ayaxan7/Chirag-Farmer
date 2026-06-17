package com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.seller

import android.content.Intent
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.CommonProductCard
import com.yash091099.ChiragFarmersApp.ui.presentation.common.data.CommonProductCardData
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.yash091099.ChiragFarmersApp.data.remote.dto.toDomain
import java.util.Locale
import com.yash091099.ChiragFarmersApp.utils.ShareUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerProfileScreen(
    navController: NavHostController,
    sellerImage: String? = null,
    viewModel: SellerProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = stringResource(R.string.seller_title),
                icon = R.drawable.ic_arrow
            )
        }, containerColor = BGWhite
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is SellerProfileUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is SellerProfileUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message, color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.retry() }) {
                            Text(stringResource(R.string.seller_retry))
                        }
                    }
                }

                is SellerProfileUiState.Success -> {
                    val sellerData = state.sellerDetails
                    Column(
                        modifier = Modifier.fillMaxSize()
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
                                    model = sellerData.seller.profileImageUrl ?: sellerImage ?: R.drawable.sell_category_spices,
                                    contentDescription = stringResource(R.string.seller_image_description),
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .border(3.dp, Color.White, CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = sellerData.seller.name,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BGBlack
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = stringResource(R.string.seller_verified_description),
                                        tint = Color(0xFF2196F3),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        // Stats Section
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            StatItem(
                                value = "${String.format(Locale.getDefault(), "%.1f", sellerData.seller.rating)} ⭐",
                                label = stringResource(R.string.seller_ratings_label, sellerData.seller.totalRatings.toString())
                            )
                            StatItem(label = stringResource(R.string.seller_products_label), value = sellerData.stats.totalListings.toString())
                            StatItem(label = stringResource(R.string.seller_sold_out_label), value = sellerData.stats.soldOutProducts.toString())
                            StatItem(
                                label = stringResource(R.string.seller_share_label),
                                icon = R.drawable.ic_share,
                                onClick = {
                                    val shareLink = ShareUtils.generateShareLink(
                                        type = "farmer",
                                        id = sellerData.seller.userId
                                    )
                                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, shareLink)
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.share_seller_profile)))
                                }
                            )
                        }

                        // Products Section
                        Text(
                            text = stringResource(R.string.seller_all_products),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        if (sellerData.products.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = stringResource(R.string.seller_no_products), color = TextGray)
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                items(sellerData.products) { productDto ->
                                    val product = productDto.toDomain()
                                    CommonProductCard(
                                        product = CommonProductCardData(
                                            imageUrl = product.imageUrl,
                                            imageRes = R.drawable.sprayer,
                                            productName = product.productName,
                                            brandName = product.sellerName,
                                            currentPrice = product.effectivePrice.toString(),
                                            originalPrice = product.originalPrice.toString(),
                                            rating = product.rating
                                        ),
                                        onClick = {
                                            navController.navigate(Route.ProductDetails.createRoute(product.productId))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String? = null, icon: Int? = null, onClick: (() -> Unit)? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(R.string.seller_share_description),
                tint = Color.Unspecified
            )
        } else if (value != null) {
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BGBlack)

        }
        Text(text = label, fontSize = 12.sp, color = TextGray)
    }
}
