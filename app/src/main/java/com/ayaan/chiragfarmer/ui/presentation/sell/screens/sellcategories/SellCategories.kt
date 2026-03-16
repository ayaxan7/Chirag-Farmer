package com.ayaan.chiragfarmer.ui.presentation.sell.screens.sellcategories

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.common.components.ChiragButton
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.ChiragTopBar
import com.ayaan.chiragfarmer.ui.presentation.navigation.navbar.Route
import com.ayaan.chiragfarmer.ui.presentation.sell.data.Categories
import com.ayaan.chiragfarmer.ui.presentation.sell.data.SellCategory
import com.ayaan.chiragfarmer.ui.theme.BGBlack
import com.ayaan.chiragfarmer.ui.theme.BGWhite
import com.ayaan.chiragfarmer.ui.theme.BackgroundGray
import com.ayaan.chiragfarmer.ui.theme.BorderGreen
@Composable
fun SellCategoriesScreen(navController: NavHostController) {
    val snackBarHostState = remember { SnackbarHostState() }
    var selectedCategory by rememberSaveable { mutableStateOf("") }
    val categories = remember {
        Categories.sellCategories
    }
    Scaffold(
        topBar = {
            ChiragTopBar(
                title = "Sell Produces", navController = navController, icon = R.drawable.ic_arrow
            )
        },
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackBarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Select Categories",
                fontWeight = FontWeight.W500,
                color = BGBlack,
                fontSize = 16.sp
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .weight(1f)
                    .padding(top=4.dp)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { category ->
                    CategoryItem(
                        category = category,
                        selected = selectedCategory == category.name,
                        onClick = {
                            selectedCategory = if (selectedCategory == category.name) {
                                ""
                            } else {
                                category.name
                            }
                        })
                }
            }
            Spacer(Modifier.weight(1f))
            ChiragButton(
                text = "Continue", containerColor = BGBlack, contentColor = BGWhite, onClick = {
                    val sanitizedCategory = sanitizeCategoryForForm(selectedCategory)
                    navController.navigate(
                        Route.SellProduct.createRoute(
                            selectedCategory = Uri.encode(sanitizedCategory)
                        )
                    )
                }, enabled = selectedCategory.isNotEmpty()
            )
        }
    }
}

@Composable
private fun CategoryItem(
    category: SellCategory, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .clickable { onClick() }
        .padding(top = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        // Circular Image Container
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    color = BackgroundGray
                )
                .border(
                    width = if (selected) 2.dp else 0.dp,
                    color = if (selected) BorderGreen else Color.Transparent,
                    shape = CircleShape
                ), contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = category.image),
                contentDescription = category.name,
                modifier = Modifier
                    .fillMaxSize(.9f)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Category Name
        Text(
            text = category.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp,
            modifier = Modifier.padding(horizontal = 4.dp),
            maxLines = 2,
            overflow = TextOverflow.MiddleEllipsis
        )
    }
}
private fun sanitizeCategoryForForm(value: String): String {
    return value
        .replace("\\n", " ")
        .replace("\n", " ")
        .trim()
        .replace(Regex("\\s+"), " ")
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CategoryItemPreview() {
    Column {
        CategoryItem(
            category = SellCategory("Cereals &\nGrains", R.drawable.sell_category_cereals),
            selected = true,
            onClick = {})
        Spacer(modifier = Modifier.height(16.dp))
        CategoryItem(
            category = SellCategory("Pulses &\nLegumes", R.drawable.sell_category_cereals),
            selected = false,
            onClick = {})
    }
}