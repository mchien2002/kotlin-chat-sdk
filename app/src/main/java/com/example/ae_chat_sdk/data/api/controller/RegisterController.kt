package com.example.ae_chat_sdk.data.api.controller

import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.model.MyResponse
import com.example.ae_chat_sdk.data.model.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RegisterController {
    @POST(ApiConstant.ACCOUNT_POST_EMAIL)
    @FormUrlEncoded
    fun registerAccount(@Field("email") email: String): Call<MyResponse>

    @POST(ApiConstant.VERIFY_OTP)
    @FormUrlEncoded
    fun verifyOTP(@Field("email") email: String, @Field("otp") otp: String): Call<MyResponse>


}