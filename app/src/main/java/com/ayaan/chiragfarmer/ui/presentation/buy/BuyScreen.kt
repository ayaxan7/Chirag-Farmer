package com.ayaan.chiragfarmer.ui.presentation.buy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.common.components.CategoryHeader
import com.ayaan.chiragfarmer.ui.presentation.common.components.CategoryItem
import com.ayaan.chiragfarmer.ui.presentation.home.components.ImageCarousel
import com.ayaan.chiragfarmer.ui.presentation.home.components.topbar.SearchBarButton
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.ChiragTopBar
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.Route
import com.ayaan.chiragfarmer.ui.presentation.sell.data.Categories
import com.ayaan.chiragfarmer.ui.theme.BGWhite

@Composable
fun BuyScreen(navController: NavHostController) {
    val snackBarHostState = remember { SnackbarHostState() }
    val carouselImages= listOf(
        R.drawable.buy_banner,
        R.drawable.buy_banner,
        R.drawable.buy_banner,
    )
    var selectedCategory by rememberSaveable { mutableDoubleStateOf(0.0) }
    val categories = Categories.BuyCategories
    Scaffold(
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            ChiragTopBar(
                navController = navController, icon = R.drawable.ic_arrow, title = "Buy"
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBarButton(
                onClick = {
                // Navigate to search screen
                navController.navigate(Route.Search.path)
            },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(8.dp))
            ImageCarousel(
                images = carouselImages,
                modifier = Modifier.fillMaxWidth()
            )
            CategoryHeader(
                category = "Categories"
            )
            LazyRow( modifier = Modifier
                .weight(1f)
                .padding(top = 4.dp)
                .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { category ->
                    CategoryItem(
                        category = category,
                        selected = selectedCategory == category.id,
                        onClick = {
                            selectedCategory = if (selectedCategory == category.id) {
                                0.0
                            } else {
                                category.id
                            }
                        })
                }
            }
        }
    }
}

