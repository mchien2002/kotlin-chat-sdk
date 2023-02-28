package com.example.ae_chat_sdk.acti.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.IClickListener

class ContactAdapter(val contact: ArrayList<String>,val iClickListener: IClickListener)  :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.findViewById<TextView>(R.id.tViewUsername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_frame_contact, parent, false)
        return ContactViewHolder(v)
    }

    override fun getItemCount(): Int {
        return contact.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val itemObject = contact[position]
        holder.tvUsername.text = itemObject
        holder.tvUsername.setOnClickListener(View.OnClickListener {
            fun onClick(v:View){
                iClickListener.clickItem(itemObject)
            }
        })
    }

}
