package com.example.ae_chat_sdk.data.model

data class ApiResponse(val status: Boolean,
                       val message: String?,
                       val error: String?,
                       val data: List<User>)
