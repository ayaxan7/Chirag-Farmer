package com.yash091099.ChiragFarmersApp.ui.presentation.sell.screens.sellcategories

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.CategoryItem
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.ChiragButton
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.data.Categories
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite

@Composable
fun SellCategoriesScreen(navController: NavHostController) {
    val snackBarHostState = remember { SnackbarHostState() }
    var selectedCategory by rememberSaveable { mutableIntStateOf(0) }
    val categories = remember {
        Categories.sellCategories
    }
    Scaffold(topBar = {
        ChiragTopBar(
            title = stringResource(R.string.sell_categories_title), navController = navController, icon = R.drawable.ic_arrow
        )
    }, containerColor = BGWhite, bottomBar = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            ChiragButton(
                text = stringResource(R.string.sell_categories_continue), containerColor = BGBlack, contentColor = BGWhite, onClick = {
                    val selectedCategoryName =
                        categories.firstOrNull { it.id == selectedCategory }?.name
                    val sanitizedCategory = sanitizeCategoryForForm(selectedCategoryName.orEmpty())
                    if (sanitizedCategory.isNotEmpty()) {
                        navController.navigate(
                            Route.SellProduct.createRoute(
                                selectedCategory = Uri.encode(sanitizedCategory)
                            )
                        )
                    }
                }, enabled = (selectedCategory != 0)
            )
        }
    }, snackbarHost = { SnackbarHost(snackBarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.sell_categories_select),
                fontWeight = FontWeight.W500,
                color = BGBlack,
                fontSize = 16.sp
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 4.dp)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { category ->
                    CategoryItem(
                        category = category, selected = selectedCategory == category.id, onClick = {
                            selectedCategory = if (selectedCategory == category.id) {
                                0
                            } else {
                                category.id
                            }
                        })
                }
            }
//            Spacer(Modifier.weight(1f))

        }
    }
}

private fun sanitizeCategoryForForm(value: String): String {
    return value.replace("\\n", " ").replace("\n", " ").trim().replace(Regex("\\s+"), " ")
}