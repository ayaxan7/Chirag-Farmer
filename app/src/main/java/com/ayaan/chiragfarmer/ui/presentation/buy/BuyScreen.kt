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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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

    var selectedCategory by rememberSaveable { mutableDoubleStateOf(0.0) }

    val categories = remember { Categories.BuyCategories }

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

    Scaffold(
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            ChiragTopBar(
                navController = navController,
                icon = R.drawable.ic_arrow,
                title = "Buy"
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding() + 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                SearchBarButton(
                    onClick = {
                        navController.navigate(Route.Search.path)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                ImageCarousel(
                    images = carouselImages,
                    modifier = Modifier.fillMaxWidth()
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
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            selected = selectedCategory == category.id,
                            onClick = {
                                selectedCategory =
                                    if (selectedCategory == category.id) 0.0 else category.id
                            }
                        )
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
                    product = product
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                CategoryHeader(
                    category = "Direct From Farmers",
                    btnText = "View All",
                    onClick = {}
                )
            }
            items(smartFarmingProducts) { product ->
                CommonProductCard(
                    product = product
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                CategoryHeader(
                    category = "Seeds",
                    btnText = "View All",
                    onClick = {}
                )
            }
            items(smartFarmingProducts) { product ->
                CommonProductCard(
                    product = product
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                CategoryHeader(
                    category = "Popular Products",
                    btnText = "View All",
                    onClick = {}
                )
            }
            items(smartFarmingProducts) { product ->
                CommonProductCard(
                    product = product
                )
            }
        }
    }
}