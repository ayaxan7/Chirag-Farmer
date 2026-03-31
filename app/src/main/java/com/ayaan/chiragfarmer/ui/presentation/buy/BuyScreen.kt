package com.ayaan.chiragfarmer.ui.presentation.buy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.common.components.CategoryHeader
import com.ayaan.chiragfarmer.ui.presentation.common.components.CategoryItem
import com.ayaan.chiragfarmer.ui.presentation.common.components.CommonProductCard
import com.ayaan.chiragfarmer.ui.presentation.common.data.CommonProductCardData
import com.ayaan.chiragfarmer.ui.presentation.home.components.ImageCarousel
import com.ayaan.chiragfarmer.ui.presentation.home.components.topbar.SearchBarButton
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.ChiragTopBar
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.Route
import com.ayaan.chiragfarmer.ui.presentation.sell.data.Categories
import com.ayaan.chiragfarmer.ui.theme.BGWhite

@Composable
fun BuyScreen(navController: NavHostController) {
    val snackBarHostState = remember { SnackbarHostState() }

    val carouselImages = listOf(
        R.drawable.buy_banner,
        R.drawable.buy_banner,
        R.drawable.buy_banner,
    )

    val categories = remember { Categories.BuyCategories }

    // Map categories to their banner images
    val categoryBannerMap = remember {
        mapOf(
            "Seeds" to R.drawable.seeds_banner,
            "Sprayers" to R.drawable.agri_sprayers_banner,
            "Agriculture Drone" to R.drawable.agri_drones,
            "Tractors" to R.drawable.tractors_banner,
            "Direct From Farmers" to R.drawable.direct_from_farmers_banner
        )
    }

    val smartFarmingProducts = remember {
        List(4) {
            CommonProductCardData(
                imageUrl = "imageUrl",
                productName = "productName",
                brandName = "sellerName",
                currentPrice = "121",
                originalPrice = "145",
                rating = "4.8"
            )
        }
    }
    val directFromFarmersProducts = remember {
        List(2) {
            CommonProductCardData(
                imageUrl = "imageUrl",
                productName = "productName",
                brandName = "sellerName",
                currentPrice = "121",
                originalPrice = "145",
                rating = "4.8"
            )
        }
    }
    val seedsProducts = remember {
        List(2) {
            CommonProductCardData(
                imageUrl = "imageUrl",
                productName = "productName",
                brandName = "sellerName",
                currentPrice = "121",
                originalPrice = "145",
                rating = "4.8"
            )
        }
    }
    val popularProducts = remember {
        List(4) {
            CommonProductCardData(
                imageUrl = "imageUrl",
                productName = "productName",
                brandName = "sellerName",
                currentPrice = "121",
                originalPrice = "145",
                rating = "4.8"
            )
        }
    }

    Scaffold(
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            ChiragTopBar(
                navController = navController, icon = R.drawable.ic_arrow, title = "Buy"
            )
        }) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SearchBarButton(
                    onClick = {
                        navController.navigate(Route.Search.path)
                    }, modifier = Modifier.fillMaxWidth()
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                ImageCarousel(
                    images = carouselImages, modifier = Modifier.fillMaxWidth()
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                CategoryHeader(
                    category = "Categories"
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            selected = false,
                            onClick = {
                                val routeCategoryName = category.name
                                    .replace("\n", " ")
                                    .trim()

                                val bannerResId = categoryBannerMap[routeCategoryName] ?: R.drawable.buy_banner

                                navController.navigate(
                                    Route.BuyCategory.createRoute(routeCategoryName, bannerResId)
                                )
                            })
                    }
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                CategoryHeader(
                    category = "Smart Farming"
                )
            }

            items(smartFarmingProducts) { product ->
                CommonProductCard(
                    product = product, modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                CategoryHeader(
                    category = "Direct From Farmers",
                    btnText = "View All",
                    onClick = {
                        val bannerResId = categoryBannerMap["Direct From Farmers"] ?: R.drawable.buy_banner
                        navController.navigate(Route.BuyCategory.createRoute("Direct From Farmers", bannerResId))
                    }
                )
            }
            items(directFromFarmersProducts) { product ->
                CommonProductCard(
                    product = product, modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                CategoryHeader(
                    category = "Seeds",
                    btnText = "View All",
                    onClick = {
                        val bannerResId = categoryBannerMap["Seeds"] ?: R.drawable.buy_banner
                        navController.navigate(Route.BuyCategory.createRoute("Seeds", bannerResId))
                    }
                )
            }
            items(seedsProducts) { product ->
                CommonProductCard(
                    product = product, modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                CategoryHeader(
                    category = "Popular Products",
                    btnText = "View All",
                    onClick = {
                        navController.navigate(Route.BuyCategory.createRoute("Popular Products"))
                    }
                )
            }
            items(popularProducts) { product ->
                CommonProductCard(
                    product = product, modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}