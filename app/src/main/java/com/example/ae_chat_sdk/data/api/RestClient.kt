package com.example.ae_chat_sdk.data.api

import android.content.Context
import com.example.ae_chat_sdk.data.model.User
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class RestClient {
    fun getToken(): String {
        val gson = Gson()
        val type = object : TypeToken<User>() {}.type
        val appStorage = AppStorage.getInstance()
        val userString: String = appStorage.getData("User", "").toString()
        val user = gson.fromJson<User>(userString, type)
        return "Bearer " + user.token
    }
    fun getUserId(): String {
        val gson = Gson()
        val type = object : TypeToken<User>() {}.type
        val appStorage = AppStorage.getInstance()
        val userString: String = appStorage.getData("User", "").toString()
        val user = gson.fromJson<User>(userString, type)
        return user.userId
    }
}