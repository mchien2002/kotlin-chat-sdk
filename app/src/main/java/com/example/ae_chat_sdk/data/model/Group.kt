package com.example.ae_chat_sdk.data.model

import com.google.gson.annotations.SerializedName

class Group{
    @SerializedName("groupId")
    var groupId: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("groupType")
    var groupType: Int? = null

    @SerializedName("theme")
    var theme: Int? = null

    @SerializedName("creatorUin")
    var creatorUin :String? = null

    @SerializedName("ownerUin")
    var ownerUin : String?= null

    @SerializedName("groupStatus")
    var groupStatus: Int? = null

    @SerializedName("members")
    var members: List<String>? = null

    @SerializedName("lastMessage")
    var lastMessage: Message? = null
}

data class ListGroupResponse(val listGroup: List<Group>)