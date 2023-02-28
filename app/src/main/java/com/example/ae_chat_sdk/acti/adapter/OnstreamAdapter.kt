package com.example.ae_chat_sdk.acti.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.IClickListener

class OnstreamAdapter(val data: ArrayList<String>, val iClickListener: IClickListener) :
    RecyclerView.Adapter<OnstreamAdapter.OnStreamViewHolder>() {

    class OnStreamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.findViewById<TextView>(R.id.tvUsername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnStreamViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_frame_onstream, parent, false)
        return OnStreamViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: OnStreamViewHolder, position: Int) {
        val itemObject = data[position]
        holder.tvUsername.text = itemObject
//        holder.tvUsername.setOnClickListener(View.OnClickListener {
//            fun onClick(v:View){
//                iClickListener.clickItem(itemObject)
//            }
//        })
    }
}