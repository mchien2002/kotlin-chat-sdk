package com.example.ae_chat_sdk.data.model

data class MyResponse constructor(
    val `data`: Any,
    val error: Any,
    val message: String,
    val status: Boolean
)

