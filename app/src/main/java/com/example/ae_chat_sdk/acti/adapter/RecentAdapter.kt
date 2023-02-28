package com.example.ae_chat_sdk.acti.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.IClickListener

class RecentAdapter(val data:ArrayList<String>, val iClickListener: IClickListener) : RecyclerView.Adapter<RecentAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView =itemView.findViewById<TextView>(R.id.tvUsername)
        val tvMessage:TextView=itemView.findViewById(R.id.tvMessage)
        val tvTimeReceive:TextView= itemView.findViewById(R.id.tvTimeReceive)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.layout_frame_recent, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemObject = data[position]
        if(data.size==0){
            return
        }

        holder.tvUsername.text = itemObject
        holder.tvUsername.setOnClickListener(View.OnClickListener {
            fun onClick(v:View){
                iClickListener.clickItem(itemObject)
            }
        })
    }
}