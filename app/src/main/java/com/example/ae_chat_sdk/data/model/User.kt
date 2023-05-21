package com.example.ae_chat_sdk.data.model

import java.util.*

 class User constructor(
    val avatar: String?,
    val createdAt: String,
    val email: String,
    val fullName: String,
    val localName: String,
    val state: Int?,
    val phone: String?,
    val token: String,
    val userId: String,
    val userName: String,


)

enum class UserState(state: Int?) {
   NOT_AUTHORIZED(0), AUTHORIZED(1), PUBLIC(2), PRIAVTE(3)
}
