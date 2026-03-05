package com.ayaan.chiragfarmer.ui.presentation.sell.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.common.components.CommonProductCard
import com.ayaan.chiragfarmer.ui.presentation.home.SmartFarmingProduct

@Composable
fun SoldOutProductsScreen() {
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 2.dp)
            .padding(top = 16.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                    },
                    isMarkAsSoldRowVisible = true,
                    onMarkAsSoldClick = {
                        // Handle mark as sold click
                    },
                    isSoldOut = false
                )
            }
        }
    }
}