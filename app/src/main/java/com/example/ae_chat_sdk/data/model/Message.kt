package com.example.ae_chat_sdk.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

class Message {
    enum class Type(val type: Int) {
        TEXT(0),
        CALL(1),
        IMAGE(2),
        AUDIO(3),
        VIDEO(4),
        FIRST_MESSAGE(5),
        GROUP_UPDATE(6),
        LEAVE_GROUP(7),
        REPLY(8),
        FORWARD(9),
        STICKER(10),
        FILE(11),
        SCREEN_SHOT(12),
        LOCATION(13),
        LIVE_LOCATION(14),
        GROUP_CALL(15)

    }

    enum class Status(val status: Int) {
        UNKNOWN(0),
        SENT(1),
        RECEIVED(2),
        SEEN(3),
        DELETED(4),
        SENDING(5)
    }

    enum class GroupType(val groupType: Int) {
        UNKOWN(0),
        PRIVATE(1),
        GROUP(2),
        PUBLIC(3),
        CHANNEL(4),
        OFFICICAL(5)
    }

    @SerializedName("id")
    var id: String = ""

    @SerializedName("type")
    var type: String = Type.TEXT.type.toString()

    @SerializedName("status")
    var status: String = Status.UNKNOWN.status.toString()

    @SerializedName("groupType")
    var groupType: String = GroupType.UNKOWN.groupType.toString()

    @SerializedName("groupId")
    var groupId: String = ""

    @SerializedName("message")
    var message: String = ""

    @SerializedName("createAt")
    var createdAt: String = ""

    @SerializedName("updateAt")
    var updateAt: String = ""

    @SerializedName("senderName")
    var senderName: String = ""

    @SerializedName("senderUin")
    var senderUin: String = ""

    @SerializedName("senderAvatar")
    var senderAvatar: String = ""

    @SerializedName("nonce")
    var nonce: String = ""

    @SerializedName("seenUins")
    var seenUins: ArrayList<String> = ArrayList()

    @SerializedName("deleteUins")
    var deletedUins: ArrayList<String> = ArrayList()

    @SerializedName("attachments")
    var attachments: Any = ""


}