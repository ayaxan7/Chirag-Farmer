package com.yash091099.ChiragFarmersApp.ui.presentation.orders.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.orders.DropReviewState
import com.yash091099.ChiragFarmersApp.ui.presentation.orders.DropReviewViewModel
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.LightGray
import com.yash091099.ChiragFarmersApp.ui.theme.TextGray
import java.util.Locale
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropReviewScreen(
    navController: NavHostController,
    orderId: String,
    productId: String,
    imageUrl: String? = null,
    productName: String? = null,
    sellerName: String? = null,
    pricePaid: String? = null,
    viewModel: DropReviewViewModel = hiltViewModel()
) {
    var rating by remember { mutableIntStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    val submitState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(submitState) {
        if (submitState is DropReviewState.Success) {
            viewModel.resetState()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            ChiragTopBar(
                navController = navController, title = stringResource(R.string.review_title), icon = R.drawable.ic_arrow
            )
        }, containerColor = BGWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BGWhite)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f), contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    ReviewItemCard(
                        imageUrl = imageUrl,
                        title = productName ?: stringResource(R.string.fallback_product),
                        seller = sellerName ?: stringResource(R.string.fallback_seller),
                        price = formatPrice(pricePaid)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(R.string.review_order_id, orderId),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextGray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = stringResource(R.string.review_overall_rating),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (i in 1..5) {
                            Icon(
                                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = stringResource(R.string.review_star_description, i),
                                tint = if (i <= rating) Color(0xFFFFC107) else TextGray,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(4.dp)
                                    .clickable { rating = i })
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Text(
                        text = stringResource(R.string.review_write_label),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    val maxChar = 400

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .drawBehind {
                            drawRoundRect(
                                color = Color.LightGray, style = Stroke(
                                    width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(
                                            10f, 10f
                                        ), 0f
                                    )
                                ), cornerRadius = CornerRadius(8.dp.toPx())
                            )
                        }
                        .padding(2.dp)) {
                        TextField(
                            value = reviewText, onValueChange = {
                            if (it.length <= maxChar) reviewText = it
                        }, placeholder = {
                            Text(
                                stringResource(R.string.review_placeholder),
                                color = Color.DarkGray,
                                fontSize = 12.sp
                            )
                        }, colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ), modifier = Modifier.fillMaxSize()
                        )
                    }

                    if (submitState is DropReviewState.Error) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = (submitState as DropReviewState.Error).message,
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        text = stringResource(R.string.review_characters_remaining, maxChar - reviewText.length),
                        fontSize = 10.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        textAlign = TextAlign.End
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // Bottom Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black
                    )
                ) {
                    Text(stringResource(R.string.review_cancel), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }

                Button(
                    onClick = {
                        viewModel.submitReview(
                            orderId = orderId,
                            productId = productId,
                            rating = rating,
                            review = reviewText
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BGBlack
                    ),
                    enabled = rating > 0 && submitState !is DropReviewState.Loading
                ) {
                    if (submitState is DropReviewState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            stringResource(R.string.review_yes_submit),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewItemCard(
    imageUrl: String?,
    title: String,
    seller: String,
    price: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, LightGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    1.dp, LightGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp)
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.height(70.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = seller, fontSize = 12.sp, color = TextGray
                )
            }

            Column {
                Text(
                    text = price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

            }
        }
    }
}

@Composable
private fun formatPrice(pricePaid: String?): String {
    if (pricePaid.isNullOrBlank()) return "--"
    val value = pricePaid.toDoubleOrNull()
    return if (value != null) "${stringResource(R.string.currency_rs)}${
        String.format(
            Locale.getDefault(),
            "%.2f",
            value
        )
    }" else pricePaid
}

