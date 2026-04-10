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
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.home.components.search.RecentSearchItem
import com.yash091099.ChiragFarmersApp.ui.presentation.home.components.search.SearchBox
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.Teal

data class RecentSearch(
    val id: Int,
    val imageRes: Int,
    val searchText: String
)

@Composable
fun SearchScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    // Sample recent searches data
    val recentSearches = remember {
        mutableStateListOf(
            RecentSearch(1, R.drawable.agri_seeds, "Seeds"),
            RecentSearch(2, R.drawable.agri_seeds, "Drone"),
            RecentSearch(3, R.drawable.agri_seeds, "Power Sprayer"),
            RecentSearch(4, R.drawable.agri_seeds, "Tomato's")
        )
    }

    // Auto-focus the search field when screen opens
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController,
                icon = R.drawable.ic_arrow,
                title = "Search"
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
                onValueChange = { searchQuery = it },
                placeholder = "Search For",
                focusRequester = focusRequester
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Recent Section Header
            if (recentSearches.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    TextButton(
                        onClick = {
                            recentSearches.clear()
                        }
                    ) {
                        Text(
                            text = "Clear all",
                            fontSize = 14.sp,
                            color = Teal// Teal/Cyan color
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Recent Searches List
                recentSearches.forEach { search ->
                    RecentSearchItem(
                        imageRes = search.imageRes,
                        searchText = search.searchText,
                        onItemClick = {
                            searchQuery = search.searchText
                        },
                        onRemoveClick = {
                            recentSearches.remove(search)
                        }
                    )
                }
            }
        }
    }
}