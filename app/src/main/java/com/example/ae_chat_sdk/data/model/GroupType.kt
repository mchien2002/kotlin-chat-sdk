package com.example.ae_chat_sdk.data.model

enum class GroupType(val groupType: Int) {
    UNKOWN(0),
    PRIVATE(1),
    GROUP(2),
    PUBLIC(3),
    CHANNEL(4),
    OFFICICAL(5)
}