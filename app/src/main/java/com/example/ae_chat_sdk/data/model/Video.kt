package com.example.ae_chat_sdk.data.model

import com.google.gson.annotations.SerializedName

class Video {
    @SerializedName("url")
    var url: String? = null

    @SerializedName("width")
    var width: Int? = null

    @SerializedName("height")
    var height: Int? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("duration")
    var duration: Int? = null

    @SerializedName("thumbnailUrl")
    var thumbnailUrl: String? = null
}