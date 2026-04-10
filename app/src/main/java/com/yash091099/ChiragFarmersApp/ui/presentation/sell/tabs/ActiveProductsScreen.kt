package com.yash091099.ChiragFarmersApp.ui.presentation.sell.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.yash091099.ChiragFarmersApp.domain.model.Product
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.CommonProductCard
import com.yash091099.ChiragFarmersApp.ui.presentation.common.data.CommonProductCardData
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.components.RemoveProductDialog
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.components.SoldOutDialog
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack

@Composable
fun ActiveProductsScreen(
    products: LazyPagingItems<Product>,
    onToggleSoldOut: (String) -> Unit,
    onDeleteProduct: (String) -> Unit,
    onEditProduct: (String) -> Unit
) {
    var showRemoveDialog by remember { mutableStateOf(false) }
    var showSoldOutDialog by remember { mutableStateOf(false) }
    var selectedProductId by remember { mutableStateOf<String?>(null) }

    // Remove Product Dialog
    if (showRemoveDialog && selectedProductId != null) {
        RemoveProductDialog(
            onDismiss = {
                showRemoveDialog = false
                selectedProductId = null
            },
            onConfirm = {
                selectedProductId?.let { onDeleteProduct(it) }
                selectedProductId = null
            }
        )
    }

    // Sold Out Dialog
    if (showSoldOutDialog && selectedProductId != null) {
        SoldOutDialog(
            onDismiss = {
                showSoldOutDialog = false
                selectedProductId = null
            },
            onConfirm = {
                selectedProductId?.let { onToggleSoldOut(it) }
                selectedProductId = null
            }
        )
    }

    when (products.loadState.refresh) {
        is LoadState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = BGBlack)
            }
        }
        is LoadState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error loading products")
            }
        }
        else -> {
            if (products.itemCount == 0) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No active products found")
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize(),
//                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(products.itemCount) { index ->
                        products[index]?.let { product ->
                            CommonProductCard(
                                product = CommonProductCardData(
                                    imageUrl = product.imageUrl,
                                    productName = product.productName,
                                    brandName = product.sellerName,
                                    currentPrice = product.effectivePrice.toString(),
                                    originalPrice = product.originalPrice.toString(),
                                    rating = "4.8", // Static for now as per DTO
                                    isSoldOut = false
                                ),
                                isSellScreen = true,
                                onMarkAsSoldClick = {
                                    selectedProductId = product.productId
                                    showSoldOutDialog = true
                                },
                                onDeleteClick = {
                                    selectedProductId = product.productId
                                    showRemoveDialog = true
                                },
                                onEditClick = {
                                    onEditProduct(product.productId)
                                }
                            )
                        }
                    }
                    if (products.loadState.append is LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = BGBlack)
                            }
                        }
                    }
                }
            }
        }
    }
}