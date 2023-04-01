package com.example.ae_chat_sdk.data.api.service

import com.example.ae_chat_sdk.data.api.controller.RegisterController
import com.example.ae_chat_sdk.data.api.controller.UserController
import com.example.ae_chat_sdk.data.model.MyResponse
import okhttp3.MultipartBody
import retrofit2.Call

public class UserService constructor() : UserController {
    private val userService = BaseService.getAPI().create(UserController::class.java)

    override fun uploadAvatarUser(
        token: String,
        userId: String,
        imgFile: MultipartBody.Part
    ): Call<MyResponse> {
        return userService.uploadAvatarUser(token, userId, imgFile)
    }
}