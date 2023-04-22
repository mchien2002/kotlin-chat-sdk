package com.example.ae_chat_sdk.acti.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.model.Message
import com.example.ae_chat_sdk.databinding.LayoutFrameMessageBeginBinding
import com.example.ae_chat_sdk.databinding.LayoutFrameMessageReceiverFooterBinding
import com.example.ae_chat_sdk.databinding.LayoutFrameMessageSenderFooterBinding

class MessageAdapter(val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listMessage: ArrayList<Message> = ArrayList()

    enum class TypeView(val typeView: Int) {
        FIRST_MESSAGE(0),
        SEND(1),
        RECEIVE(2)
    }

    inner class SendMessageFooterHolder(private val binding: LayoutFrameMessageSenderFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Message) {
            Log.d("Check Begin", "Send")

            binding.tvSenderFooter.text = data.message
        }
    }

    inner class ReceiveMessageFooterHolder(private val binding: LayoutFrameMessageReceiverFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Message) {
            Log.d("Check Begin", "Receive")

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

    inner class BeginMessageHolder(private val binding: LayoutFrameMessageBeginBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            Log.d("Check Begin", "Begin")
            binding.tvMessageBegin.text = "Bạn đã bắt đầu cuộc trò chuyện."
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TypeView.FIRST_MESSAGE.ordinal) {
            val view = LayoutFrameMessageBeginBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            BeginMessageHolder(view)
        } else if (viewType == TypeView.RECEIVE.ordinal) {
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
        if (getItemViewType(position) == TypeView.FIRST_MESSAGE.ordinal) {
            (holder as BeginMessageHolder).bind()
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
//        if (ms.message == "" && ms.type == Message.Type.FIRST_MESSAGE.ordinal) {
//            return TypeView.FIRST_MESSAGE.ordinal
        } else if (ms.senderUin == RestClient().getUserId()) {
            return TypeView.SEND.ordinal
        }
        return TypeView.RECEIVE.ordinal
    }

    //@SuppressLint("NotifyDataSetChanged")
    fun getMessages(message: ArrayList<Message>) {
        //listMessage.add(message)
        this.listMessage = message
        notifyDataSetChanged()
    }

    fun addMessage(message: Message){
        listMessage.add(message)
        notifyDataSetChanged()
    }

}
