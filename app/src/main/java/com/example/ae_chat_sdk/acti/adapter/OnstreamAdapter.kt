package com.example.ae_chat_sdk.acti.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.popupwindow.PopUpWindowBoxChat
import de.hdodenhof.circleimageview.CircleImageView

class OnstreamAdapter(val listOnStream: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<OnstreamAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.findViewById<TextView>(R.id.tvUsername)
        val fLayoutOnstream:FrameLayout=itemView.findViewById<FrameLayout>(R.id.flOnstream)
        val ivAvatar:CircleImageView=itemView.findViewById<CircleImageView>(R.id.ivAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_frame_onstream, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listOnStream.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemObject = listOnStream[position]
        holder.tvUsername.text = itemObject
        holder.fLayoutOnstream.setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent(context, PopUpWindowBoxChat::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        })
    }
}