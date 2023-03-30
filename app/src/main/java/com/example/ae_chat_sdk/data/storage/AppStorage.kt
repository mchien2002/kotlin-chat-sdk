package com.example.ae_chat_sdk.data.storage

import android.content.Context
import android.content.SharedPreferences

class AppStorage private constructor(context: Context)
{
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE)

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
}
