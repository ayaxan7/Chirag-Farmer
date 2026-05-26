package com.yash091099.ChiragFarmersApp.ui.presentation.assist.screens.plantProblemHelp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.remote.ChatApiService
import com.yash091099.ChiragFarmersApp.data.remote.dto.ChatMessageRequestDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PlantProblemHelpViewModel @Inject constructor(
    private val chatApiService: ChatApiService, private val chiragDataStore: ChiragDataStore
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        initializeChatSession()
    }

    fun initializeChatSession() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val token = chiragDataStore.getAuthToken().first()
                if (token.isNullOrEmpty()) {
                    _error.value = "No authentication token found"
                    _isLoading.value = false
                    return@launch
                }

                // Call the API with empty message to get history/initialize
                val response = chatApiService.getChatHistory(
                    token = "Bearer $token", body = ChatMessageRequestDto(message = "")
                )

                if (response.success) {
                    val chatMessages = response.data.map { dto ->
                        val sender = if (dto.role == "assistant") Sender.BOT else Sender.USER
                        ChatMessage(
                            content = dto.content,
                            sender = sender,
                            timestamp = formatTimestamp(dto.timestamp)
                        )
                    }

                    // If empty (e.g. API returned nothing, though it should return welcome message), provide fallback welcome message
                    if (chatMessages.isEmpty()) {
                        loadFallbackWelcome()
                    } else {
                        // Check if the last message is from the assistant and asks for the crop, or contains options
                        // We can dynamically add options to the last bot message if it's asking for symptoms
                        val processedMessages = chatMessages.mapIndexed { index, msg ->
                            if (index == chatMessages.lastIndex && msg.sender == Sender.BOT && (msg.content.contains(
                                    "issue",
                                    ignoreCase = true
                                ) || msg.content.contains("symptom", ignoreCase = true))
                            ) {
                                msg.copy(
                                    options = listOf(
                                        "Yellow leaves",
                                        "Leaf curling",
                                        "Spots on leaves",
                                        "Wilting",
                                        "Other"
                                    )
                                )
                            } else {
                                msg
                            }
                        }
                        _messages.value = processedMessages
                    }
                } else {
                    _error.value = response.message
                    loadFallbackWelcome()
                }
            } catch (e: Exception) {
                Log.e("PlantProblemHelpVM", "Failed to initialize chat: ${e.message}", e)
                _error.value = e.message ?: "Failed to connect to chatbot server"
                loadFallbackWelcome()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadFallbackWelcome() {
        if (_messages.value.isEmpty()) {
            _messages.value = listOf(
                ChatMessage(
                    content = "Hello, I am here to help you identify problems in your crop. Can you tell me which crop you are facing issues with?",
                    sender = Sender.BOT
                )
            )
        }
    }

    fun sendMessage(text: String) {
        if (text.trim().isEmpty()) return

        viewModelScope.launch {
            val userMsg = ChatMessage(
                content = text, sender = Sender.USER
            )
            // Add user message to the list immediately
            _messages.value = _messages.value + userMsg
            _isLoading.value = true

            try {
                val token = chiragDataStore.getAuthToken().first()
                if (token.isNullOrEmpty()) {
                    _messages.value += ChatMessage(
                        content = "Error: Not authenticated.", sender = Sender.BOT
                    )
                    return@launch
                }

                val response = chatApiService.sendMessage(
                    token = "Bearer $token", body = ChatMessageRequestDto(message = text)
                )

                if (response.success && response.data != null) {
                    val replyText = response.data.reply

                    // If reply contains questions about issues or symptoms, attach options card
                    val isOptionsNeeded = replyText.contains(
                        "issue",
                        ignoreCase = true
                    ) || replyText.contains(
                        "symptom",
                        ignoreCase = true
                    ) || replyText.contains("affecting", ignoreCase = true)

                    if (isOptionsNeeded) {
                        _messages.value += ChatMessage(
                            content = replyText, sender = Sender.BOT, options = listOf(
                                "Yellow leaves",
                                "Leaf curling",
                                "Spots on leaves",
                                "Wilting",
                                "Other"
                            ), timestamp = formatTimestamp(response.data.timestamp)
                        )
                    } else {
                        _messages.value += ChatMessage(
                            content = replyText,
                            sender = Sender.BOT,
                            timestamp = formatTimestamp(response.data.timestamp)
                        )
                    }
                } else {
                    _messages.value += ChatMessage(
                        content = "Failed to generate AI response: ${response.message}",
                        sender = Sender.BOT
                    )
                }
            } catch (e: Exception) {
                Log.e("PlantProblemHelpVM", "Failed to send message: ${e.message}", e)
                _messages.value += ChatMessage(
                    content = "Error: Failed to connect to server.", sender = Sender.BOT
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun formatTimestamp(isoString: String?): String {
        if (isoString.isNullOrEmpty()) return getCurrentTime()
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            val date = inputFormat.parse(isoString)
            val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            outputFormat.format(date ?: Date()).lowercase()
        } catch (e: Exception) {
            e.printStackTrace()
            getCurrentTime()
        }
    }
}
