package com.ayaan.chiragfarmer.ui.presentation.buy.screens.details

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.buy.screens.details.components.RatingProgressBar
import com.ayaan.chiragfarmer.ui.presentation.buy.screens.details.components.ReviewCard
import com.ayaan.chiragfarmer.ui.presentation.buy.screens.details.components.VariantChip
import com.ayaan.chiragfarmer.ui.theme.BGBlack
import com.ayaan.chiragfarmer.ui.theme.BGWhite
import com.ayaan.chiragfarmer.ui.theme.BorderColour

@Composable
fun ProductDetailsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var selectedVariant by remember { mutableIntStateOf(0) }
    var selectedImageIndex by remember { mutableIntStateOf(0) }

    // Sample data
    val productImages = listOf(
        R.drawable.sprayer,
        R.drawable.sprayer,
        R.drawable.sprayer,
        R.drawable.sprayer,
        R.drawable.sprayer
    )

    val variants = listOf(
        Variant("1 unit", "1999", "2799", "20% OFF"),
        Variant("2 unit", "3998", "5598", "20% OFF")
    )

    Scaffold(
        modifier = modifier,
        containerColor = BGWhite,
        topBar = {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BGWhite)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }
                Text(
                    text = "Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
                IconButton(onClick = { /* Share */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.Black)
                }
                IconButton(onClick = { /* Camera */ }) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = Color.Black)
                }
            }
        },
        bottomBar = {
            // Bottom action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BGWhite)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Add to cart */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, Color.Black)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_cart),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add to Cart", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }

                Button(
                    onClick = { /* Buy now */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BGBlack
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_cart),
                        contentDescription = "Cart",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Buy Now", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    ) { paddingValues ->
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
                Image(
                    painter = painterResource(id = productImages[selectedImageIndex]),
                    contentDescription = "Product",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                // Thumbnail images on left
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    productImages.forEachIndexed { index, imageRes ->
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = "Thumbnail $index",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = if (index == selectedImageIndex) 2.dp else 0.5.dp,
                                    color = if (index == selectedImageIndex) Color.Black else Color(0xFFE0E0E0),
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
                        text = "NEPTUNE BATTERY OPERATED",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
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

                // Brand
                Text(
                    text = "Geolite Agritech India Pvt Ltd",
                    fontSize = 13.sp,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Price
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹1999",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "₹2799.00",
                        fontSize = 14.sp,
                        color = Color(0xFF999999),
                        textDecoration = TextDecoration.LineThrough
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Variants
                Text(
                    text = "Variants :",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    variants.forEachIndexed { index, variant ->
                        VariantChip(
                            label = variant.label,
                            price = variant.price,
                            originalPrice = variant.originalPrice,
                            discountPercentage = variant.discount,
                            isSelected = selectedVariant == index,
                            onClick = { selectedVariant = index }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Product Description
                Text(
                    text = "Product Description",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Neptune 20 Litre White Knapsack Power Sprayer, NF-767 is a premium quality product. These are far superior to the cheap popular equipment used for spraying insecticides, pesticides in fields a...",
                    fontSize = 13.sp,
                    color = Color(0xFF666666),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Key Features
                Text(
                    text = "Key Features :",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))

                listOf(
                    "Safe, Quick & Reliable.",
                    "Integrated Battery & Chemical Tank.",
                    "Recoil Ignition Makes the Starting Easy.",
                    "Oil Ratio: Add 20W40 Oil Separately in Oil Tank.",
                    "Spraying Horizontal Range: 10-12 m."
                ).forEach { feature ->
                    Row(modifier = Modifier.padding(vertical = 2.dp)) {
                        Text("• ", fontSize = 13.sp, color = Color.Black)
                        Text(
                            text = feature,
                            fontSize = 13.sp,
                            color = Color(0xFF666666),
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Sold by
                Text(
                    text = "Sold by",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile_icon),
                        contentDescription = "Seller",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Geolite Agritech, India",
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
                        colors = ButtonDefaults.buttonColors(containerColor = BGBlack),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cart),
                            contentDescription = "Store",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("View Seller", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Similar Products
                Text(
                    text = "Similar Products",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(2) {
                        SimilarProductCard()
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // More Products for you
                Text(
                    text = "More Products for you",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(2) {
                        MoreProductCard()
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
                    reviewText = "This Power Sprayer has been a game changer for my farming needs. It is efficient, easy to use, and incredibly reliable. Highly recommended",
                    timeAgo = "1 Day ago",
                    likeCount = 12,
                    onLikeClick = { }
                )

                ReviewCard(
                    userName = "Sandhya",
                    userImage = R.drawable.profile_icon,
                    rating = 4.5f,
                    reviewText = "This Power Sprayer has been a game changer for my farming needs. It is efficient, easy to use, and incredibly reliable. Highly recommended",
                    timeAgo = "1 Day ago",
                    likeCount = 12,
                    onLikeClick = { }
                )
            }
        }
    }
}

data class Variant(
    val label: String,
    val price: String,
    val originalPrice: String,
    val discount: String
)

@Composable
fun SimilarProductCard() {
    Card(
        modifier = Modifier.width(160.dp),
        colors = CardDefaults.cardColors(containerColor = BGWhite),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, BorderColour)
    ) {
        Column {
            Image(
                painter = painterResource(R.drawable.sprayer),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text("NEPTUNE BATTERY OPERATED", fontSize = 11.sp, fontWeight = FontWeight.Medium, maxLines = 1)
                Text("Geolite Agritech India Pvt Ltd", fontSize = 9.sp, color = Color(0xFF999999), maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("₹1599", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("₹1899.00", fontSize = 10.sp, color = Color(0xFF999999), textDecoration = TextDecoration.LineThrough)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        Text("Size", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Row(
                            modifier = Modifier
                                .border(1.dp, BorderColour, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("1 Unit", fontSize = 10.sp)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(12.dp))
                        Text("4.8", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun MoreProductCard() {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(180.dp),
        colors = CardDefaults.cardColors(containerColor = BGWhite),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.sprayer),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("4WD TRACTORS", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color.White)
                Text("Geolife Agritech India Pvt Ltd", fontSize = 9.sp, color = Color.White.copy(alpha = 0.8f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("₹22990", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(12.dp))
                    Text("4.8", fontSize = 10.sp, color = Color.White)
                }
            }
        }
    }
}
