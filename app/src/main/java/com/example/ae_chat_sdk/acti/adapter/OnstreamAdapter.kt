package com.example.ae_chat_sdk.acti.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.boxchat.BoxChatActivity
import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.api.reponsitory.UserRepository
import com.example.ae_chat_sdk.data.model.MyResponse
import com.example.ae_chat_sdk.data.model.User
import com.example.ae_chat_sdk.data.model.UserOnlineStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class OnstreamAdapter(val listOnStream: List<User>, val context: Context) :
    RecyclerView.Adapter<OnstreamAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.findViewById<TextView>(R.id.tvUsername)
        val fLayoutOnstream: FrameLayout = itemView.findViewById<FrameLayout>(R.id.flOnstream)
        val ivAvatar: ImageView = itemView.findViewById<ImageView>(R.id.ivAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_frame_onstream, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listOnStream.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemObject = listOnStream[position]
        holder.tvUsername.text = itemObject.userName
//        var imageUrl =
//            "https://3.bp.blogspot.com/-SMNLs_5XfVo/VHvNUx8dWZI/AAAAAAAAQnY/NWdkO4JPE_M/s1600/Avatar-Facebook-an-danh-trang-4.jpg"
//        if (itemObject.avatar == null) {
//            Glide.with(context)
//                .load(imageUrl)
//                .into(holder.ivAvatar)
//        } else {
//            imageUrl = ApiConstant.URL_AVATAR + itemObject.avatar
//            Glide.with(context)
//                .load(imageUrl)
//                .into(holder.ivAvatar)
//        }
        var imageUrl = ""
        if(itemObject.avatar!=null){
            imageUrl = ApiConstant.URL_IMAGE + itemObject.avatar
            Glide.with(context)
                .load(imageUrl)
                .into(holder.ivAvatar)
        }
        var status: Int? = null
        var statusValue = 2
        var lastTimeOnline: Date? = null
        val call = UserRepository().getUserOnlineStatus(RestClient().getToken(), itemObject.userId)
        call.enqueue(object : Callback<MyResponse> {
            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                Log.e("CCCCC", t.toString())
            }
            override fun onResponse(
                call: Call<MyResponse>, response: Response<MyResponse>
            ) {
                val gson = Gson()
                val typeOnline = object : TypeToken<UserOnlineStatus>() {}.type
                val userTemp =
                    gson.fromJson<UserOnlineStatus>(gson.toJson(response.body()?.data), typeOnline)
                status = userTemp.status
                lastTimeOnline = userTemp.lastTimeOnline
            }
        })
        holder.fLayoutOnstream.setOnClickListener(View.OnClickListener {

            val recipients = listOf(RestClient().getUserId(), itemObject.userId)
            val call = UserRepository().groupProfileByMember(RestClient().getToken(),recipients)
            call.enqueue(object : Callback<MyResponse> {
                override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                    Log.e("CCCCC", t.toString())
                }
                override fun onResponse(
                    call: Call<MyResponse>, response: Response<MyResponse>
                ) {
                   Log.e("RESPONSE",response.body().toString())
                }
            })

            val intent: Intent = Intent(context, BoxChatActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("avatar", imageUrl)
            intent.putExtra("username", itemObject.userName)
            intent.putExtra("status",status)
            val longDate = lastTimeOnline?.time
            intent.putExtra("lastTimeOnline", longDate)
            context.startActivity(intent)
        })
    }
}