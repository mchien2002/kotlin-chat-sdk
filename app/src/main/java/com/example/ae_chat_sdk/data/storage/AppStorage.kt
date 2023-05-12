package com.example.ae_chat_sdk.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.ae_chat_sdk.data.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppStorage private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("myPref", Context.MODE_PRIVATE)

    companion object {
        private var instance: AppStorage? = null

        fun getInstance(context: Context): AppStorage {
            if (instance == null) {
                instance = AppStorage(context)
            }
            return instance as AppStorage
        }

        fun getInstance(): AppStorage {
            return instance as AppStorage
        }
    }

    fun saveData(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getData(key: String, defaultValue: String): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }

    fun getUserLocal(): User {
        val gson = Gson()
        val type = object : TypeToken<User>() {}.type
        val appStorage = AppStorage.getInstance()
        val userString: String = appStorage.getData("User", "").toString()
        val user = gson.fromJson<User>(userString, type)
        return user
    }

    fun getSWStatus(): Boolean {
        val appStorage = AppStorage.getInstance()
        val state: Boolean = appStorage.getData("swState", "false").toBoolean()
        return state
    }
}
