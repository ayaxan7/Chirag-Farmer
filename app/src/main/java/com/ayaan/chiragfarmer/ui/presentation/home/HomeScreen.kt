package com.ayaan.chiragfarmer.ui.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.common.components.CategoryHeader
import com.ayaan.chiragfarmer.ui.presentation.home.components.bookservicecard.BookServiceCard
import com.ayaan.chiragfarmer.ui.presentation.home.components.ImageCarousel
import com.ayaan.chiragfarmer.ui.presentation.home.components.CommonProductCard
import com.ayaan.chiragfarmer.ui.presentation.home.components.topbar.HomeTopBar
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.Route
import com.ayaan.chiragfarmer.ui.theme.BGWhite
import kotlinx.coroutines.launch

data class SmartFarmingProduct(
    val imageRes: Int,
    val name: String,
    val brand: String,
    val currentPrice: String,
    val originalPrice: String,
    val rating: String
)

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val isProfileComplete by viewModel.isProfileComplete.collectAsStateWithLifecycle()

    val carouselImages = listOf(
        R.drawable.smart_farmer,
        R.drawable.smart_farmer,
        R.drawable.smart_farmer,
        R.drawable.smart_farmer
    )

    // Sample Smart Farming Products Data
    val smartFarmingProducts = listOf(
        SmartFarmingProduct(
            imageRes = R.drawable.sprayer,
            name = "25 LITER POWER SPRAYER",
            brand = "Snap Export",
            currentPrice = "1599",
            originalPrice = "1899.00",
            rating = "4.8"
        ),
        SmartFarmingProduct(
            imageRes = R.drawable.sprayer,
            name = "25 LITER POWER SPRAYER",
            brand = "Snap Export",
            currentPrice = "1599",
            originalPrice = "1899.00",
            rating = "4.8"
        ),
    )

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
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Smart Farmer Carousel
            ImageCarousel(
                images = carouselImages,
                modifier = Modifier.fillMaxWidth()
            )

            // Book Service Form Card
            BookServiceCard(
                onBookNowClick = {
                    // Handle booking
                }
            )

            // Buy Products Section
            CategoryHeader(
                category = "Buy Products For Your Farm",
                btnText = "View All",
                onClick = {}
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(5) {
                    Card(
                        modifier = Modifier
                            .height(120.dp)
                            .width(116.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Image(
                            painter = painterResource(R.drawable.seeds_product),
                            contentDescription = "Seeds Product",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Smart Farming Section with Full Product Cards
            CategoryHeader(
                category = "Smart Farming",
                btnText = "View All",
                onClick = {}
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(smartFarmingProducts) { product ->
                    CommonProductCard(
                        imageRes = product.imageRes,
                        productName = product.name,
                        brandName = product.brand,
                        currentPrice = product.currentPrice,
                        originalPrice = product.originalPrice,
                        rating = product.rating,
                        onSizeClick = {
                            // Handle size selection
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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

                        // Navigate back to login screen
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