package com.yash091099.ChiragFarmersApp.ui.presentation.assist.screens.assistResult

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGBlack
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.ChiragFarmerTheme

@Composable
fun AssistResult(
    navController: NavHostController,
    viewModel: AssistResultViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val imageUri = viewModel.getImageUri()

    AssistResultScreen(
        uiState = uiState,
        imageUri = imageUri,
        onRetry = { viewModel.retry() },
        onAnalyzeAnother = { navController.popBackStack() },
        navController = navController
    )
}

@Composable
fun AssistResultScreen(
    uiState: AssistResultUiState,
    imageUri: String,
    onRetry: () -> Unit,
    onAnalyzeAnother: () -> Unit,
    navController: NavHostController
) {
    Scaffold(
        containerColor = BGWhite,
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = "Assist",
                icon = R.drawable.ic_arrow
            )
        },
        bottomBar = {
            OutlinedButton(
                onClick = onAnalyzeAnother,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "Analyze Another Image", color = BGBlack)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (uiState) {
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
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
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
                                text = uiState.message,
                                color = Color(0xFFD32F2F),
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onRetry,
                                colors = ButtonDefaults.buttonColors(containerColor = BGBlack)
                            ) {
                                Text(text = "Retry", color = BGWhite)
                            }
                        }
                    }
                }

                is AssistResultUiState.Success -> {
                    AssistResultContent(
                        data = uiState.data,
                        imageUri = imageUri
                    )
                }
            }
        }
    }
}

@Composable
private fun AssistResultContent(
    data: CropAnalysisUiModel,
    imageUri: String = ""
) {
    Text(
        text = "Disease Identified: ${data.diseaseName}",
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BGBlack
    )
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
    Column {
        Text(
            text = data.about,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            color = BGBlack,
            fontWeight = FontWeight.Bold
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Crop: ${data.cropName}",
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = BGBlack
    )

    Spacer(modifier = Modifier.height(8.dp))
    SectionTitle(text = "Symptoms Identified")
    Spacer(modifier = Modifier.height(4.dp))
    if (data.symptoms.isEmpty()) {
        EmptySectionText(text = "No symptoms were returned by the analysis.")
    } else {
        data.symptoms.forEach { symptom ->
            BulletPointText(symptom)
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    SectionTitle(text = "Avoid")
    Spacer(modifier = Modifier.height(4.dp))
    if (data.avoid.isEmpty()) {
        EmptySectionText(text = "No avoidance tips were returned by the analysis.")
    } else {
        data.avoid.forEach { item ->
            BulletPointText(item)
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    SectionTitle(text = "Insecticides (Spray once every 7-10 days)")
    Spacer(modifier = Modifier.height(4.dp))
    InsecticideTable(data.insecticides)
    Spacer(modifier = Modifier.height(20.dp))
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
        modifier = Modifier.padding(bottom = 2.dp, start = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = "• ", fontSize = 14.sp, color = BGBlack, lineHeight = 14.sp)
        Text(text = text, fontSize = 14.sp, color = BGBlack, lineHeight = 14.sp)
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
                .padding(start=12.dp, top = 2.dp, bottom = 2.dp, end = 12.dp),
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
                .padding(start=12.dp, top = 2.dp, bottom = 2.dp, end = 12.dp),
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

@Preview(showBackground = true)
@Composable
private fun AssistResultContentPreview() {
    ChiragFarmerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BGWhite)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AssistResultContent(
                data = CropAnalysisUiModel(
                    cropName = "Tomato",
                    diseaseName = "Early Blight",
                    confidence = 92,
                    about = "Early blight is a common fungal disease that affects tomato plants, caused by the fungus Alternaria solani. It typically appears as dark spots with concentric rings on older leaves.",
                    symptoms = listOf(
                        "Small dark spots on older leaves",
                        "Spots enlarge into concentric rings (target effect)",
                        "Yellowing of leaves around spots",
                        "Stem lesions and fruit rot in severe cases"
                    ),
                    avoid = listOf(
                        "Overhead irrigation (keep leaves dry)",
                        "Crowded planting (ensure good airflow)",
                        "Planting in the same soil every year",
                        "Ignoring early symptoms"
                    ),
                    insecticides = listOf(
                        CropInsecticideUiModel("Chlorothalonil", "2.5g/L", "Fungal spores"),
                        CropInsecticideUiModel("Copper Fungicide", "5ml/L", "Fungal growth"),
                        CropInsecticideUiModel("Mancozeb", "2g/L", "Early blight control")
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AssistResultSuccessPreview() {
    val navController = rememberNavController()
    ChiragFarmerTheme {
        AssistResultScreen(
            uiState = AssistResultUiState.Success(
                CropAnalysisUiModel(
                    cropName = "Tomato",
                    diseaseName = "Early Blight",
                    confidence = 92,
                    about = "Early blight is a common fungal disease that affects tomato plants, caused by the fungus Alternaria solani.",
                    symptoms = listOf("Small dark spots", "Yellowing leaves"),
                    avoid = listOf("Overhead irrigation", "Crowded planting"),
                    insecticides = listOf(
                        CropInsecticideUiModel("Chlorothalonil", "2.5g/L", "Fungal spores")
                    )
                )
            ),
            imageUri = "",
            onRetry = {},
            onAnalyzeAnother = {},
            navController = navController
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AssistResultLoadingPreview() {
    val navController = rememberNavController()
    ChiragFarmerTheme {
        AssistResultScreen(
            uiState = AssistResultUiState.Loading,
            imageUri = "",
            onRetry = {},
            onAnalyzeAnother = {},
            navController = navController
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AssistResultErrorPreview() {
    val navController = rememberNavController()
    ChiragFarmerTheme {
        AssistResultScreen(
            uiState = AssistResultUiState.Error("Failed to analyze crop image."),
            imageUri = "",
            onRetry = {},
            onAnalyzeAnother = {},
            navController = navController
        )
    }
}
