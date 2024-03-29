package com.example.ae_chat_sdk.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

class UserOnlineStatus{
    enum class UserStatus(val status : Int){
        OFFLINE(0),
        ONLINE(1)
    }

    @SerializedName("userId")
    var userId: String? = null

    @SerializedName("status")
    var status: Int? = null

    @SerializedName("lastTimeOnline")
    var lastTimeOnline: Date? = null
}
