package com.example.ae_chat_sdk.data.model

data class Group(
    val groupId: String,
    val name: String,
    val groupType: Int,
    val theme: Int,
    val groupStatus: Int,
    val members: List<String>
)
data class ListGroupResponse(val listGroup: List<Group>)