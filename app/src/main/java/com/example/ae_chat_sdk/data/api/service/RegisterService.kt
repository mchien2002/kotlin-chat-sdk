package com.example.ae_chat_sdk.data.api.service

import com.example.ae_chat_sdk.data.api.controller.RegisterController
import retrofit2.Call

public class RegisterService constructor() : RegisterController {

    private val registerService = BaseService.getAPI().create(RegisterController::class.java)

    override fun registerAccount(email: String): Call<String> {
        return registerService.registerAccount(email)
    }

    override fun verifyOTP(email: String, otp: String): Call<String> {
        TODO("Not yet implemented")
    }


}