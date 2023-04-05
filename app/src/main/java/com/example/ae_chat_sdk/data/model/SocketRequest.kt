package com.example.ae_chat_sdk.data.model

import java.util.Objects

class SocketRequest constructor(
    val event: String,
    val id: String,
    val payload: MutableMap<String, Any>
)