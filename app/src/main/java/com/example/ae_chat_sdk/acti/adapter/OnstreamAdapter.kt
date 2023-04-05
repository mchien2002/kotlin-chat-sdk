package com.example.ae_chat_sdk.acti.adapter

import android.content.Context
import android.content.Intent
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
import com.example.ae_chat_sdk.data.model.User

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
        if (itemObject.avatar == null) {
            val imageUrl =
                "https://3.bp.blogspot.com/-SMNLs_5XfVo/VHvNUx8dWZI/AAAAAAAAQnY/NWdkO4JPE_M/s1600/Avatar-Facebook-an-danh-trang-4.jpg"
            Glide.with(context)
                .load(imageUrl)
                .into(holder.ivAvatar)
        } else {
            val imageUrl = ApiConstant.URL_AVATAR + itemObject.avatar
            Glide.with(context)
                .load(imageUrl)
                .into(holder.ivAvatar)
        }
        holder.fLayoutOnstream.setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent(context, BoxChatActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        })
    }
}