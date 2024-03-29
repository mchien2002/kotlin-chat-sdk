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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.boxchat.BoxChatActivity
import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.api.reponsitory.UserRepository
import com.example.ae_chat_sdk.data.model.*
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.example.ae_chat_sdk.utils.DateTimeUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RecentAdapter(val progressBar: ProgressBar, val context: Context) : RecyclerView.Adapter<RecentAdapter.ViewHolder>() {

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
        val ivOnline : ImageView = itemView.findViewById(R.id.ivOnline)
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

        val senderUin = itemObject!!.lastMessage!!.senderUin
        if (itemObject.lastMessage!!.type == Message.Type.FIRST_MESSAGE.ordinal) {
            holder.tvMessage.text = "Hãy bắt đầu cuộc trò chuyện"
        } else if (itemObject.lastMessage!!.type == Message.Type.TEXT.ordinal) {
            if (itemObject.lastMessage != null && holder.tvMessage != null) {
                if (senderUin == RestClient().getUserId()) {
                    holder.tvMessage.text = "Bạn: " + itemObject.lastMessage!!.message
                } else {
                    holder.tvMessage.text = itemObject.lastMessage!!.message
                }
            }
        } else if (itemObject.lastMessage!!.type == Message.Type.IMAGE.ordinal) {
            if (itemObject.lastMessage != null && holder.tvMessage != null) {
                if (senderUin == RestClient().getUserId()) {
                    holder.tvMessage.text = "Bạn đã gửi 1 ảnh."
                } else {
                    holder.tvMessage.text = "Bạn nhận được 1 ảnh."
                }
            }
        }else if (itemObject.lastMessage!!.type == Message.Type.AUDIO.ordinal){
            if (itemObject.lastMessage != null && holder.tvMessage != null) {
                if (senderUin == RestClient().getUserId()) {
                    holder.tvMessage.text = "Bạn đã gửi 1 tin nhắn thoại."
                } else {
                    holder.tvMessage.text = "Bạn đã nhận được 1 tin nhắn thoại."
                }
            }
        }else if(itemObject.lastMessage!!.type == Message.Type.VIDEO.ordinal){
            if (itemObject.lastMessage != null && holder.tvMessage != null) {
                if (senderUin == RestClient().getUserId()) {
                    holder.tvMessage.text = "Bạn đã gửi 1 video."
                } else {
                    holder.tvMessage.text = "Bạn đã nhận được 1 video."
                }
            }
        }
            var seenUins: ArrayList<String>? = itemObject.lastMessage!!.seenUins
            Log.e("SEENNN", seenUins.toString())
            //val seenUins = itemObject.lastMessage.seenUins
            if (seenUins != null) {
                for (item in seenUins) {
                    Log.e("ITEMMM", item)
                    holder.tvMessage.setTypeface(null, Typeface.BOLD)
                    holder.tvUsername.setTypeface(null, Typeface.BOLD)
                    if (item == RestClient().getUserId()) {
                        Log.e("USERIDCL", RestClient().getUserId())
                        holder.tvMessage.setTypeface(null, Typeface.NORMAL)
                        holder.tvUsername.setTypeface(null, Typeface.NORMAL)
                        break
                    }
                }
            }

            val timeString = DateTimeUtil().getTimeFromDate(itemObject.lastMessage!!.createdAt)
            holder.tvTimeReceive.text = timeString

            val gson = Gson()
            val type = object : TypeToken<User>() {}.type
            val myUser: User = AppStorage.getInstance(context).getUserLocal()
            var username = ""
            var userId = ""
            var status: Int? = null
            var statusValue = 2
            var lastTimeOnline: Date? = null
            var imageUrl = ""
            if (itemObject.groupType == TypeView.PUBLIC.typeView) {
                for (item in itemObject.members!!) {
                    if (item == myUser.userId) {
                        continue
                    } else {
                        val call = UserRepository().getUserProfile(RestClient().getToken(), item)
                        call.enqueue(object : Callback<MyResponse> {
                            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                                Log.e("CCCCC", t.toString())
                            }

                            override fun onResponse(
                                call: Call<MyResponse>, response: Response<MyResponse>
                            ) {
                                val userTemp =
                                    gson.fromJson<User>(gson.toJson(response.body()?.data), type)
                                if(userTemp != null){
                                    holder.tvUsername.text = userTemp.fullName
                                    username = userTemp.userName
                                    userId = userTemp.userId
                                    if (userTemp.avatar != null) {
                                        imageUrl = ApiConstant.URL_IMAGE + userTemp.avatar
                                        Glide.with(context).load(imageUrl)
                                            .placeholder(R.drawable.avatardefault)
                                            .error(R.drawable.avatardefault).into(holder.ivAvatarRecent)
                                    }
                                }
                            }
                        })
                        break
                    }
                }
                for (item in itemObject.members!!) {
                    if (item == myUser.userId) {
                        continue
                    } else {
                        val call =
                            UserRepository().getUserOnlineStatus(RestClient().getToken(), item)
                        call.enqueue(object : Callback<MyResponse> {
                            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                                Log.e("CCCCC", t.toString())
                            }

                            override fun onResponse(
                                call: Call<MyResponse>, response: Response<MyResponse>
                            ) {
                                val typeOnline = object : TypeToken<UserOnlineStatus>() {}.type
                                val userTemp =
                                    gson.fromJson<UserOnlineStatus>(
                                        gson.toJson(response.body()?.data),
                                        typeOnline
                                    )
                                if (userTemp!=null){
                                    status = userTemp.status
                                    lastTimeOnline = userTemp.lastTimeOnline
                                    if (userTemp.status == UserOnlineStatus.UserStatus.ONLINE.ordinal) {
                                        holder.ivOnline.visibility = View.VISIBLE
                                    } else {
                                        holder.ivOnline.visibility = View.GONE
                                    }
                                }

                            }
                        })
                        break
                    }
                }
            }
            // holder.tvUsername.text = itemObject.groupId
            holder.flRecent.setOnClickListener(View.OnClickListener {
                val intent: Intent = Intent(context, BoxChatActivity::class.java)
                intent.putExtra("GroupId", itemObject.groupId)
                intent.putExtra("avatar", imageUrl)
                intent.putExtra("username", username)
                //intent.putExtra("userId", userId)
                intent.putExtra("status", status)
                val longDate = lastTimeOnline?.time
                intent.putExtra("lastTimeOnline", longDate)
                val senderUin = itemObject.lastMessage!!.senderUin
                if (senderUin != RestClient().getUserId()) {
                    if (itemObject.lastMessage!!.status != Message.Status.SEEN.ordinal) {
                        intent.putExtra("lastmessage", itemObject.lastMessage!!.messageId)
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
        progressBar.visibility=View.VISIBLE
        this.listRecent = group
        progressBar.visibility=View.GONE
        notifyDataSetChanged()
    }
}
