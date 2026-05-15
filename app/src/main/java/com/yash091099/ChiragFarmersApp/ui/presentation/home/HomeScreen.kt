package com.yash091099.ChiragFarmersApp.ui.presentation.home

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.CategoryHeader
import com.yash091099.ChiragFarmersApp.ui.presentation.common.data.CommonProductCardData
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ProductCategoryCard
import com.yash091099.ChiragFarmersApp.ui.presentation.home.components.bookservicecard.BookServiceCard
import com.yash091099.ChiragFarmersApp.ui.presentation.home.components.ImageCarousel
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.CommonProductCard
import com.yash091099.ChiragFarmersApp.ui.presentation.home.components.topbar.HomeTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val isProfileComplete by viewModel.isProfileComplete.collectAsStateWithLifecycle()
    val homeMixedProductsUiState by viewModel.homeMixedProductsUiState.collectAsStateWithLifecycle()

    val carouselImages = listOf(
        R.drawable.smart_farmer,
        R.drawable.smart_farmer,
        R.drawable.smart_farmer,
        R.drawable.smart_farmer
    )

    val productCategories = listOf(
        Pair("Agriculture\nDrones", R.drawable.agri_drone),
        Pair("Seeds", R.drawable.agri_seeds),
        Pair("Sprayer", R.drawable.agri_sprayer),
        Pair("Tractors",R.drawable.agri_tractor),
        Pair("Harvesting Machines",R.drawable.agri_harvester)
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
                Log.d("HomeScreen", "Booking Success: ${(bookingStatus as BookingStatus.Success).message}")
                viewModel.resetBookingStatus()
            }
            is BookingStatus.Error -> {
                Log.e("HomeScreen", "Booking Error: ${(bookingStatus as BookingStatus.Error).message}")
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Only show profile incomplete image if profile is not complete
            if (!isProfileComplete) {
                Image(
                    painter = painterResource(R.drawable.profile_incomplete_image),
                    contentDescription = "Profile Incomplete Image",
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
                images = carouselImages,
                modifier = Modifier.fillMaxWidth()
            )

            // Buy Products Section
            CategoryHeader(
                category = "Buy Products For Your Farm",
                btnText = "View All",
                onClick = {
                    navController.navigate(Route.Buy.path)
                }
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productCategories) { category ->
                    ProductCategoryCard(
                        title = category.first,
                        imageRes = category.second,
                        onClick = {
                            val categoryName = when {
                                category.first.contains("Agriculture", ignoreCase = true) -> "Agriculture Drone"
                                category.first.equals("Seeds", ignoreCase = true) -> "Seeds"
                                category.first.equals("Sprayer", ignoreCase = true) -> "Sprayers"
                                category.first.equals("Tractors", ignoreCase = true) -> "Tractors"
                                category.first.equals("Harvesting Machines", ignoreCase = true) -> "Harvesting Machines"
                                else -> ""
                            }
                            if (categoryName.isNotEmpty()) {
                                navController.navigate(
                                    Route.BuyCategory.createRoute(categoryName, R.drawable.buy_banner)
                                )
                            }
                        }
                    )
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
                    val successState = homeMixedProductsUiState as HomeMixedProductsUiState.Success

                    // Smart Farming Products — show max 2 items, 1 row of 2
                    if (successState.smartFarmingProducts.isNotEmpty()) {
                        CategoryHeader(
                            category = "Smart Farming",
                            btnText = "View All",
                            onClick = {
                                navController.navigate(Route.BuyCategory.createRoute("Smart Farming", R.drawable.buy_banner))
                            }
                        )
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
                                            rating = "4.5"
                                        ),
                                        onClick = {
                                            navController.navigate(Route.ProductDetails.createRoute(product.id))
                                        }
                                    )
                                }
                                // Fill empty slot if odd number
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    // Seeds Products — show max 2 items, 1 row of 2
                    if (successState.seedProducts.isNotEmpty()) {
                        CategoryHeader(
                            category = "Seeds",
                            btnText = "View All",
                            onClick = {
                                navController.navigate(Route.BuyCategory.createRoute("Seeds", R.drawable.buy_banner))
                            }
                        )
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
                                            rating = "4.5"
                                        ),
                                        onClick = {
                                            navController.navigate(Route.ProductDetails.createRoute(product.id))
                                        }
                                    )
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
                        CategoryHeader(
                            category = "Popular Products",
                            btnText = "View All",
                            onClick = {
                                navController.navigate(Route.BuyCategory.createRoute("Popular Products", R.drawable.buy_banner))
                            }
                        )
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
                                            rating = "4.5"
                                        ),
                                        onClick = {
                                            navController.navigate(Route.ProductDetails.createRoute(product.id))
                                        }
                                    )
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
                            text = (homeMixedProductsUiState as HomeMixedProductsUiState.Error).message,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.retryHomeMixedProducts() }) {
                            Text("Retry")
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            CategoryHeader(
                category = "Our USPs"
            )
            Image(
                painter = painterResource(R.drawable.our_usps),
                contentDescription = "USPs",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = "You are successfully logged in as a farmer.",
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    scope.launch {
                        // Logout using ViewModel
                        viewModel.logout()
                        // Navigate back to log in screen
                        navController.navigate(Route.Auth.path) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            ) {
                Text(text = "Logout")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}