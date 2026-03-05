package com.ayaan.chiragfarmer.ui.presentation.sell

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.home.components.search.SearchBox
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.ChiragTopBar
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.Route
import com.ayaan.chiragfarmer.ui.presentation.sell.tabs.ActiveProductsScreen
import com.ayaan.chiragfarmer.ui.presentation.sell.tabs.SoldOutProductsScreen
import com.ayaan.chiragfarmer.ui.theme.BGBlack
import com.ayaan.chiragfarmer.ui.theme.BGWhite
import kotlinx.coroutines.launch

@Composable
fun SellScreen(navController: NavHostController) {

    val snackBarHostState = remember { SnackbarHostState() }
    var searchQuery by remember { mutableStateOf("") }

    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

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
                    navController.navigate(Route.Sell.path) {
                        popUpTo(0) { inclusive = true }
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
                onValueChange = { searchQuery = it },
                placeholder = "Search For Products"
            )

            Spacer(modifier = Modifier.padding(8.dp))

            val tabs = listOf("Active Products", "Products Sold Out")

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                contentColor = BGBlack,
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            tabPositions[pagerState.currentPage]
                        ), height = 2.dp, color = BGBlack
                    )
                }
            ) {

                tabs.forEachIndexed { index, title ->

                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                color = BGBlack,
                                fontWeight = if (pagerState.currentPage == index)
                                    FontWeight.SemiBold
                                else
                                    FontWeight.Normal
                            )
                        },
                        selectedContentColor = BGBlack,
                        unselectedContentColor = BGBlack
                    )
                }
            }

            HorizontalPager(
                state = pagerState, modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> ActiveProductsScreen()
                    1 -> SoldOutProductsScreen()
                }
            }
        }
    }
}