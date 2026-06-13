package com.yash091099.ChiragFarmersApp.utils

import com.google.gson.Gson
import com.yash091099.ChiragFarmersApp.data.model.auth.AuthResponse
import retrofit2.HttpException

fun getErrorMessage(e: Exception): String {
    if (e is HttpException) {
        return try {
            val errorBody = e.response()?.errorBody()?.string()
            if (!errorBody.isNullOrBlank()) {
                val errorResponse = Gson().fromJson(errorBody, AuthResponse::class.java)
                errorResponse.message
            } else {
                e.message ?: "Network error occurred"
            }
        } catch (_: Exception) {
            e.message ?: "Network error occurred"
        }
    }
    return e.message ?: "Network error occurred"
}
