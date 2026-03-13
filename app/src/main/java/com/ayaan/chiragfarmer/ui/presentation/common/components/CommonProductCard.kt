package com.ayaan.chiragfarmer.ui.presentation.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.presentation.sell.components.DeleteItemBox
import com.ayaan.chiragfarmer.ui.presentation.sell.components.EditItemBox
import com.ayaan.chiragfarmer.ui.theme.BGBlack
import com.ayaan.chiragfarmer.ui.theme.BGWhite
import com.ayaan.chiragfarmer.ui.theme.BorderGray
import com.ayaan.chiragfarmer.ui.theme.DisabledButtonGray
import com.ayaan.chiragfarmer.ui.theme.TextDarkGray

data class CommonProductCardData(
    val imageRes: Int? = null,
    val imageUrl: String? = null,
    val productName: String,
    val brandName: String,
    val currentPrice: String,
    val originalPrice: String? = null,
    val rating: String,
    val isSoldOut: Boolean = true
)
@Composable
fun CommonProductCard(
    modifier: Modifier = Modifier,
    product: CommonProductCardData,
    onMarkAsSoldClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(), colors = CardDefaults.cardColors(
            containerColor = BGWhite
        ), border = BorderStroke(
            width = 1.dp, color = BorderGray
        ), shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {

                if (product.imageUrl != null) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.productName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.sprayer),
                        error = painterResource(R.drawable.sprayer)
                    )
                } else if (product.imageRes != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center),
                            color = BGBlack,
                            strokeWidth = 2.dp
                        )
                    }
                }

                // Edit button overlay
                if (!product.isSoldOut) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        EditItemBox(onEdit = onEditClick)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 2.dp)
            ) {
                // Product Name
                Text(
                    text = product.productName,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.W600,
                    color = Color.Black,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Brand Name
                Text(
                    text = product.brandName, fontSize = 10.sp, fontWeight = W400, color = TextDarkGray
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Price and Rating Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Price
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "₹${product.currentPrice}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BGBlack
                        )
                        if (product.originalPrice != null) {
                            Text(
                                text = product.originalPrice,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal,
                                color = TextDarkGray,
                                style = TextStyle(
                                    textDecoration = TextDecoration.LineThrough
                                )
                            )
                        }
                    }

                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_rating_star),
                            contentDescription = "Rating",
                            tint = Color(0xffF7E62D),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = product.rating, fontSize = 10.sp, fontWeight = W400, color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val backgroundColor = if (product.isSoldOut) DisabledButtonGray else BGBlack
                    val text = if (product.isSoldOut) "Marked as Sold" else "Mark as Sold"
                    val clickableModifier =
                        if (product.isSoldOut) Modifier else Modifier.clickable { onMarkAsSoldClick() }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(6.dp))
                            .background(backgroundColor)
                            .then(clickableModifier)
                            .padding(start = 4.dp, top = 2.dp, bottom = 2.dp, end = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Cancel,
                                contentDescription = text,
                                modifier = Modifier.size(12.dp),
                                tint = BGWhite
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = text,
                                fontSize = 12.sp,
                                fontWeight = W400,
                                color = BGWhite,
                                maxLines = 1,
                                overflow = TextOverflow.Clip
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    DeleteItemBox(onDelete = onDeleteClick)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}