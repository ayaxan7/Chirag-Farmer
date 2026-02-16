package com.ayaan.chiragfarmer.ui.presentation.navigation.navbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ayaan.chiragfarmer.ui.theme.BGWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChiragTopBar(navController: NavHostController,title:String="",icon:Int?=null){
    TopAppBar(
        title={
            Text(
                text = title,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = W400,
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        },
        navigationIcon = {
            if(icon!=null) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "Back",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(16.dp)
                        .clickable {
                            navController.popBackStack()
                        },
                    tint = Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BGWhite,
            titleContentColor = Color.Black
        )
    )
}