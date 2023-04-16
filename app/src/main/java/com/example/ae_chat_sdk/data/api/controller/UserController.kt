package com.example.ae_chat_sdk.data.api.controller

import com.example.ae_chat_sdk.data.model.ApiResponse
import com.example.ae_chat_sdk.data.model.MyResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UserController {
    @Multipart
    @POST("upload_user_avatar")
    fun uploadAvatarUser(@Header("token") token: String, @Query("userId") userId: String, @Part imgFile : MultipartBody.Part ) : Call<MyResponse>

    @GET("search_user")
    fun searchUser(@Header("token") token: String,@Query("key") key : String) : Call<ApiResponse>

    @GET("user_profile")
    fun getUserProfile(@Header("token") token:String, @Query("id") id:String) : Call<MyResponse>
}