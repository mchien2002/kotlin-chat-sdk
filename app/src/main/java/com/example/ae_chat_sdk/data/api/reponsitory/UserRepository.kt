package com.example.ae_chat_sdk.data.api.reponsitory

import com.example.ae_chat_sdk.data.api.service.UserService
import com.example.ae_chat_sdk.data.model.ApiResponse
import com.example.ae_chat_sdk.data.model.MyResponse
import com.example.ae_chat_sdk.data.model.User
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

    public fun getUserProfile(token: String, id: String): Call<MyResponse> {
        return userService.getUserProfile(token, id)
    }

    public fun getUserOnlineStatus(token: String, userId: String): Call<MyResponse> {
        return userService.getUserOnlineStatus(token, userId)
    }

    public fun groupProfileByMember(token: String, listUserId: List<String>): Call<MyResponse> {
        return userService.groupProfileByMember(token, listUserId)
    }

    public fun updateUser(token: String, user: User): Call<MyResponse> {
        return userService.updateUser(token, user)
    }

    public fun updateUserState(token: String,userId: String,state : Int) : Call<MyResponse>{
        return userService.updateUserState(token, userId, state)
    }
}