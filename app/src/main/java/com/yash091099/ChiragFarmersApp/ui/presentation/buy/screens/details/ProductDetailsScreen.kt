package com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.details.components.RatingProgressBar
import com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.details.components.ReviewCard
import com.yash091099.ChiragFarmersApp.ui.presentation.common.components.CommonProductCard
import com.yash091099.ChiragFarmersApp.ui.presentation.common.data.CommonProductCardData
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    navController: NavHostController,
    viewModel: ProductDetailsViewModel,
    modifier: Modifier = Modifier
) {
    var selectedImageIndex by remember { mutableIntStateOf(0) }
    val uiState by viewModel.uiState.collectAsState()
    val cartState by viewModel.cartState.collectAsState()
    val isInCart by viewModel.isInCart.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.loadProductDetails()
    }
    // Show snackbar when cart action completes
    when (val state = cartState) {
        is CartActionState.Success -> {
            LaunchedEffect(state) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Product added to cart! (${state.cartItemsCount} items)")
                    viewModel.resetCartState()
                }
            }
        }
        is CartActionState.Error -> {
            LaunchedEffect(state) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Error: ${state.message}")
                    viewModel.resetCartState()
                }
            }
        }
        else -> {}
    }

    when (val state = uiState) {
        is ProductDetailsUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BGWhite),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BGBlack)
            }
        }

        is ProductDetailsUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BGWhite),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Error Loading Product",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = state.message,
                        fontSize = 14.sp,
                        color = TextGray,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Button(
                        onClick = { viewModel.retry() },
                        colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                    ) {
                        Text("Retry", color = BGWhite)
                    }
                }
            }
        }

        is ProductDetailsUiState.Success -> {
            val product = state.productDetails
            Scaffold(
                modifier = modifier,
                containerColor = BGWhite,
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Details",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }, navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painterResource(R.drawable.ic_arrow),
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }
                    }, actions = {
                        IconButton(onClick = { /* Share */ }) {
                            Icon(
                                painterResource(R.drawable.ic_share),
                                contentDescription = "Share",
                                tint = Color.Black
                            )
                        }

                        IconButton(onClick = { navController.navigate(
                            Route.Cart.path
                        ) }) {
                            Icon(
                                painterResource(R.drawable.ic_cart_outlined),
                                contentDescription = "Cart",
                                tint = Color.Black
                            )
                        }
                    }, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BGWhite
                    )
                )
            }, bottomBar = {
                ProductDetailsBottomBar(
                    cartState = cartState,
                    isInCart = isInCart,
                    onAddToCart = { viewModel.addToCart() },
                    onBuyNow = {
                        navController.navigate(
                            Route.Cart.createRoute(
                                isBuyNow = true,
                                productId = product.productId,
                                quantity = 1
                            )
                        )
                    }
                )
            }) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Image Gallery Section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .background(Color(0xFFF8F8F8))
                    ) {
                        // Main product image
                        if (product.productImages.isNotEmpty()) {
                            AsyncImage(
                                model = product.productImages[selectedImageIndex],
                                contentDescription = "Product",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }else{
                            Image(
                                painter = painterResource(R.drawable.sell_category_other),
                                contentDescription = "Product",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }

                        // Thumbnail images on left
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            product.productImages.forEachIndexed { index, imageUrl ->
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = "Thumbnail $index",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(
                                            width = if (index == selectedImageIndex) 2.dp else 0.5.dp,
                                            color = if (index == selectedImageIndex) Color.Black else Color(
                                                0xFFE0E0E0
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedImageIndex = index },
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        // Product name
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = product.productName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "4.5",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Seller name as brand
                        Text(
                            text = product.seller.name,
                            fontSize = 13.sp,
                            color = Color(0xFF666666)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Price
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if(product.discountedPrice!=product.originalPrice) {
                                Text(
                                    text = "₹${product.discountedPrice}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = "₹${product.originalPrice}",
                                fontSize = 14.sp,
                                fontWeight = if(product.discountedPrice!=product.originalPrice) FontWeight.Normal else FontWeight.Bold,
                                color = if(product.discountedPrice!=product.originalPrice) Color(0xFF999999) else BGBlack,
                                textDecoration = if(product.discountedPrice!=product.originalPrice) TextDecoration.LineThrough else null
                            )
                            if(product.discountedPrice!=product.originalPrice) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${product.discountPercent.toInt()}% OFF",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Product Description
                        if(!product.productDescription.isNullOrEmpty()) {
                            Text(
                                text = "Product Description",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = product.productDescription,
                                fontSize = 13.sp,
                                color = TextGray,
                                lineHeight = 13.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Key Features
                        if (product.keyFeatures.isNotEmpty()) {
                            Text(
                                text = "Key Features :",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BGBlack
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            product.keyFeatures.forEach { feature ->
                                Row(modifier = Modifier.padding(vertical = 1.dp)) {
                                    Text(
                                        text = " • $feature",
                                        fontSize = 13.sp,
                                        color = TextGray,
                                        lineHeight = 13.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Sold by
                        Text(
                            text = "Sold by",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (product.seller.profilePhoto != null) {
                                AsyncImage(
                                    model = product.seller.profilePhoto,
                                    contentDescription = "Seller",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(R.drawable.profile_icon),
                                    contentDescription = "Seller",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = product.seller.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Black
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified",
                                        tint = Color(0xFF2196F3),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFC107),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "4.8",
                                        fontSize = 12.sp,
                                        color = Color(0xFF666666),
                                        modifier = Modifier.padding(start = 2.dp)
                                    )
                                }
                            }
                            Button(
                                onClick = { /* View seller */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BGBlack, contentColor = BGWhite
                                ),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_view_seller),
                                    contentDescription = "Store",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("View Seller", fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        if (product.similarProducts.isNotEmpty()) {
                            Text(
                                text = "Similar Products",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            LazyHorizontalGrid(
                                rows = GridCells.Fixed(1),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp)
                            ) {
                                items(product.similarProducts.size) { index ->
                                    val item = product.similarProducts[index]
                                    CommonProductCard(
                                        product = CommonProductCardData(
                                            imageUrl = item.imageUrl,
                                            productName = item.name,
                                            brandName = item.sellerName,
                                            currentPrice = item.discountedPrice.toInt().toString(),
                                            originalPrice = item.originalPrice.toInt().toString(),
                                            rating = "4.5"
                                        ),
                                        modifier = Modifier.width(180.dp),
                                        onClick = {
                                            navController.navigate(Route.ProductDetails.createRoute(item.productId))
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        if (product.moreProducts.isNotEmpty()) {
                            Text(
                                text = "More Products for you",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            LazyHorizontalGrid(
                                rows = GridCells.Fixed(1),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp)
                            ) {
                                items(product.moreProducts.size) { index ->
                                    val item = product.moreProducts[index]
                                    CommonProductCard(
                                        product = CommonProductCardData(
                                            imageUrl = item.imageUrl,
                                            productName = item.name,
                                            brandName = item.sellerName,
                                            currentPrice = item.discountedPrice.toInt().toString(),
                                            originalPrice = item.originalPrice.toInt().toString(),
                                            rating = "4.5"
                                        ),
                                        modifier = Modifier.width(180.dp),
                                        onClick = {
                                            navController.navigate(Route.ProductDetails.createRoute(item.productId))
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Reviews & Ratings
                        Text(
                            text = "Reviews & Ratings :",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Rating box
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.width(100.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "4.5",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("6 Ratings", fontSize = 11.sp, color = Color.White)
                                    Text("2 Reviews", fontSize = 11.sp, color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.width(20.dp))

                            // Rating breakdown
                            Column(modifier = Modifier.weight(1f)) {
                                RatingProgressBar("Very Good", 1f, 6, Color(0xFF4CAF50))
                                Spacer(modifier = Modifier.height(6.dp))
                                RatingProgressBar("Good", 0f, 0, Color(0xFF8BC34A))
                                Spacer(modifier = Modifier.height(6.dp))
                                RatingProgressBar("Ok-ok", 0f, 0, Color(0xFFFFC107))
                                Spacer(modifier = Modifier.height(6.dp))
                                RatingProgressBar("Bad", 0f, 0, Color(0xFFFF9800))
                                Spacer(modifier = Modifier.height(6.dp))
                                RatingProgressBar("Very Bad", 0f, 0, Color(0xFFF44336))
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Review cards
                        ReviewCard(
                            userName = "Aravind",
                            userImage = R.drawable.profile_icon,
                            rating = 4.5f,
                            reviewText = "This product has been a game changer for my farming needs. It is efficient, easy to use, and incredibly reliable. Highly recommended",
                            timeAgo = "1 Day ago",
                            likeCount = 12,
                            onUnLikeClick = {},
                            onLikeClick = { },
                            unLikeCount = 2
                        )

                        ReviewCard(
                            userName = "Sandhya",
                            userImage = R.drawable.profile_icon,
                            rating = 4.5f,
                            reviewText = "This product has been a game changer for my farming needs. It is efficient, easy to use, and incredibly reliable. Highly recommended",
                            timeAgo = "1 Day ago",
                            likeCount = 12,
                            onLikeClick = { },
                            unLikeCount = 4,
                            onUnLikeClick = {}
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductDetailsBottomBar(
    cartState: CartActionState,
    isInCart: Boolean,
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BGWhite)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onAddToCart,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = cartState !is CartActionState.Loading && !isInCart,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isInCart) Color.Gray else BGBlack,
                contentColor = BGWhite,
                disabledContainerColor = Color.Gray
            )
        ) {
            if (cartState is CartActionState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = BGWhite,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    painter = painterResource(
                        if (isInCart) R.drawable.ic_cart_outlined else R.drawable.ic_cart_outlined
                    ),
                    contentDescription = if (isInCart) "In Cart" else "Add to Cart",
                    modifier = Modifier.size(20.dp),
                    tint = if (isInCart) BGWhite else BGWhite
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                if (isInCart) "In Cart" else "Add to Cart",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isInCart) Color.White else BGWhite
            )
        }

        Button(
            onClick = onBuyNow,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BGWhite,
                contentColor = BGBlack
            ),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_buy_now),
                contentDescription = "Buy Now",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Buy Now", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

