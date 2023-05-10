package com.example.ae_chat_sdk.acti.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.home.HomeActivity
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.api.service.WebSocketListener
import com.example.ae_chat_sdk.data.model.Message
import com.example.ae_chat_sdk.databinding.LayoutFrameMessageBeginBinding
import com.example.ae_chat_sdk.databinding.LayoutFrameMessageReceiverBinding
import com.example.ae_chat_sdk.databinding.LayoutFrameMessageSenderBinding

class MessageAdapter(val context: Context,val groupId : String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val webSocketListener: WebSocketListener = WebSocketListener()
    var listMessage: ArrayList<Message> = ArrayList()
//    var imageUrl =
//        "https://3.bp.blogspot.com/-SMNLs_5XfVo/VHvNUx8dWZI/AAAAAAAAQnY/NWdkO4JPE_M/s1600/Avatar-Facebook-an-danh-trang-4.jpg"

    enum class TypeView(val typeView: Int) {
        FIRST_MESSAGE(0),
        SEND(1),
        RECEIVE(2)
    }

    inner class SendMessageFooterHolder(val binding: LayoutFrameMessageSenderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Message) {
            Log.d("Check Begin", "Send")
            binding.tvSenderFooter.text = data.message
            binding.ivCheckSeen.setImageDrawable(null)
        }
    }

    inner class ReceiveMessageFooterHolder(val binding: LayoutFrameMessageReceiverBinding) :
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
            val view = LayoutFrameMessageReceiverBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ReceiveMessageFooterHolder(view)
        } else {
            val view = LayoutFrameMessageSenderBinding.inflate(
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

        val senderUin = listMessage[position].senderUin
        if (senderUin == RestClient().getUserId()) {
            if (position == listMessage.size-1 && listMessage[position].status == Message.Status.SEEN.ordinal){
                Log.e("LASTMS",listMessage[position].message.toString())
                if(holder is SendMessageFooterHolder){
//                    Glide.with(context).load(imageUrl).into(holder.binding.ivCheckSeen)
                    //holder.binding.ivCheckSeen.setImageDrawable(null)
                    holder.binding.ivCheckSeen.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.avatardefault
                        )
                    )
                }
            }else if (listMessage[position].status == Message.Status.SENDING.ordinal) {
                if (holder is SendMessageFooterHolder) {
                    holder.binding.ivCheckSeen.setImageDrawable(
                        ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.ic_check_sending
                        )
                    )
                }
            }else if (listMessage[position].status == Message.Status.SENT.ordinal) {
                if (holder is SendMessageFooterHolder) {
                    holder.binding.ivCheckSeen.setImageDrawable(
                        ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.ic_check_seen
                        )
                    )
                }
            }else if (listMessage[position].status == Message.Status.SEEN.ordinal && listMessage[position+1].status != Message.Status.SEEN.ordinal){
                if(holder is SendMessageFooterHolder) {
//                    Glide.with(context).load(imageUrl).into(holder.binding.ivCheckSeen)
                    holder.binding.ivCheckSeen.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.avatardefault
                        )
                    )
                }
            }
        } else {
        }
        if (senderUin == RestClient().getUserId()){
            if (position< listMessage.size - 1 && position > 0){
                if (listMessage[position-1].senderUin == senderUin && listMessage[position+1].senderUin == senderUin){
                    if (holder is SendMessageFooterHolder) {
                        holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_two)
                    }
                } else if(listMessage[position-1].senderUin != senderUin && listMessage[position+1].senderUin == senderUin){
                    if (holder is SendMessageFooterHolder) {
                        holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_one_bottom)
                    }
                }else if(listMessage[position-1].senderUin != senderUin && listMessage[position+1].senderUin != senderUin){
                    if (holder is SendMessageFooterHolder) {
                        holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_four)
                    }
                }else{
                    if (holder is SendMessageFooterHolder) {
                        holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_one_top)
                    }
                }
            }else if(position == listMessage.size - 1){
                if(listMessage[position-1].senderUin != senderUin){
                    if (holder is SendMessageFooterHolder) {
                        holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_four)
                    }
                }else{
                    if (holder is SendMessageFooterHolder) {
                        holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_one_top)
                    }
                }
            }else if(position == 0){
                if(listMessage[position+1].senderUin == senderUin){
                    if (holder is SendMessageFooterHolder) {
                        holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_one_bottom)
                    }
                }else {
                    if (holder is SendMessageFooterHolder) {
                        holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_four)
                    }
                }
            }
        }else {
            if (position< listMessage.size - 1 && position > 0){
                if (listMessage[position-1].senderUin == senderUin && listMessage[position+1].senderUin == senderUin){
                    if (holder is ReceiveMessageFooterHolder) {
                        holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_two)
                    }
                } else if(listMessage[position-1].senderUin != senderUin && listMessage[position+1].senderUin == senderUin){
                    if (holder is ReceiveMessageFooterHolder) {
                        holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_one_bottom)
                    }
                }else if(listMessage[position-1].senderUin != senderUin && listMessage[position+1].senderUin != senderUin){
                    if (holder is ReceiveMessageFooterHolder) {
                        holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_four)
                    }
                }else{
                    if (holder is ReceiveMessageFooterHolder) {
                        holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_one_top)
                    }
                }
            }else if(position == listMessage.size - 1){
                if(listMessage[position-1].senderUin != senderUin){
                    if (holder is ReceiveMessageFooterHolder) {
                        holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_four)
                    }
                }else{
                    if (holder is ReceiveMessageFooterHolder) {
                        holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_one_top)
                    }
                }
            }else if(position == 0){
                if(listMessage[position+1].senderUin == senderUin){
                    if (holder is ReceiveMessageFooterHolder) {
                        holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_one_bottom)
                    }
                }else {
                    if (holder is ReceiveMessageFooterHolder) {
                        holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_four)
                    }
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        val ms: Message = listMessage[position]
        ms.message?.let { Log.d("message", it) }
        if (ms.message == "" && ms.type?.toFloat()!!.toInt() == Message.Type.FIRST_MESSAGE.type) {
            return TypeView.FIRST_MESSAGE.typeView
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun addMessage(message: Message){
        if(message.groupId == this.groupId){
            if (message.messageId != null && message.senderUin != RestClient().getUserId()){
                listMessage.add(message)
                webSocketListener.seenMessage(HomeActivity.webSocket, message.messageId!!)
            }else {
                for(i in listMessage.indices.reversed()){

                    if ( listMessage[i].createdAt.toString()  == message.createdAt.toString() && listMessage[i].status== Message.Status.SENDING.ordinal){
                        listMessage[i] = message
                        break
                    }
                }
            }
            notifyDataSetChanged()
        }

    }
    fun checkSeenMessage(message : Message){
        for(i in listMessage.indices.reversed()){
            if ( message.messageId == listMessage[i].messageId && message.groupId == this.groupId ){
                listMessage[i] = message
                break
            }
        }
    }
    fun addMessageSeeding(message: Message){
        if(message.messageId == null && message.status == Message.Status.SENDING.ordinal){
            listMessage.add(message)
            notifyDataSetChanged()
        }
    }
}
