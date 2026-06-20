package com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.CategoryItem
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.CommonProductCard
import com.yash091099.ChiragFarmersApp.ui.presentation.common.data.CommonProductCardData
import com.yash091099.ChiragFarmersApp.utils.formatAmount
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.data.BuySellCategory
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.data.Categories
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite

private fun String.singleLine(): String =
    replace("\n", " ")
        .replace(Regex("\\s+"), " ")
        .trim()

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    categoryName: String = "",
    bannerImageRes: Int = R.drawable.buy_banner,
    viewModel: BuyCategoriesViewModel = hiltViewModel()
) {

    var selectedCategoryId by rememberSaveable { mutableIntStateOf(0) }

    val categories = Categories.getBuySubcategories(categoryName)

    val allCategories = listOf(
        BuySellCategory("All\nProduces", R.drawable.sell_category_all, 0, displayNameRes = R.string.category_all_products)
    ) + categories

    val products = viewModel.products.collectAsLazyPagingItems()

    LaunchedEffect(categoryName) {
        selectedCategoryId = 0
        viewModel.setCategoryName(categoryName)
        viewModel.onCategoryChipSelected(null)
    }

    val selectedCat = allCategories.find { it.id == selectedCategoryId }
    val categoryDisplayText = if (selectedCat?.displayNameRes != null) {
        stringResource(selectedCat.displayNameRes)
    } else {
        selectedCat?.name ?: stringResource(R.string.category_all_products)
    }.singleLine()

    val categoryTitle = (
        Categories.getBuyCategory(categoryName)?.displayNameRes?.let { stringResource(it) }
            ?: categoryName
        ).singleLine()

    Scaffold(
        modifier = modifier, containerColor = BGWhite, topBar = {
            ChiragTopBar(
                navController = navController, title = categoryTitle, icon = R.drawable.ic_arrow
            )
        }) { paddingValues ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = 16.dp, vertical = 12.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item(span = { GridItemSpan(maxLineSpan) }) {
                Image(
                    painter = painterResource(id = bannerImageRes),
                    contentDescription = stringResource(R.string.categories_banner_description),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = stringResource(R.string.categories_title), fontSize = 16.sp, fontWeight = FontWeight.W600
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(allCategories) { category ->
                        CategoryItem(
                            category = category,
                            selected = selectedCategoryId == category.id,
                            onClick = {
                                selectedCategoryId =
                                    if (selectedCategoryId == category.id) 0 else category.id
                                viewModel.onCategoryChipSelected(
                                    allCategories.find { it.id == selectedCategoryId }?.apiValue
                                )
                            })
                    }
                }
            }


            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = categoryDisplayText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600
                    )

//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.FilterList,
//                            contentDescription = "Filter",
//                            modifier = Modifier.width(18.dp)
//                        )
//                        Spacer(modifier = Modifier.width(6.dp))
//                        Text(
//                            text = "Filter", fontSize = 13.sp
//                        )
//                    }
                }
            }

            when (products.loadState.refresh) {
                is LoadState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(text = stringResource(R.string.categories_loading))
                    }
                }

                is LoadState.Error -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(text = stringResource(R.string.categories_no_products))
                    }
                }

                is LoadState.NotLoading -> {
                    if (products.itemCount == 0) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Text(text = stringResource(R.string.categories_no_products))
                        }
                    } else {
                        items(products.itemCount) { index ->
                            val product = products[index] ?: return@items
                            CommonProductCard(
                                product = CommonProductCardData(
                                    productName = product.productName,
                                    brandName = product.sellerName,
                                    currentPrice = product.effectivePrice.formatAmount(),
                                    originalPrice = product.originalPrice.takeIf { it > 0 }?.formatAmount(),
                                    rating = product.rating,
                                    imageUrl = product.imageUrl
                                ), isSellScreen = false, onClick = {
                                    navController.navigate(Route.ProductDetails.createRoute(product.productId))
                                })
                        }

                        when (val appendState = products.loadState.append) {
                            is LoadState.Loading -> {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Text(text = stringResource(R.string.categories_loading_more))
                                }
                            }

                            is LoadState.Error -> {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Text(text = appendState.error.message ?: stringResource(R.string.categories_error_load_more))
                                }
                            }

                            else -> Unit
                        }
                    }
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
