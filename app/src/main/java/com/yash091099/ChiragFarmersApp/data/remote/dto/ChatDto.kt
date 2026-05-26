package com.yash091099.ChiragFarmersApp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChatMessageRequestDto(
    @SerializedName("message")
    val message: String
)

data class ChatMessageDto(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("timestamp")
    val timestamp: String? = null
)

data class ChatHistoryResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<ChatMessageDto> = emptyList()
)

data class ChatReplyDto(
    @SerializedName("reply")
    val reply: String,
    @SerializedName("timestamp")
    val timestamp: String? = null
)

data class ChatSendResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: ChatReplyDto? = null
)
