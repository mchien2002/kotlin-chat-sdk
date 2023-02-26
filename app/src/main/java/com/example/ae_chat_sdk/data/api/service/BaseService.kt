package com.example.ae_chat_sdk.data.api.service


import com.example.ae_chat_sdk.data.api.ApiConstant.Companion.ACCOUNT_POST_EMAIL
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface BaseService {
    @POST("register_by_email")
    @FormUrlEncoded
    fun registerAccount(@Field("email") params: String): Call<String>
}