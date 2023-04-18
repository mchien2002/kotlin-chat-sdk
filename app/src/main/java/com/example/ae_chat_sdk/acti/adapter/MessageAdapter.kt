package com.example.ae_chat_sdk.acti.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.model.Message
import com.example.ae_chat_sdk.databinding.LayoutFrameMessageReceiverFooterBinding
import com.example.ae_chat_sdk.databinding.LayoutFrameMessageSenderFooterBinding

class MessageAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listMessage:ArrayList<Message> =ArrayList()

    enum class TypeView(val typeView: Int) {
        FIRST_MESSAGE(0),
        SEND(1),
        RECEIVE(2)
    }

    inner class SendMessageFooterHolder(private val binding: LayoutFrameMessageSenderFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Message) {
            binding.tvSenderFooter.text = data.message
        }
    }

    inner class ReceiveMessageFooterHolder(private val binding: LayoutFrameMessageReceiverFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Message) {
            binding.tvReceiverFooter.text = data.message
            try {
                Glide.with(context)
                    .load(data.senderAvatar)
                    .into(binding.ivAvatar)
            } catch (ex: Exception) {
                Log.d("GlideError", ex.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TypeView.FIRST_MESSAGE.typeView || viewType == TypeView.RECEIVE.typeView) {
            val view = LayoutFrameMessageReceiverFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ReceiveMessageFooterHolder(view)
        } else {
            val view = LayoutFrameMessageSenderFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            SendMessageFooterHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return listMessage.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TypeView.FIRST_MESSAGE.typeView) {
            listMessage[position].message="Bạn đã bắt đầu cuộc trò chuyện."
            (holder as ReceiveMessageFooterHolder).bind(listMessage[position])
        } else if (getItemViewType(position) == TypeView.RECEIVE.typeView) {
            (holder as ReceiveMessageFooterHolder).bind(listMessage[position])

        } else {
            (holder as SendMessageFooterHolder).bind(listMessage[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        val ms: Message = listMessage[position]
        ms.message?.let { Log.d("message", it) }
        Log.d("type", ms.type.toString())
        if (ms.message == "" && ms.type?.toFloat()!!.toInt() == Message.Type.FIRST_MESSAGE.type) {
            return TypeView.FIRST_MESSAGE.typeView
        } else if (ms.senderUin == RestClient().getUserId()) {
            return TypeView.SEND.typeView
        }
        return TypeView.RECEIVE.typeView
    }

    fun addItem(message: Message){
        listMessage.add(message)
        notifyDataSetChanged()
    }
}