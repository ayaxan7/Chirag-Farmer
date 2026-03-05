package com.ayaan.chiragfarmer.ui.presentation.navigation.navbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ayaan.chiragfarmer.R
import com.ayaan.chiragfarmer.ui.theme.BGBlack
import com.ayaan.chiragfarmer.ui.theme.BGWhite
import com.ayaan.chiragfarmer.ui.theme.ChiragFarmerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChiragTopBar(
    navController: NavHostController,
    title: String = "",
    icon: Int? = null,
    buttonText: String? = null,
    buttonIcon: ImageVector? = null,
    onButtonClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
        Text(
            text = title,
            color = Color.Black,
            modifier = Modifier.padding(start = 16.dp),
            fontWeight = W500,
            fontSize = 20.sp,
            lineHeight = 24.sp
        )
    }, navigationIcon = {
        if (icon != null) {
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
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = BGWhite, titleContentColor = Color.Black
    ), actions = {
            if (buttonText != null && buttonIcon != null) {
                Button(
                    onClick = onButtonClick,
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BGBlack,
                        contentColor = BGWhite
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                ) {
                    Icon(
                        imageVector = buttonIcon,
                        contentDescription = buttonText,
                        tint = BGWhite,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = buttonText,
                        color = BGWhite,
                        fontSize = 14.sp,
                        fontWeight = W500,
                        maxLines = 1,
                        overflow = Ellipsis
                    )
                }
            }
        })
}

@Preview(showBackground = true)
@Composable
fun ChiragTopBarPreview() {
    val navController = rememberNavController()
    ChiragFarmerTheme {
        ChiragTopBar(
            navController = navController,
            title = "Marketplace",
            icon = R.drawable.ic_arrow,
            buttonText = "Sell Product",
            buttonIcon = Icons.Default.Add,
            onButtonClick = { /* navigate to sell screen */ }
        )
    }
}