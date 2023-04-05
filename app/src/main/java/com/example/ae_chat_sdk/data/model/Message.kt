package com.example.ae_chat_sdk.data.model

import java.util.*

class Message {
    enum class Type{
        TEXT,
        CALL,
        IMAGE,
        AUDIO,
        VIDEO,
        FIRST_MESSAGE,
        GROUP_UPDATE,
        LEAVE_GROUP,
        REPLY,
        FORWARD,
        STICKER,
        FILE,
        SCREEN_SHOT,
        LOCATION,
        LIVE_LOCATION,
        GROUP_CALL

    }
    enum class Status{
        UNKNOWN,
        SENT,
        RECEIVED,
        SEEN,
        DELETED,
        SENDING
    }
    enum class GroupType{
        UNKOWN,
        PRIVATE,
        GROUP,
        PUBLIC,
        CHANNEL,
        OFFICICAL
    }

    lateinit var id: String
    lateinit var type: Type
    lateinit var status: Status
    lateinit var groupType: GroupType
    lateinit var groupId: String
    lateinit var message: String
    lateinit var createdAt: Date
    lateinit var updateAt: Date
    lateinit var senderName: String
    lateinit var senderUin: String
    lateinit var senderAvatar: String
    lateinit var nonce: String
    lateinit var seenUins: List<String>
    lateinit var deletedUins: List<String>
    lateinit var attachments: Any


}