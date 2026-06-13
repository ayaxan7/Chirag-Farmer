package com.yash091099.ChiragFarmersApp.ui.presentation.home.components.bookservicecard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.ChiragFarmerTheme

@Composable
fun BookServiceCard(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedService by remember { mutableStateOf<String?>(null) }

    BookServiceCardContent(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        selectedService = selectedService,
        onServiceSelected = {
            selectedService = it
            expanded = false
        },
        navController=navController
    )
}

@Composable
fun BookServiceCardContent(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedService: String?,
    onServiceSelected: (String) -> Unit,
    navController: NavHostController
) {
    val services = listOf(
        "Drone Spraying",
        "Soil Testing",
        "Tractor Rental",
        "Soil Testing",
        "Tractor Rental",
        "Soil Testing",
        "Tractor Rental"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(165.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Background Image
                Image(
                    painter = painterResource(R.drawable.book_service_card),
                    contentDescription = stringResource(R.string.service_book_card_description),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Heading
                Text(
                    text = "Need Farm\nServices?\nBook in Minutes",
                    color = BGWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W700,
                    lineHeight = 18.sp,
                    modifier = Modifier
                        .padding(start = 12.dp, top = 8.dp)
                        .align(Alignment.TopStart)
                )

                // Bottom Section
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "From drone spraying, soil testing\nconnect with verified providers instantly",
                        color = BGWhite,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 12.sp,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(300.dp)
                            .height(36.dp)
                            .shadow(
                                elevation = 4.46.dp,
                                shape = RoundedCornerShape(8.dp),
                                ambientColor = Color.Black.copy(alpha = 0.25f),
                                spotColor = Color.Black.copy(alpha = 0.25f)
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.20f))
                            .border(
                                width = 0.5.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onExpandedChange(true) },
                                contentAlignment = Alignment.CenterStart
                            ) {

                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = selectedService ?: "Select Service",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Spacer(modifier = Modifier.weight(1f))

                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = stringResource(R.string.service_dropdown_description),
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(18.dp)
                                            .padding(end = 4.dp)
                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { onExpandedChange(false) },
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .width(300.dp)
                                ) {
                                    services.forEach { service ->
                                        DropdownMenuItem(
                                            text = {
                                            Text(
                                                text = service,
                                                color = Color.Black,
                                                fontSize = 12.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }, onClick = {
                                            onServiceSelected(service)
                                        }, colors = MenuDefaults.itemColors(
                                            textColor = Color.Black
                                        ), modifier = Modifier
                                                .fillMaxWidth()
                                                .shadow(
                                                    elevation = 4.46.dp,
                                                    shape = RoundedCornerShape(8.dp),
                                                    ambientColor = Color.Black.copy(alpha = 0.25f),
                                                    spotColor = Color.Black.copy(alpha = 0.25f)
                                                )
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.White.copy(alpha = 0.20f))
                                                .border(
                                                    width = 0.5.dp,
                                                    color = Color.White,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                        )
                                    }
                                }
                            }
                            VerticalDivider(
                                modifier = Modifier
                                    .fillMaxHeight(0.8f)
                                    .width(1.dp)
                                    .background(Color.White.copy(alpha = 0.6f))
                            )
                            Spacer(modifier = Modifier.width(18.dp))
                            Button(
                                modifier = Modifier
                                    .height(18.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color.Transparent)
                                    .padding(horizontal = 24.dp),
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black, contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 14.dp)
                            ) {
                                Text(
                                    text = "Book Now",
                                    color = Color.White,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}

@Preview(showBackground = true)
@Composable
fun BookServiceCardPreview() {
    ChiragFarmerTheme {
        BookServiceCard(
            navController = rememberNavController()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookServiceCardExpandedPreview() {
    ChiragFarmerTheme {
        BookServiceCardContent(
            expanded = true,
            onExpandedChange = {},
            selectedService = null,
            onServiceSelected = {},
            navController = rememberNavController()
        )
    }
}
