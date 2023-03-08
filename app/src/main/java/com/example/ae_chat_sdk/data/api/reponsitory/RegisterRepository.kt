package com.example.ae_chat_sdk.data.api.reponsitory

import com.example.ae_chat_sdk.data.api.service.RegisterService
import com.example.ae_chat_sdk.data.model.MyResponse
import com.example.ae_chat_sdk.data.model.User
import retrofit2.Call

class RegisterRepository constructor(){
    private val registerService = RegisterService()

    public fun registerByMail(email: String): Call<MyResponse> {
        return registerService.registerAccount(email)
    }
    public fun verifyOTP(email: String,otp : String): Call<MyResponse> {
        return registerService.verifyOTP(email, otp)
    }
}