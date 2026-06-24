package com.yash091099.ChiragFarmersApp.ui.presentation.buy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.data.remote.dto.MixedProductItem
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.CategoryHeader
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.CategoryItem
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.CommonProductCard
import com.yash091099.ChiragFarmersApp.ui.presentation.common.data.CommonProductCardData
import com.yash091099.ChiragFarmersApp.ui.presentation.home.components.ImageCarousel
import com.yash091099.ChiragFarmersApp.ui.presentation.home.components.topbar.SearchBarButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.data.Categories
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite

@Composable
fun BuyScreen(navController: NavHostController, viewModel: BuyViewModel = hiltViewModel()) {
    val snackBarHostState = remember { SnackbarHostState() }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val langTag = LocalConfiguration.current.locales[0].toLanguageTag()
    val carouselImages = when {
        langTag.startsWith("hi") -> listOf(
            R.drawable.buy_banner_hi,
            R.drawable.buy_banner_hi,
            R.drawable.buy_banner_hi,
        )

        langTag.startsWith("te") -> listOf(
            R.drawable.buy_banner_te,
            R.drawable.buy_banner_te,
            R.drawable.buy_banner_te,
        )

        langTag.startsWith("pa") -> listOf(
            R.drawable.buy_banner_pa,
            R.drawable.buy_banner_pa,
            R.drawable.buy_banner_pa,
        )

        else -> listOf(
            R.drawable.buy_banner,
            R.drawable.buy_banner,
            R.drawable.buy_banner,
        )
    }

    val categories = remember { Categories.buyCategories }


    Scaffold(
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            ChiragTopBar(
                navController = navController,
                icon = R.drawable.ic_arrow,
                title = stringResource(R.string.buy_title)
            )
        }) { paddingValues ->

        when (val state = uiState.value) {
            is BuyUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is BuyUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message, modifier = Modifier.padding(16.dp)
                    )
                }
            }

            is BuyUiState.Success -> {
                // Helper function to check if a product is valid
                fun isValidProduct(product: MixedProductItem): Boolean {
                    return product.finalPrice > 0
                }

                // Filter products to show only valid ones
                val validVendorProducts = state.vendorProducts.filter { isValidProduct(it) }
                val validDirectFromFarmersProducts =
                    state.directFromFarmersProducts.filter { isValidProduct(it) }
                val validSeedProducts = state.seedProducts.filter { isValidProduct(it) }
                val validRandomProducts = state.randomProducts.filter { isValidProduct(it) }

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
                            images = carouselImages,
                            modifier = Modifier.fillMaxWidth(),
                            isIndicatorVisible = false
                        )
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        CategoryHeader(
                            category = stringResource(R.string.buy_categories)
                        )
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(categories) { category ->
                                CategoryItem(
                                    category = category, selected = false, onClick = {
                                        val routeCategoryName =
                                            category.name.replace("\n", " ").trim()

                                        val bannerResId = Categories.getBuyBannerImage(
                                            routeCategoryName, langTag
                                        )

                                        navController.navigate(
                                            Route.BuyCategory.createRoute(
                                                routeCategoryName,
                                                bannerResId
                                            )
                                        )
                                    })
                            }
                        }
                    }

                    if (validVendorProducts.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            CategoryHeader(
                                category = stringResource(R.string.buy_smart_farming)
                            )
                        }

                        items(validVendorProducts) { product ->
                            CommonProductCard(
                                product = CommonProductCardData(
                                    imageUrl = product.imageUrl,
                                    productName = product.productName,
                                    brandName = product.sellerName,
                                    currentPrice = product.finalPrice.toInt().toString(),
                                    originalPrice = product.originalPrice.toInt().toString(),
                                    rating = product.rating
                                ), onClick = {
                                    navController.navigate(Route.ProductDetails.createRoute(product.id))
                                }, modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }
                    }

                    if (validDirectFromFarmersProducts.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            CategoryHeader(
                                category = stringResource(R.string.buy_direct_from_farmers),
                                btnText = stringResource(R.string.home_view_all),
                                onClick = {
                                    val bannerResId = Categories.getBuyBannerImage(
                                        "Direct From Farmers", langTag
                                    )
                                    navController.navigate(
                                        Route.BuyCategory.createRoute(
                                            "Direct From Farmers",
                                            bannerResId
                                        )
                                    )
                                })

                        }
                        items(validDirectFromFarmersProducts) { product ->
                            CommonProductCard(
                                product = CommonProductCardData(
                                    imageUrl = product.imageUrl,
                                    productName = product.productName,
                                    brandName = product.sellerName,
                                    currentPrice = product.finalPrice.toInt().toString(),
                                    originalPrice = product.originalPrice.toInt().toString(),
                                    rating = product.rating
                                ), onClick = {
                                    navController.navigate(Route.ProductDetails.createRoute(product.id))
                                }, modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }
                    }

                    if (validSeedProducts.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            CategoryHeader(
                                category = stringResource(R.string.buy_seeds),
                                btnText = stringResource(R.string.home_view_all),
                                onClick = {
                                    val bannerResId = Categories.getBuyBannerImage(
                                        "Seeds", langTag
                                    )
                                    navController.navigate(
                                        Route.BuyCategory.createRoute(
                                            "Seeds",
                                            bannerResId
                                        )
                                    )
                                })
                        }
                        items(validSeedProducts) { product ->
                            CommonProductCard(
                                product = CommonProductCardData(
                                    imageUrl = product.imageUrl,
                                    productName = product.productName,
                                    brandName = product.sellerName,
                                    currentPrice = product.finalPrice.toInt().toString(),
                                    originalPrice = product.originalPrice.toInt().toString(),
                                    rating = product.rating
                                ), onClick = {
                                    navController.navigate(Route.ProductDetails.createRoute(product.id))
                                }, modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }
                    }

                    if (validRandomProducts.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            CategoryHeader(
                                category = stringResource(R.string.buy_popular_products),
                                btnText = stringResource(R.string.home_view_all),
                                onClick = {
                                    navController.navigate(Route.BuyCategory.createRoute("Popular Products", isPopularProducts = true))
                                })
                        }
                        items(validRandomProducts) { product ->
                            CommonProductCard(
                                product = CommonProductCardData(
                                    imageUrl = product.imageUrl,
                                    productName = product.productName,
                                    brandName = product.sellerName,
                                    currentPrice = product.finalPrice.toInt().toString(),
                                    originalPrice = product.originalPrice.toInt().toString(),
                                    rating = product.rating
                                ), onClick = {
                                    navController.navigate(Route.ProductDetails.createRoute(product.id))
                                }, modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}