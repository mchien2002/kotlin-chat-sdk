package com.example.ae_chat_sdk.data.model

import com.google.gson.annotations.SerializedName

class Group{
    @SerializedName("groupId")
    var groupId: String = ""

    @SerializedName("name")
    var name: String = ""

    @SerializedName("groupType")
    var groupType: Int = GroupType.UNKOWN.ordinal

    @SerializedName("theme")
    var theme: Int = 0

    @SerializedName("groupStatus")
    var groupStatus: Int = 0

    @SerializedName("members")
    var members: List<String> = emptyList()

    @SerializedName("lastMessage")
    var lastMessage: Message = Message()
}

data class ListGroupResponse(val listGroup: List<Group>)