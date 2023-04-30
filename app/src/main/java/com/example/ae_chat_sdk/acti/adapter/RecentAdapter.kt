package com.example.ae_chat_sdk.acti.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.boxchat.BoxChatActivity
import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.api.reponsitory.UserRepository
import com.example.ae_chat_sdk.data.model.* // ktlint-disable no-wildcard-imports
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.example.ae_chat_sdk.utils.DateTimeUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class RecentAdapter(val context: Context) : RecyclerView.Adapter<RecentAdapter.ViewHolder>() {

    var listRecent: ArrayList<Group> = ArrayList()

    enum class TypeView(val typeView: Int) {
        UNKOWN(0), PRIVATE(1), GROUP(2), PUBLIC(3), CHANNEL(4), OFFICICAL(5)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.findViewById<TextView>(R.id.tvUsername)
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        val tvTimeReceive: TextView = itemView.findViewById(R.id.tvTimeReceive)
        val flRecent: FrameLayout = itemView.findViewById(R.id.flRecent)
        val ivAvatarRecent: ImageView = itemView.findViewById(R.id.ivAvatarRecent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_frame_recent, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listRecent.size
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (listRecent.isEmpty()) {
            Log.e("CCCCC", "UUUUUUUUUUUUUUUUU")
        }
        val itemObject = listRecent[position]

        if (itemObject.lastMessage.type == Message.Type.FIRST_MESSAGE.ordinal) {
            holder.tvMessage.text = "Hãy bắt đầu cuộc trò chuyện"
        } else if (itemObject.lastMessage.type == Message.Type.TEXT.ordinal) {
            val senderUin = itemObject.lastMessage.senderUin
            if (itemObject.lastMessage != null && holder.tvMessage != null) {
                if (senderUin == RestClient().getUserId()) {
                    holder.tvMessage.text = "Bạn: " + itemObject.lastMessage.message
                } else {
                    holder.tvMessage.text = itemObject.lastMessage.message
                }
            }
        }
        var seenUins: ArrayList<String>? = itemObject.lastMessage.seenUins
        Log.e("SEENNN",seenUins.toString())
        //val seenUins = itemObject.lastMessage.seenUins
        if (seenUins!= null){
            for (item in seenUins)
            {
                Log.e("ITEMMM",item)
                holder.tvMessage.setTypeface(null, Typeface.BOLD)
                holder.tvUsername.setTypeface(null, Typeface.BOLD)
                if (item == RestClient().getUserId()){
                    Log.e("USERIDCL",RestClient().getUserId())
                    holder.tvMessage.setTypeface(null, Typeface.NORMAL)
                    holder.tvUsername.setTypeface(null, Typeface.NORMAL)
                    break
                }
            }
        }



        val timeString = DateTimeUtil().getTimeFromDate(itemObject.lastMessage.createdAt)
        holder.tvTimeReceive.text = timeString

        val gson = Gson()
        val type = object : TypeToken<User>() {}.type
        val myUser: User = AppStorage.getInstance(context).getUserLocal()
        var username = ""
        var imageUrl =
            "https://3.bp.blogspot.com/-SMNLs_5XfVo/VHvNUx8dWZI/AAAAAAAAQnY/NWdkO4JPE_M/s1600/Avatar-Facebook-an-danh-trang-4.jpg"
        if (itemObject.groupType == TypeView.PUBLIC.typeView) {
            for (item in itemObject.members) {
                if (item == myUser.userId) {
                    continue
                } else {
                    val call = UserRepository().getUserProfile(RestClient().getToken(), item)
                    call.enqueue(object : Callback<MyResponse> {
                        override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                            Log.e("CCCCC", t.toString())
                        }
                        override fun onResponse(
                            call: Call<MyResponse>,
                            response: Response<MyResponse>
                        ) {
                            val userTemp =
                                gson.fromJson<User>(gson.toJson(response.body()?.data), type)
//                             Log.e("CCCCC", userTemp.tp)
                            holder.tvUsername.text = userTemp.fullName
                            username = userTemp.userName
                            if (userTemp.avatar != null) {
                                imageUrl = ApiConstant.URL_AVATAR + userTemp.avatar
                            }
                            Glide.with(context).load(imageUrl).into(holder.ivAvatarRecent)
                        }
                    })
                    break
                }
            }
        }
        // holder.tvUsername.text = itemObject.groupId
        holder.flRecent.setOnClickListener(
            View.OnClickListener {
                val intent: Intent = Intent(context, BoxChatActivity::class.java)
                intent.putExtra("GroupId", itemObject.groupId)
                intent.putExtra("avatar", imageUrl)
                intent.putExtra("username", username)
                val senderUin = itemObject.lastMessage.senderUin
                    if (senderUin != RestClient().getUserId()) {
                        if (itemObject.lastMessage.status != Message.Status.SEEN.ordinal){
                            intent.putExtra("lastmessage",itemObject.lastMessage.messageId)
                        }
                    }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        listRecent.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getListgroup(group: ArrayList<Group>) {
        this.listRecent = group
        notifyDataSetChanged()
    }
}
