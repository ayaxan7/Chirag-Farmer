package com.yash091099.ChiragFarmersApp.ui.presentation.home.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.res.stringResource
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.home.components.search.RecentSearchItem
import com.yash091099.ChiragFarmersApp.ui.presentation.home.components.search.SearchBox
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.Teal

data class RecentSearch(
    val id: String,
    val imageUrl: String? = null,
    val searchText: String
)

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    val focusRequester = remember { FocusRequester() }

    // Map the search results to the RecentSearch data class for compatibility with RecentSearchItem UI
    val recentSearches = searchResults.map { 
        RecentSearch(id = it.productId, imageUrl = it.imageUrl, searchText = it.name)
    }

    // Autofocus the search field when screen opens
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController,
                icon = R.drawable.ic_arrow,
                title = stringResource(R.string.search_title)
            )
        },
        containerColor = BGWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Search Box Component
            SearchBox(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                placeholder = stringResource(R.string.search_placeholder),
                focusRequester = focusRequester
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Recent Section Header (Reused for search results to match UI exactly)
            if (recentSearches.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.length < 2) "Recent" else "Results",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    
                    if (searchQuery.length < 2) {
                        TextButton(
                            onClick = {
                                // Logic to clear recent if stored locally
                            }
                        ) {
                            Text(
                                text = "Clear all",
                                fontSize = 14.sp,
                                color = Teal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Results List using the same visual component
                recentSearches.forEach { search ->
                    RecentSearchItem(
                        imageUrl = search.imageUrl,
                        searchText = search.searchText,
                        onItemClick = {
                            // Navigate to product details
                            navController.navigate(Route.ProductDetails.createRoute(search.id))
                        }
                    )
                }
            }
        }
    }
}