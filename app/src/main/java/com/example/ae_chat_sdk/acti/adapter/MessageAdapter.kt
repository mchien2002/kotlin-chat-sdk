package com.example.ae_chat_sdk.acti.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.model.Group
import com.example.ae_chat_sdk.data.model.Message
import com.example.ae_chat_sdk.databinding.LayoutFrameMessageBeginBinding
import com.example.ae_chat_sdk.databinding.LayoutFrameMessageReceiverFooterBinding
import com.example.ae_chat_sdk.databinding.LayoutFrameMessageSenderFooterBinding

class MessageAdapter(val context: Context,val groupId : String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listMessage: ArrayList<Message> = ArrayList()
    var imageUrl =
        "https://3.bp.blogspot.com/-SMNLs_5XfVo/VHvNUx8dWZI/AAAAAAAAQnY/NWdkO4JPE_M/s1600/Avatar-Facebook-an-danh-trang-4.jpg"

    enum class TypeView(val typeView: Int) {
        FIRST_MESSAGE(0),
        SEND(1),
        RECEIVE(2)
    }

    inner class SendMessageFooterHolder(val binding: LayoutFrameMessageSenderFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Message) {
            Log.d("Check Begin", "Send")

            binding.tvSenderFooter.text = data.message
            binding.ivCheckSeen.setImageDrawable(null)
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
        Log.e("position",position.toString())
        Log.e("listMessagesize",listMessage.size.toString())

        val senderUin = listMessage[position].senderUin
        if (senderUin == RestClient().getUserId()) {
            if (position == listMessage.size-1 && listMessage[position].status == Message.Status.SEEN.ordinal){
                Log.e("LASTMS",listMessage[position].message.toString())
                if(holder is SendMessageFooterHolder){
                    Glide.with(context).load(imageUrl).into(holder.binding.ivCheckSeen)
                    //holder.binding.ivCheckSeen.setImageDrawable(null)
                }
            } else if (listMessage[position].status == Message.Status.SENT.ordinal) {
                if (holder is SendMessageFooterHolder) {
                    holder.binding.ivCheckSeen.setImageDrawable(
                        ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.ic_check_seen
                        )
                    )
                }
            }else if (listMessage[position].status == Message.Status.SEEN.ordinal && listMessage[position+1].status != Message.Status.SEEN.ordinal){
                if(holder is SendMessageFooterHolder){
                    Glide.with(context).load(imageUrl).into(holder.binding.ivCheckSeen)
                }
            }
        } else {
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
        if(message.groupId == this.groupId){
            listMessage.add(message)
            notifyDataSetChanged()
        }
    }

}
