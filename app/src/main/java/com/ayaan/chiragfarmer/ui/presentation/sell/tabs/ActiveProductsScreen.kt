package com.ayaan.chiragfarmer.ui.presentation.sell.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.ayaan.chiragfarmer.domain.model.Product
import com.ayaan.chiragfarmer.ui.presentation.common.components.CommonProductCard
import com.ayaan.chiragfarmer.ui.theme.BGBlack

@Composable
fun ActiveProductsScreen(
    products: LazyPagingItems<Product>,
    onToggleSoldOut: (String) -> Unit,
    onDeleteProduct: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 2.dp)
            .padding(top = 16.dp)
    ) {
        when (products.loadState.refresh) {
            is LoadState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BGBlack)
                }
            }
            is LoadState.Error -> {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(text = "Error loading products")
                }
            }
            else -> {
                if (products.itemCount == 0) {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(text = "No active products found")
                    }
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(products.itemCount) { index ->
                            products[index]?.let { product ->
                                CommonProductCard(
                                    imageUrl = product.imageUrl,
                                    productName = product.productName,
                                    brandName = product.sellerName,
                                    currentPrice = product.price.toString(),
                                    rating = "4.8", // Static for now as per DTO
                                    onSizeClick = {},
                                    isMarkAsSoldRowVisible = true,
                                    onMarkAsSoldClick = {
                                        onToggleSoldOut(product.productId)
                                    },
                                    onDeleteClick = {
                                        onDeleteProduct(product.productId)
                                    },
                                    isSoldOut = false
                                )
                            }
                        }
                        if (products.loadState.append is LoadState.Loading) {
                            item {
                                CircularProgressIndicator(color = BGBlack, modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}