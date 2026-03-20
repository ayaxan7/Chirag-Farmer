package com.ayaan.chiragfarmer.ui.presentation.buy.screens

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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.common.components.CategoryItem
import com.ayaan.chiragfarmer.ui.presentation.common.components.CommonProductCard
import com.ayaan.chiragfarmer.ui.presentation.common.data.CommonProductCardData
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.ChiragTopBar
import com.ayaan.chiragfarmer.ui.presentation.sell.data.BuySellCategory
import com.ayaan.chiragfarmer.ui.presentation.sell.data.Categories
import com.ayaan.chiragfarmer.ui.theme.BGWhite

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    categoryName: String = "",
    bannerImageRes: Int = R.drawable.buy_banner,
) {

    var selectedCategoryId by rememberSaveable { mutableDoubleStateOf(0.0) }

    val categories = Categories.sellCategories

    val allCategories = listOf(
        BuySellCategory("All\nProduces", R.drawable.sell_category_all, 0.0)
    ) + categories

    val products = listOf(
        CommonProductCardData(
            productName = "TOMATOES",
            brandName = "Rahul kissan",
            currentPrice = "29",
            originalPrice = "35",
            rating = "4.8",
            imageUrl = ""
        ), CommonProductCardData(
            productName = "RED APPLES",
            brandName = "Rahul kissan",
            currentPrice = "189",
            originalPrice = "255",
            rating = "4.8",
            imageUrl = ""
        ), CommonProductCardData(
            productName = "WHITE RICE",
            brandName = "Rahul kissan",
            currentPrice = "899",
            rating = "4.8",
            imageUrl = ""
        ), CommonProductCardData(
            productName = "BIRYANI FLOWER",
            brandName = "Rahul kissan",
            currentPrice = "150",
            rating = "4.8",
            imageUrl = ""
        )
    )

    val categoryDisplayText =
        allCategories.find { it.id == selectedCategoryId }?.name?.replace("\n", " ")
            ?: "All Produces"

    Scaffold(
        modifier = modifier, containerColor = BGWhite, topBar = {
            ChiragTopBar(
                navController = navController, title = categoryName, icon = R.drawable.ic_arrow
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
                    contentDescription = "Banner",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Categories", fontSize = 16.sp, fontWeight = FontWeight.W600
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
                                    if (selectedCategoryId == category.id) 0.0 else category.id
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
                        text = categoryDisplayText, fontSize = 16.sp, fontWeight = FontWeight.W600
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            modifier = Modifier.width(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Filter", fontSize = 13.sp
                        )
                    }
                }
            }

            items(products) { product ->
                CommonProductCard(
                    product = product, isSellScreen = false
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}