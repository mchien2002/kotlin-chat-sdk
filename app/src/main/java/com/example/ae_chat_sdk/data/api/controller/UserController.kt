package com.example.ae_chat_sdk.data.api.controller

import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.model.ApiResponse
import com.example.ae_chat_sdk.data.model.MyResponse
import com.example.ae_chat_sdk.data.model.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UserController {
    @Multipart
    @POST(ApiConstant.UPLOAD_USER_AVATAR)
    fun uploadAvatarUser(@Header("token") token: String, @Query("userId") userId: String, @Part imgFile : MultipartBody.Part ) : Call<MyResponse>

    @GET(ApiConstant.SEARCH_USER)
    fun searchUser(@Header("token") token: String,@Query("key") key : String) : Call<ApiResponse>

    @GET(ApiConstant.USER_PROFILE)
    fun getUserProfile(@Header("token") token:String, @Query("id") id:String) : Call<MyResponse>

    @GET(ApiConstant.USER_ONLINE_STATUS)
    fun getUserOnlineStatus(@Header("token") token: String,@Query("userId") userId: String) : Call<MyResponse>

    @POST(ApiConstant.GROUP_PROFILE_BY_MEMBER)
    fun groupProfileByMember(@Header("token") token: String,@Body listUserId : List<String>):Call<MyResponse>

    @POST(ApiConstant.UPDATE_USER)
    fun updateUser(@Header("token") token:String, @Body user: User):Call<MyResponse>

    @POST(ApiConstant.UPDATE_USER_STATE)
    fun updateUserState(@Header("token") token:String, @Query("key") userId : String,@Query("state") state:Int):Call<MyResponse>
}