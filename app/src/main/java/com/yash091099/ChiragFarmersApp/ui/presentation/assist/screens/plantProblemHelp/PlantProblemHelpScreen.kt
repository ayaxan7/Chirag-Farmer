package com.yash091099.ChiragFarmersApp.ui.presentation.assist.screens.plantProblemHelp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.ui.presentation.navigation.navbar.ChiragTopBar
import com.yash091099.ChiragFarmersApp.ui.theme.BGWhite
import com.yash091099.ChiragFarmersApp.ui.theme.BorderGray
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class Sender {
    BOT, USER
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val content: String,
    val sender: Sender,
    val timestamp: String = getCurrentTime(),
    val isOptionsCard: Boolean = false,
    val options: List<String> = emptyList()
)

fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(Date()).lowercase()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlantProblemHelpScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                content = "Hello! 👋 I'm here to help you identify problems in your crop.",
                sender = Sender.BOT
            ),
            ChatMessage(
                content = "Can you tell me which crop you're facing issues with?",
                sender = Sender.BOT
            )
        )
    }

    var selectedCrop by remember { mutableStateOf<String?>(null) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var userInputValue by remember { mutableStateOf("") }

    // Scroll to bottom when messages list size changes
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        containerColor = BGWhite,
        topBar = {
            ChiragTopBar(
                navController = navController,
                title = "Plant Problem Help",
                icon = R.drawable.ic_arrow
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .navigationBarsPadding()
        ) {
            // Message List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .imeNestedScroll()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    if (message.isOptionsCard) {
                        BotOptionsCard(
                            message = message,
                            onOptionClick = { option ->
                                if (selectedOption == null) {
                                    selectedOption = option
                                    scope.launch {
                                        // 1. Add User Choice Message
                                        messages.add(
                                            ChatMessage(
                                                content = option,
                                                sender = Sender.USER
                                            )
                                        )
                                        delay(1000)

                                        // 2. Add Bot Reply
                                        if (option == "Other") {
                                            messages.add(
                                                ChatMessage(
                                                    content = "Could you please describe the symptoms you are seeing in your crop?",
                                                    sender = Sender.BOT
                                                )
                                            )
                                        } else {
                                            val responseText = when (option) {
                                                "Yellow leaves" -> "Yellow leaves can be caused by nitrogen deficiency or overwatering. Ensure proper drainage and apply a balanced NPK fertilizer."
                                                "Leaf curling" -> "Leaf curling is often due to insect damage (aphids/thrips) or viral infection. Spray organic neem oil or soap water solution."
                                                "Spots on leaves" -> "Leaf spots are typically fungal or bacterial. Prune infected leaves, avoid overhead watering, and apply an organic fungicide if needed."
                                                "Wilting" -> "Wilting can suggest root rot from overwatering or fungal wilt. Let the soil dry between waterings and check root health."
                                                else -> "Thank you for sharing! We suggest keeping the crop dry and clean. Our experts are analyzing the issue."
                                            }
                                            messages.add(
                                                ChatMessage(
                                                    content = responseText,
                                                    sender = Sender.BOT
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    } else {
                        MessageBubble(message = message)
                    }
                }
            }

            // Quick Crop Suggestion Chips (only visible before crop is chosen)
            if (selectedCrop == null) {
                CropSuggestionsRow(
                    onCropSelected = { crop ->
                        selectedCrop = crop
                        scope.launch {
                            messages.add(ChatMessage(content = crop, sender = Sender.USER))
                            delay(1000)
                            messages.add(
                                ChatMessage(
                                    content = "Thanks! What's the issue you've noticed in your ${crop.lowercase()} plants?",
                                    sender = Sender.BOT,
                                    isOptionsCard = true,
                                    options = listOf(
                                        "Yellow leaves",
                                        "Leaf curling",
                                        "Spots on leaves",
                                        "Wilting",
                                        "Other"
                                    )
                                )
                            )
                        }
                    }
                )
            }

            // Bottom Input Field
            ChatInputBar(
                value = userInputValue,
                onValueChange = { userInputValue = it },
                onSendClick = {
                    if (userInputValue.trim().isNotEmpty()) {
                        val typedText = userInputValue.trim()
                        userInputValue = ""
                        focusManager.clearFocus()
                        scope.launch {
                            // User Message
                            messages.add(ChatMessage(content = typedText, sender = Sender.USER))

                            // If crop was not selected, set it to the first typed text
                            if (selectedCrop == null) {
                                selectedCrop = typedText
                                delay(1000)
                                messages.add(
                                    ChatMessage(
                                        content = "Thanks! What's the issue you've noticed in your ${typedText.lowercase()} plants?",
                                        sender = Sender.BOT,
                                        isOptionsCard = true,
                                        options = listOf(
                                            "Yellow leaves",
                                            "Leaf curling",
                                            "Spots on leaves",
                                            "Wilting",
                                            "Other"
                                        )
                                    )
                                )
                            } else {
                                delay(1000)
                                // Smart response logic based on input keywords
                                val lower = typedText.lowercase()
                                val responseText = when {
                                    lower.contains("curling") && lower.contains("yellow") ->
                                        "Leaves curling with yellow patches suggest a combination of Tomato Yellow Leaf Curl Virus (TYLCV) and magnesium deficiency. Apply organic insect spray to manage thrips/whiteflies and balance irrigation."
                                    lower.contains("curling") ->
                                        "Leaf curling is usually caused by aphids, thrips, or stress. Try spraying organic neem oil or insecticidal soap."
                                    lower.contains("yellow") ->
                                        "Yellowing leaves might mean nitrogen deficiency or root stress due to excessive water. Try a light nitrogen fertilizer."
                                    lower.contains("spot") || lower.contains("patches") ->
                                        "Spots or patches are likely fungal infections like early blight. Remove infected leaves immediately and improve air circulation."
                                    else ->
                                        "Thanks for the description! This sounds like a plant stress factor or mild nutrient deficit. Ensure adequate sunlight, check for pests under leaves, and irrigate moderately."
                                }
                                messages.add(ChatMessage(content = responseText, sender = Sender.BOT))
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val alignment = if (message.sender == Sender.USER) Alignment.End else Alignment.Start
    // Standard premium light-green bubble background
    val bubbleColor = Color(0xFFF0FDF4)
    val textColor = Color(0xFF1E293B)
    val bubbleShape = RoundedCornerShape(12.dp)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(bubbleShape)
                .background(bubbleColor)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column {
                Text(
                    text = message.content,
                    color = textColor,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.timestamp,
                    color = Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun BotOptionsCard(
    message: ChatMessage,
    onOptionClick: (String) -> Unit
) {
    val bubbleColor = Color(0xFFF0FDF4)
    val textColor = Color(0xFF1E293B)
    val bubbleShape = RoundedCornerShape(12.dp)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(bubbleShape)
                .background(bubbleColor)
                .padding(vertical = 10.dp)
        ) {
            Column {
                // Header
                Text(
                    text = message.content,
                    color = textColor,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Options List
                message.options.forEach { option ->
                    HorizontalDivider(
                        color = Color(0xFFE2E8F0),
                        thickness = 0.5.dp
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOptionClick(option) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = option,
                            color = Color(0xFF3BB69A), // Teal color from app theme
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.timestamp,
                    color = Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 14.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CropSuggestionsRow(
    onCropSelected: (String) -> Unit
) {
    val crops = listOf("Tomato", "Potato", "Chili", "Onion", "Wheat", "Rice")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = "Select a crop or type below:",
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            crops.forEach { crop ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFF1F5F9))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                        .clickable { onCropSelected(crop) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = crop,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF475569)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE2E8F0))
            .background(BGWhite)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TextField inside a styled container
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF8FAFC))
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            if (value.isEmpty()) {
                Text(
                    text = "Type your message...",
                    color = BorderGray,
                    fontSize = 14.sp
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = { onSendClick() }
                )
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Send Button
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF3BB69A)) // Teal background
                .clickable { onSendClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
