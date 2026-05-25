package com.yash091099.ChiragFarmersApp.ui.presentation.assist.screens.assistResult

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite

@Composable
fun AssistResult(
    navController: NavHostController,
    viewModel: AssistResultViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val imageUri = viewModel.getImageUri()

    Scaffold(
        containerColor = BGWhite,
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = "Assist",
                icon = R.drawable.ic_arrow
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (imageUri.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xff3BB69A), RoundedCornerShape(12.dp))
                ) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Selected crop image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            when (val state = uiState) {
                AssistResultUiState.Loading -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = BGBlack)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Analyzing crop image...",
                                color = BGBlack,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                is AssistResultUiState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3F3)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = state.message,
                                color = Color(0xFFD32F2F),
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { viewModel.retry() },
                                colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                            ) {
                                Text(text = "Retry", color = BGWhite)
                            }
                        }
                    }
                }

                is AssistResultUiState.Success -> {
                    AssistResultContent(
                        data = state.data,
                        onAnalyzeAnother = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
private fun AssistResultContent(
    data: CropAnalysisUiModel,
    onAnalyzeAnother: () -> Unit
) {
    Text(
        text = "Disease Identified: ${data.diseaseName}",
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BGBlack
    )

    data.confidence?.let {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Confidence: $it%",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xff3BB69A)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = data.about,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = BGBlack
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Crop: ${data.cropName}",
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BGBlack
    )

    Spacer(modifier = Modifier.height(16.dp))

    SectionTitle(text = "Symptoms Identified")
    Spacer(modifier = Modifier.height(8.dp))
    if (data.symptoms.isEmpty()) {
        EmptySectionText(text = "No symptoms were returned by the analysis.")
    } else {
        data.symptoms.forEach { symptom ->
            BulletPointText(symptom)
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    SectionTitle(text = "Avoid")
    Spacer(modifier = Modifier.height(8.dp))
    if (data.avoid.isEmpty()) {
        EmptySectionText(text = "No avoidance tips were returned by the analysis.")
    } else {
        data.avoid.forEach { item ->
            BulletPointText(item)
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    SectionTitle(text = "Insecticides (Spray once every 7-10 days)")
    Spacer(modifier = Modifier.height(12.dp))
    InsecticideTable(data.insecticides)

    Spacer(modifier = Modifier.height(20.dp))

    OutlinedButton(
        onClick = onAnalyzeAnother,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = "Analyze Another Image", color = BGBlack)
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = BGBlack
    )
}

@Composable
fun BulletPointText(text: String) {
    Row(
        modifier = Modifier.padding(bottom = 4.dp, start = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = "• ", fontSize = 14.sp, color = Color.Gray)
        Text(text = text, fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
private fun EmptySectionText(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 8.dp)
    )
}

@Composable
fun InsecticideTable(items: List<CropInsecticideUiModel>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Pesticide Name",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                "Dosage/Use",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                "Target",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )
        }

        HorizontalDivider(color = Color(0xFFE0E0E0))

        if (items.isEmpty()) {
            Text(
                text = "No insecticides were returned by the analysis.",
                modifier = Modifier.padding(12.dp),
                fontSize = 13.sp,
                color = Color.Gray
            )
        } else {
            items.forEachIndexed { index, item ->
                TableRow(
                    name = item.pesticideName,
                    dosage = item.dosageUse,
                    target = item.target,
                    isLast = index == items.lastIndex
                )
            }
        }
    }
}

@Composable
fun TableRow(name: String, dosage: String, target: String, isLast: Boolean = false) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, fontSize = 12.sp, modifier = Modifier.weight(1f))
            Text(dosage, fontSize = 12.sp, modifier = Modifier.weight(1f))
            Text(target, fontSize = 12.sp, modifier = Modifier.weight(1f))
        }
        if (!isLast) {
            HorizontalDivider(color = Color(0xFFE0E0E0))
        }
    }
}
