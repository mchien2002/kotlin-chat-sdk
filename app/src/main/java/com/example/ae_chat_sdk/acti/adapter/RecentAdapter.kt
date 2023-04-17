package com.example.ae_chat_sdk.acti.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.MainActivity
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.boxchat.BoxChatActivity
import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.api.reponsitory.RegisterRepository
import com.example.ae_chat_sdk.data.api.reponsitory.UserRepository
import com.example.ae_chat_sdk.data.model.ApiResponse
import com.example.ae_chat_sdk.data.model.Group
import com.example.ae_chat_sdk.data.model.MyResponse
import com.example.ae_chat_sdk.data.model.User
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemObject = listRecent[position]
        holder.tvMessage.text = itemObject.lastMessage.message


        val createdAtString = itemObject.lastMessage.createdAt.toString()
        val dateFormat = SimpleDateFormat("MMM d, yyyy, h:mm:ss a", Locale.ENGLISH)
        val createdAtDate = dateFormat.parse(createdAtString)

        val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val formattedDateString = outputDateFormat.format(createdAtDate)
        Log.e("TIMERRR",formattedDateString.toString())

        val currentDate = LocalDate.now()
        val dateFormat1 = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val formattedDate = currentDate.format(dateFormat1)
        Log.e("TIMETTT",formattedDate.toString())

        val date2 = LocalDate.of(2023, 3, 3).toString()
        val formattedDate2 = date2.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        Log.e("TIMEEE",formattedDate2.toString() )

        if (formattedDateString.equals(formattedDate2)){
            val inputDate = itemObject.lastMessage.createdAt.toString()
            val inputFormat = SimpleDateFormat("MMM d, yyyy, h:mm:ss a", Locale.US)
            val date = inputFormat.parse(inputDate)
            val outputFormat = SimpleDateFormat("h:mm:ss a", Locale.US)
            val time = outputFormat.format(date)
            holder.tvTimeReceive.text = time
        }else{
            holder.tvTimeReceive.text = formattedDateString
        }




        val gson = Gson()
        val type = object : TypeToken<User>() {}.type
        val appStorage = AppStorage.getInstance(context)
        val userString: String = appStorage.getData("User", "").toString()
        val user = gson.fromJson<User>(userString, type)
        var username = ""
        var imageUrl =
            "https://3.bp.blogspot.com/-SMNLs_5XfVo/VHvNUx8dWZI/AAAAAAAAQnY/NWdkO4JPE_M/s1600/Avatar-Facebook-an-danh-trang-4.jpg"
        if (itemObject.groupType == TypeView.PUBLIC.typeView) {
            for (item in itemObject.members) {
                if (item == user.userId) {
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
                            Log.e("CCCCC", userTemp.fullName)
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
        //holder.tvUsername.text = itemObject.groupId
        holder.flRecent.setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent(context, BoxChatActivity::class.java)
            intent.putExtra("GroupId",itemObject.groupId)
            intent.putExtra("avatar",imageUrl)
            intent.putExtra("username",username)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        })
    }

    fun clearItems() {
        listRecent.clear()
        notifyDataSetChanged()
    }

    fun addItem(group: Group) {
        listRecent.add(group)
        Log.e("SIZEGRZ", group.members.toString())
        notifyDataSetChanged()
    }

}