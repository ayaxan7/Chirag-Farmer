package com.yash091099.ChiragFarmersApp.ui.presentation.assist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navhost.Route
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderGray

@Composable
fun AssistScreen(navController: NavHostController) {
    val snackBarHostState = remember { SnackbarHostState() }
    val langTag = LocalConfiguration.current.locales[0].toLanguageTag()
    val banner=when {
        langTag.startsWith("hi") -> R.drawable.assist_screen_banner_hi
        langTag.startsWith("te") -> R.drawable.assist_screen_banner_te
        langTag.startsWith("pa")-> R.drawable.assist_screen_banner_pu
        else -> R.drawable.assist_screen_banner
    }
    Scaffold(
        containerColor = BGWhite,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            ChiragTopBar(
                navController = navController, icon = R.drawable.ic_arrow,title = stringResource(R.string.assist_title)
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = banner),
                contentDescription = stringResource(R.string.assist_banner_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.height(16.dp))
            AssistCard(
                iconRes = R.drawable.get_help,
                title = stringResource(R.string.assist_plant_trouble_title),
                description = stringResource(R.string.assist_card_plant_diagnosis),
                onClick = {navController.navigate(Route.AssistImage.path)}
            )
            Spacer(modifier = Modifier.height(16.dp))
            AssistCard(
                iconRes = R.drawable.qna,
                title = stringResource(R.string.assist_ask_solve_title),
                description = stringResource(R.string.assist_card_ask_solve),
                onClick = { navController.navigate(Route.PlantProblemHelp.path) }
            )
        }
    }
}

@Composable
private fun AssistCard(
    onClick: () -> Unit = {},
    iconRes: Int,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        border = BorderStroke(1.dp, BorderGray),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // LEFT CONTENT
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = description, fontSize = 12.sp, lineHeight = 16.sp, color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))

                // BUTTON
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(BGBlack)
                        .clickable {
                            onClick()
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_right_arrow),
                        contentDescription = stringResource(R.string.assist_start_icon_description),
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.assist_start_now),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // RIGHT ICON
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = stringResource(R.string.assist_icon_description),
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

