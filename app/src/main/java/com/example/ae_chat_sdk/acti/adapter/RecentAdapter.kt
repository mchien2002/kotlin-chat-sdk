package com.example.ae_chat_sdk.acti.adapter

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.popupwindow.PopUpWindowBoxChat

class RecentAdapter(val listRecent: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<RecentAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.findViewById<TextView>(R.id.tvUsername)
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        val tvTimeReceive: TextView = itemView.findViewById(R.id.tvTimeReceive)
        val flRecent: FrameLayout = itemView.findViewById(R.id.flRecent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_frame_recent, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listRecent.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemObject = listRecent[position]
        holder.tvUsername.text = itemObject
        holder.flRecent.setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent(context, PopUpWindowBoxChat::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        })
    }
}