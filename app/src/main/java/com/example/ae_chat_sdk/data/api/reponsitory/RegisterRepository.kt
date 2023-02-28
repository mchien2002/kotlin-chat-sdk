package com.example.ae_chat_sdk.data.api.reponsitory

import com.example.ae_chat_sdk.data.api.service.RegisterService
import retrofit2.Call

class RegisterRepository constructor(){
    private val registerService = RegisterService()

    public fun registerByMail(email: String): Call<String> {
        return registerService.registerAccount(email)
    }
}