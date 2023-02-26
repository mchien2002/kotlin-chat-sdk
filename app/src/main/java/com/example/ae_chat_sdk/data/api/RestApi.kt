package com.example.ae_chat_sdk.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestApi {
    companion object {
        fun getAPI(): Retrofit {
            val logging = HttpLoggingInterceptor()
            logging.level = (HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
            client.addInterceptor(logging)

            return Retrofit.Builder()
                .baseUrl("https://104c-2001-ee0-5004-ec30-797d-bd00-168f-eea7.ap.ngrok.io/api/v1/")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}