package com.example.ae_chat_sdk.data.model

import com.google.gson.annotations.SerializedName

class Image {
    @SerializedName("url")
    var url: String? = null

    @SerializedName("subIndex")
    var subIndex: Int? = null

    @SerializedName("width")
    var width: Int? = null

    @SerializedName("height")
    var height: Int? = null
}