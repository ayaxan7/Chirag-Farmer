package com.yash091099.ChiragFarmersApp.ui.presentation.home

import android.util.Log
import timber.log.Timber
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.CategoryHeader
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.CommonProductCard
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ProductCategoryCard
import com.yash091099.ChiragFarmersApp.ui.presentation.common.data.CommonProductCardData
import com.yash091099.ChiragFarmersApp.ui.presentation.home.components.ImageCarousel
import com.yash091099.ChiragFarmersApp.ui.presentation.home.components.bookservicecard.BookServiceCard
import com.yash091099.ChiragFarmersApp.ui.presentation.home.components.topbar.HomeTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite

@Composable
fun HomeScreen(
    navController: NavHostController, viewModel: HomeViewModel = hiltViewModel()
) {
    val isProfileComplete by viewModel.isProfileComplete.collectAsStateWithLifecycle()
    val homeMixedProductsUiState by viewModel.homeMixedProductsUiState.collectAsStateWithLifecycle()

    val carouselImages = listOf(
        R.drawable.smart_farmer,
        R.drawable.smart_farmer,
        R.drawable.smart_farmer,
        R.drawable.smart_farmer
    )
    val productCategories = listOf(
        Pair(stringResource(R.string.home_agriculture_drones), R.drawable.agri_drone),
        Pair(stringResource(R.string.home_seeds), R.drawable.agri_seeds),
        Pair(stringResource(R.string.home_sprayer), R.drawable.agri_sprayer),
        Pair(stringResource(R.string.home_tractors), R.drawable.agri_tractor),
        Pair(stringResource(R.string.home_harvesting_machines), R.drawable.agri_harvester)
    )
    val bookingStatus by viewModel.bookingStatus.collectAsStateWithLifecycle()

    // Update default location every time HomeScreen is displayed
    LaunchedEffect(Unit) {
        viewModel.updateDefaultLocationOnScreenOpen()
    }

    // Sync FCM token every time HomeScreen is displayed
    LaunchedEffect(Unit) {
        viewModel.updateFcmDeviceTokenOnScreenOpen()
    }

    LaunchedEffect(bookingStatus) {
        when (bookingStatus) {
            is BookingStatus.Success -> {
                Timber.d("Booking Success: ${(bookingStatus as BookingStatus.Success).message}")
                viewModel.resetBookingStatus()
            }

            is BookingStatus.Error -> {
                Timber.e("Booking Error: ${(bookingStatus as BookingStatus.Error).message}")
                viewModel.resetBookingStatus()
            }

            else -> {}
        }
    }

    Scaffold(
        topBar = {
            HomeTopBar(navController)
        },
        containerColor = BGWhite,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
//                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                // Only show profile incomplete image if profile is not complete
                if (!isProfileComplete) {
                    Image(
                        painter = painterResource(R.drawable.profile_incomplete_image),
                        contentDescription = stringResource(R.string.home_profile_incomplete_image_description),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Route.Register.path)
                            },
                        contentScale = ContentScale.FillWidth
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                BookServiceCard(
                    navController = navController
                )
                // Smart Farmer Carousel
                ImageCarousel(
                    images = carouselImages, modifier = Modifier.fillMaxWidth()
                )

                // Buy Products Section
                CategoryHeader(
                    category = stringResource(R.string.home_buy_products_category),
                    btnText = stringResource(R.string.home_view_all),
                    onClick = {
                        navController.navigate(Route.Buy.path)
                    })
                val agriDroneLabel = stringResource(R.string.home_agriculture_drone_label)
                val seedsLabel = stringResource(R.string.home_seeds)
                val sprayerLabel = stringResource(R.string.home_sprayer)
                val sprayersLabel = stringResource(R.string.home_sprayers_label)
                val tractorsLabel = stringResource(R.string.home_tractors)
                val tractorsLabelRes = stringResource(R.string.home_tractors_label)
                val harvestingMachinesLabel = stringResource(R.string.home_harvesting_machines)
                val harvestingLabel = stringResource(R.string.home_harvesting_label)
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productCategories) { category ->
                        ProductCategoryCard(
                            title = category.first, imageRes = category.second, onClick = {
                                val categoryName = when {
                                    category.first.contains(
                                        agriDroneLabel.substringBefore(" "), ignoreCase = true
                                    ) -> agriDroneLabel

                                    category.first.equals(
                                        seedsLabel,
                                        ignoreCase = true
                                    ) -> seedsLabel

                                    category.first.equals(
                                        sprayerLabel, ignoreCase = true
                                    ) -> sprayersLabel

                                    category.first.equals(
                                        tractorsLabel, ignoreCase = true
                                    ) -> tractorsLabelRes

                                    category.first.equals(
                                        harvestingMachinesLabel, ignoreCase = true
                                    ) -> harvestingLabel

                                    else -> ""
                                }
                                if (categoryName.isNotEmpty()) {
                                    navController.navigate(
                                        Route.BuyCategory.createRoute(
                                            categoryName, R.drawable.buy_banner
                                        )
                                    )
                                }
                            })
                    }
                }

                // Display products based on UI state
                when (homeMixedProductsUiState) {
                    is HomeMixedProductsUiState.Loading -> {
                        Spacer(modifier = Modifier.height(32.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    is HomeMixedProductsUiState.Success -> {
                        val successState =
                            homeMixedProductsUiState as HomeMixedProductsUiState.Success

                        // Smart Farming Products — show max 2 items, 1 row of 2
                        val smartFarmingLabel = stringResource(R.string.home_smart_farming_label)
                        if (successState.smartFarmingProducts.isNotEmpty()) {
                            CategoryHeader(
                                category = stringResource(R.string.home_smart_farming_category),
                                btnText = stringResource(R.string.home_view_all),
                                onClick = {
                                    navController.navigate(
                                        Route.BuyCategory.createRoute(
                                            smartFarmingLabel, R.drawable.buy_banner
                                        )
                                    )
                                })
                            val smartFarmingItems = successState.smartFarmingProducts.take(2)
                            smartFarmingItems.chunked(2).forEach { rowItems ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    rowItems.forEach { product ->
                                        CommonProductCard(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(bottom = 12.dp),
                                            product = CommonProductCardData(
                                                imageRes = 0,
                                                imageUrl = product.imageUrl ?: "",
                                                productName = product.productName,
                                                brandName = product.sellerName,
                                                currentPrice = product.finalPrice.toString(),
                                                originalPrice = product.originalPrice.toString(),
                                                rating = product.rating
                                            ),
                                            onClick = {
                                                navController.navigate(
                                                    Route.ProductDetails.createRoute(
                                                        product.id
                                                    )
                                                )
                                            })
                                    }
                                    // Fill empty slot if odd number
                                    if (rowItems.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }

                        // Seeds Products — show max 2 items, 1 row of 2
                        val seedsCategoryLabel = stringResource(R.string.home_seeds_category)
                        if (successState.seedProducts.isNotEmpty()) {
                            CategoryHeader(
                                category = seedsCategoryLabel,
                                btnText = stringResource(R.string.home_view_all),
                                onClick = {
                                    navController.navigate(
                                        Route.BuyCategory.createRoute(
                                            seedsCategoryLabel, R.drawable.buy_banner
                                        )
                                    )
                                })
                            val seedItems = successState.seedProducts.take(2)
                            seedItems.chunked(2).forEach { rowItems ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    rowItems.forEach { product ->
                                        CommonProductCard(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(bottom = 12.dp),
                                            product = CommonProductCardData(
                                                imageRes = 0,
                                                imageUrl = product.imageUrl ?: "",
                                                productName = product.productName,
                                                brandName = product.sellerName,
                                                currentPrice = product.finalPrice.toString(),
                                                originalPrice = product.originalPrice.toString(),
                                                rating = product.rating
                                            ),
                                            onClick = {
                                                navController.navigate(
                                                    Route.ProductDetails.createRoute(
                                                        product.id
                                                    )
                                                )
                                            })
                                    }
                                    // Fill empty slot if odd number
                                    if (rowItems.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }

                        // Popular Products — show max 4 items, 2 rows of 2
                        if (successState.popularProducts.isNotEmpty()) {
                            val popularLabel = stringResource(R.string.home_popular_products_label)
                            CategoryHeader(
                                category = stringResource(R.string.home_popular_products_category),
                                btnText = stringResource(R.string.home_view_all),
                                onClick = {
                                    navController.navigate(
                                        Route.BuyCategory.createRoute(
                                            popularLabel, R.drawable.buy_banner
                                        )
                                    )
                                })
                            val popularItems = successState.popularProducts.take(4)
                            popularItems.chunked(2).forEach { rowItems ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    rowItems.forEach { product ->
                                        CommonProductCard(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(bottom = 12.dp),
                                            product = CommonProductCardData(
                                                imageRes = 0,
                                                imageUrl = product.imageUrl ?: "",
                                                productName = product.productName,
                                                brandName = product.sellerName,
                                                currentPrice = product.finalPrice.toString(),
                                                originalPrice = product.originalPrice.toString(),
                                                rating = product.rating
                                            ),
                                            onClick = {
                                                navController.navigate(
                                                    Route.ProductDetails.createRoute(
                                                        product.id
                                                    )
                                                )
                                            })
                                    }
                                    // Fill empty slot if odd number
                                    if (rowItems.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }

                    is HomeMixedProductsUiState.Error -> {
                        Spacer(modifier = Modifier.height(32.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.home_network_error),
//                                text = (homeMixedProductsUiState as HomeMixedProductsUiState.Error).message,
                                color = Color.Red, fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.retryHomeMixedProducts() }) {
                                Text(stringResource(R.string.home_retry))
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }

                CategoryHeader(
                    category = stringResource(R.string.home_usps_category)
                )
                Image(
                    painter = painterResource(R.drawable.our_usps),
                    contentDescription = stringResource(R.string.home_usps_description),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Image(
                painter = painterResource(R.drawable.home_footer),
                contentDescription = stringResource(R.string.home_footer_description),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                CategoryHeader(
                    category = stringResource(R.string.home_clients_category)
                )
                Image(
                    painter = painterResource(R.drawable.clients_list),
                    contentDescription = stringResource(R.string.home_clients_description),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}