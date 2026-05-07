package com.yash091099.ChiragFarmersApp.ui.presentation.sell

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.home.components.search.SearchBox
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.tabs.ActiveOrdersScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.tabs.ActiveProductsScreen
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.tabs.SoldOutProductsScreen
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import kotlinx.coroutines.launch

@Composable
fun SellScreen(
    navController: NavHostController, viewModel: SellViewModel = hiltViewModel()
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val toggleState by viewModel.toggleState.collectAsStateWithLifecycle()
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    val activeProducts = viewModel.activeProducts.collectAsLazyPagingItems()
    val soldOutProducts = viewModel.soldOutProducts.collectAsLazyPagingItems()

    val selectedOrderId by viewModel.selectedOrderId.collectAsStateWithLifecycle()

    BackHandler(enabled = selectedOrderId != null) {
        viewModel.selectOrder(null)
    }

    // Handle toggle state changes
    LaunchedEffect(toggleState) {
        when (val state = toggleState) {
            is ToggleSoldOutState.Success -> {
                snackBarHostState.showSnackbar(state.message)
                viewModel.resetToggleState()
                activeProducts.refresh()
                soldOutProducts.refresh()
            }

            is ToggleSoldOutState.Error -> {
                snackBarHostState.showSnackbar(state.message)
                viewModel.resetToggleState()
            }

            else -> Unit
        }
    }

    // Handle delete state changes
    LaunchedEffect(deleteState) {
        when (val state = deleteState) {
            is DeleteProductState.Success -> {
                snackBarHostState.showSnackbar(state.message)
                viewModel.resetDeleteState()
                // Refresh both lists
                activeProducts.refresh()
                soldOutProducts.refresh()
            }

            is DeleteProductState.Error -> {
                snackBarHostState.showSnackbar(state.message)
                viewModel.resetDeleteState()
            }

            else -> Unit
        }
    }

    // Update search query based on current tab
    LaunchedEffect(pagerState.currentPage, searchQuery) {
        when (pagerState.currentPage) {
            0 -> viewModel.fetchActive(searchQuery)
            1 -> viewModel.fetchSoldOut(searchQuery)
        }
    }

    Scaffold(
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = "Sell",
                icon = R.drawable.ic_arrow,
                buttonText = "Sell Product",
                buttonIcon = Icons.Default.Add,
                onButtonClick = {
                    navController.navigate(Route.SellCategories.path) {
                        launchSingleTop = true
                    }
                })
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            SearchBox(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                placeholder = "Search For Products"
            )

            Spacer(modifier = Modifier.padding(8.dp))

            val tabs = listOf("Active Products", "Products Sold Out", "Active Orders")

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                contentColor = BGBlack,
//                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            tabPositions[pagerState.currentPage]
                        ), height = 2.dp, color = BGBlack
                    )
                }) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index, onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }, text = {
                        Text(
                            text = title,
                            color = BGBlack,
                            fontWeight = if (pagerState.currentPage == index) FontWeight.SemiBold
                            else FontWeight.Normal,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }, selectedContentColor = BGBlack, unselectedContentColor = BGBlack
                    )
                }
            }

            HorizontalPager(
                state = pagerState, modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> ActiveProductsScreen(
                        products = activeProducts,
                        onToggleSoldOut = { productId ->
                            viewModel.toggleSoldOut(productId)
                        },
                        onDeleteProduct = { productId ->
                            viewModel.deleteProduct(productId)
                        },
                        onEditProduct = { productId ->
                            navController.navigate(Route.SellProduct.createRoute(productId))
                        })

                    1 -> SoldOutProductsScreen(
                        products = soldOutProducts,
                        onToggleSoldOut = { productId ->
                            viewModel.toggleSoldOut(productId)
                        },
                        onDeleteProduct = { productId ->
                            viewModel.deleteProduct(productId)
                        })

                    2 -> ActiveOrdersScreen(
                        navController = navController,
                        selectedOrderId = selectedOrderId,
                        onOrderClick = { viewModel.selectOrder(it) }
                    )
                }
            }
        }
    }
}