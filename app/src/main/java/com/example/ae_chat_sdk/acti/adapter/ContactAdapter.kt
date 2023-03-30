package com.example.ae_chat_sdk.acti.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ae_chat_sdk.R
import de.hdodenhof.circleimageview.CircleImageView

class ContactAdapter(val listContact: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: CircleImageView = itemView.findViewById<CircleImageView>(R.id.ivAvatar)
        val tvUsername: TextView = itemView.findViewById<TextView>(R.id.tvUsername)
        val fLayoutContact:FrameLayout=itemView.findViewById<FrameLayout>(R.id.flContact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_frame_contact, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemObject = listContact[position]
        holder.tvUsername.text = itemObject
        holder.fLayoutContact.setOnClickListener(View.OnClickListener {
            Toast.makeText(context,"Contact", Toast.LENGTH_SHORT).show()
        })
    }

    override fun getItemCount(): Int {
        return listContact.size
    }

}
