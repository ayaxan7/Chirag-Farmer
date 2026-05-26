package com.yash091099.ChiragFarmersApp.data.remote

import com.yash091099.ChiragFarmersApp.data.remote.dto.ChatHistoryResponseDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.ChatMessageRequestDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.ChatSendResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatApiService {

    @POST("api/farmers/ai/chat")
    suspend fun getChatHistory(
        @Header("Authorization") token: String,
        @Body body: ChatMessageRequestDto
    ): ChatHistoryResponseDto

    @POST("api/farmers/ai/chat")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body body: ChatMessageRequestDto
    ): ChatSendResponseDto
}
