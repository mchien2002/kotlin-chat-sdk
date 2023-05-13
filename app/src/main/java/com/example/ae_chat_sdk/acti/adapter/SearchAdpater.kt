package com.example.ae_chat_sdk.acti.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.boxchat.BoxChatActivity
import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.api.reponsitory.UserRepository
import com.example.ae_chat_sdk.data.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SearchAdpater(val listContact: List<User>, val context: Context) :
    RecyclerView.Adapter<SearchAdpater.ViewHolder>() {
    lateinit var lastMessage : Message

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: CircleImageView = itemView.findViewById<CircleImageView>(R.id.ivAvatar)
        val tvUsername: TextView = itemView.findViewById<TextView>(R.id.tvUsername)
        val fLayoutContact: FrameLayout = itemView.findViewById<FrameLayout>(R.id.flContact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_frame_search, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var group: Group? = null
        var check : Boolean = false
        var status: Int? = null
        var lastTimeOnline: Date? = null

        val itemObject = listContact[position]
        holder.tvUsername.text = itemObject.userName
        val imageUrl = ApiConstant.URL_IMAGE + itemObject.avatar
        Glide.with(context).load(imageUrl).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.avatardefault)
            .error(R.drawable.avatardefault).into(holder.ivAvatar)

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

        holder.fLayoutContact.setOnClickListener(View.OnClickListener {
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
                    val responseData = response.body()?.data
                    if (responseData != null){
                        val gson = Gson()
                        val type = object : TypeToken<Group>() {}.type
                        group = gson.fromJson<Group>(gson.toJson(response.body()?.data), type)
                    }
                    if (group!=null){
                        lastMessage = group!!.lastMessage!!
                        check = true
                        val intent: Intent = Intent(context, BoxChatActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.putExtra("avatar", imageUrl)
                        intent.putExtra("username", itemObject.userName)
                        Log.e("CHECK",check.toString())
                        if (check == true){
                            intent.putExtra("GroupId", group!!.groupId)
                            val senderUin = lastMessage.senderUin
                            if (senderUin != RestClient().getUserId()) {
                                if (lastMessage.status != Message.Status.SEEN.ordinal) {
                                    intent.putExtra("lastmessage", lastMessage.messageId)
                                }
                            }
                        }
                        intent.putExtra("status",status)
                        val longDate = lastTimeOnline?.time
                        intent.putExtra("lastTimeOnline", longDate)
                        context.startActivity(intent)
                    }else{
                        val intent: Intent = Intent(context, BoxChatActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.putExtra("userId", itemObject.userId)
                        intent.putExtra("avatar", imageUrl)
                        intent.putExtra("username", itemObject.userName)
                        intent.putExtra("status",status)
                        val longDate = lastTimeOnline?.time
                        intent.putExtra("lastTimeOnline", longDate)
                        context.startActivity(intent)
                    }
                }
            })
        })
    }

    override fun getItemCount(): Int {
        return listContact.size
    }

}
