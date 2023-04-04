package com.example.ae_chat_sdk.data.api.reponsitory

import com.example.ae_chat_sdk.data.api.service.RegisterService
import com.example.ae_chat_sdk.data.api.service.UserService
import com.example.ae_chat_sdk.data.model.ApiResponse
import com.example.ae_chat_sdk.data.model.MyResponse
import okhttp3.MultipartBody
import retrofit2.Call

class UserRepository constructor(){
    private val userService = UserService()

    public fun uploadAvatarUser(token: String,
                                userId: String,
                                imgFile: MultipartBody.Part): Call<MyResponse> {
        return userService.uploadAvatarUser(token, userId, imgFile)
    }
    public fun searchUser(token: String,key : String) : Call<ApiResponse>
    {
        return userService.searchUser(token, key)
    }
}