package com.example.ae_chat_sdk.acti.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.ae_chat_sdk.PhotoActivity
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.home.HomeActivity
import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.api.service.WebSocketListener
import com.example.ae_chat_sdk.data.model.Image
import com.example.ae_chat_sdk.data.model.Message
import com.example.ae_chat_sdk.databinding.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MessageAdapter(val context: Context,val appCompatActivity: AppCompatActivity, val groupId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val webSocketListener: WebSocketListener = WebSocketListener()
    var listMessage: ArrayList<Message> = ArrayList()
//    var imageUrl =
//        "https://3.bp.blogspot.com/-SMNLs_5XfVo/VHvNUx8dWZI/AAAAAAAAQnY/NWdkO4JPE_M/s1600/Avatar-Facebook-an-danh-trang-4.jpg"

    enum class TypeView(val typeView: Int) {
        FIRST_MESSAGE(0),
        TEXT_SEND(1),
        TEXT_RECEIVE(2),
        IMG_SEND(3),
        IMG_RECEVIE(4)
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

    inner class ImageSenderHolder(val binding: LayoutFrameImageSenderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Message) {
            if (data.attachment == null) {
                GlobalScope.launch(Dispatchers.Main) {
                    Glide.with(context).load("")
                        .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.image_default)
                        .error(R.drawable.image_default)
                        .apply(RequestOptions().override(600, 800))
                        .into(binding.ivImageMessageSender)
                }
            } else {
                val gson = Gson()
                val img = gson.fromJson(gson.toJson(data.attachment), Image::class.java)
                val url = ApiConstant.URL_IMAGE + img.url
                binding.ivImageMessageSender.setOnClickListener(View.OnClickListener {
                    val intent = Intent(context, PhotoActivity::class.java)
                    intent.putExtra("imageUrl", url)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                })
                GlobalScope.launch(Dispatchers.Main) {
                    Glide.with(context).load(url)
                        .placeholder(R.drawable.image_default)
                        .error(R.drawable.image_default)
                        .apply(RequestOptions().override(600, 800))
                        .into(binding.ivImageMessageSender)
                }
            }
        }
    }

    inner class ImageReceiverHolder(val binding: LayoutFrameImageReceiverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Message) {
            if (data.attachment == null) {
                binding.ivImageMessageReceiver.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.image_default
                    )
                );
            } else {
                val gson = Gson()
                val img = gson.fromJson(gson.toJson(data.attachment), Image::class.java)
                val url = ApiConstant.URL_IMAGE + img.url
                binding.ivImageMessageReceiver.setOnClickListener(View.OnClickListener {
                    val intent = Intent(context, PhotoActivity::class.java)
                    intent.putExtra("imageUrl", url)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                })
                GlobalScope.launch(Dispatchers.Main) {
                    Glide.with(context).load(url)
                        .placeholder(R.drawable.image_default)
                        .error(R.drawable.image_default)
                        .apply(RequestOptions().override(600, 800))
                        .into(binding.ivImageMessageReceiver)
                }
            }
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

        } else (if (viewType == TypeView.TEXT_RECEIVE.ordinal) {
            val view = LayoutFrameMessageReceiverBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ReceiveMessageFooterHolder(view)
        } else if (viewType == TypeView.IMG_SEND.ordinal) {
            val view = LayoutFrameImageSenderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ImageSenderHolder(view)
        } else if (viewType == TypeView.IMG_RECEVIE.ordinal) {
            val view = LayoutFrameImageReceiverBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ImageReceiverHolder(view)
        } else {
            val view = LayoutFrameMessageSenderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            SendMessageFooterHolder(view)
        })
    }

    override fun getItemCount(): Int {
        return listMessage.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message: Message = listMessage[position]

        if (getItemViewType(position) == TypeView.FIRST_MESSAGE.ordinal) {
            (holder as BeginMessageHolder).bind()
        } else if (getItemViewType(position) == TypeView.TEXT_RECEIVE.typeView) {
            (holder as ReceiveMessageFooterHolder).bind(message)
        } else if (getItemViewType(position) == TypeView.IMG_SEND.typeView) {
            (holder as ImageSenderHolder).bind(message)
        } else if (getItemViewType(position) == TypeView.IMG_RECEVIE.typeView) {
            (holder as ImageReceiverHolder).bind(message)
        } else {
            (holder as SendMessageFooterHolder).bind(message)
        }
        val senderUin = message.senderUin
        if (senderUin == RestClient().getUserId()) {
            if (position == listMessage.size - 1 && message.status == Message.Status.SEEN.ordinal) {
                Log.e("LASTMS", message.message.toString())
                if (holder is SendMessageFooterHolder) {
                    holder.binding.ivCheckSeen.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.avatardefault
                        )
                    )
                }
            } else if (message.status == Message.Status.SENDING.ordinal) {
                if (holder is SendMessageFooterHolder) {
                    holder.binding.ivCheckSeen.setImageDrawable(
                        ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.ic_check_sending
                        )
                    )
                } else if (holder is ImageSenderHolder) {
                    holder.binding.ivCheckSeen.setImageDrawable(
                        ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.ic_check_sending
                        )
                    )
                }
            } else if (message.status == Message.Status.SENT.ordinal) {
                if (holder is SendMessageFooterHolder) {
                    holder.binding.ivCheckSeen.setImageDrawable(
                        ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.ic_check_seen
                        )
                    )
                } else if (holder is ImageSenderHolder) {
                    holder.binding.ivCheckSeen.setImageDrawable(
                        ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.ic_check_seen
                        )
                    )
                }
            } else if (message.status == Message.Status.SEEN.ordinal && listMessage[position + 1].status != Message.Status.SEEN.ordinal) {
                if (holder is SendMessageFooterHolder) {
//                    Glide.with(context).load(imageUrl).into(holder.binding.ivCheckSeen)
                    holder.binding.ivCheckSeen.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.avatardefault
                        )
                    )
                }
            } else {
            }
            if (getItemViewType(position) != TypeView.FIRST_MESSAGE.ordinal) {
                if (senderUin == RestClient().getUserId()) {
                    if (position < listMessage.size - 1 && position > 0) {
                        if (listMessage[position - 1].senderUin == senderUin && listMessage[position + 1].senderUin == senderUin) {
                            if (holder is SendMessageFooterHolder) {
                                holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_two)
                            }
                        } else if (listMessage[position - 1].senderUin != senderUin && listMessage[position + 1].senderUin == senderUin) {
                            if (holder is SendMessageFooterHolder) {
                                holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_one_bottom)
                            }
                        } else if (listMessage[position - 1].senderUin != senderUin && listMessage[position + 1].senderUin != senderUin) {
                            if (holder is SendMessageFooterHolder) {
                                holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_four)
                            }
                        } else {
                            if (holder is SendMessageFooterHolder) {
                                holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_one_top)
                            }
                        }
                    } else if (position == listMessage.size - 1) {
                        if (listMessage[position - 1].senderUin != senderUin) {
                            if (holder is SendMessageFooterHolder) {
                                holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_four)
                            }
                        } else {
                            if (holder is SendMessageFooterHolder) {
                                holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_one_top)
                            }
                        }
                    } else if (position == 0) {
                        if (listMessage[position + 1].senderUin == senderUin) {
                            if (holder is SendMessageFooterHolder) {
                                holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_one_bottom)
                            }
                        } else {
                            if (holder is SendMessageFooterHolder) {
                                holder.binding.tvSenderFooter.setBackgroundResource(R.drawable.bg_message_send_four)
                            }
                        }
                    }
                } else {
                    if (position < listMessage.size - 1 && position > 0) {
                        if (listMessage[position - 1].senderUin == senderUin && listMessage[position + 1].senderUin == senderUin) {
                            if (holder is ReceiveMessageFooterHolder) {
                                holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_two)
                            }
                        } else if (listMessage[position - 1].senderUin != senderUin && listMessage[position + 1].senderUin == senderUin) {
                            if (holder is ReceiveMessageFooterHolder) {
                                holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_one_bottom)
                            }
                        } else if (listMessage[position - 1].senderUin != senderUin && listMessage[position + 1].senderUin != senderUin) {
                            if (holder is ReceiveMessageFooterHolder) {
                                holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_four)
                            }
                        } else {
                            if (holder is ReceiveMessageFooterHolder) {
                                holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_one_top)
                            }
                        }
                    } else if (position == listMessage.size - 1) {
                        if (listMessage[position - 1].senderUin != senderUin) {
                            if (holder is ReceiveMessageFooterHolder) {
                                holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_four)
                            }
                        } else {
                            if (holder is ReceiveMessageFooterHolder) {
                                holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_one_top)
                            }
                        }
                    } else if (position == 0) {
                        if (listMessage[position + 1].senderUin == senderUin) {
                            if (holder is ReceiveMessageFooterHolder) {
                                holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_one_bottom)
                            }
                        } else {
                            if (holder is ReceiveMessageFooterHolder) {
                                holder.binding.tvReceiverFooter.setBackgroundResource(R.drawable.bg_message_receive_four)
                            }
                        }
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
        }
        else if (ms.senderUin == RestClient().getUserId()){
            if (ms.type?.toFloat()!!.toInt() == Message.Type.TEXT.ordinal){
                return TypeView.TEXT_SEND.ordinal
            } else if(ms.type?.toFloat()!!
                    .toInt() == Message.Type.IMAGE.type){
                return TypeView.IMG_SEND.ordinal
            }
        }else{
            if (ms.type?.toFloat()!!.toInt() == Message.Type.TEXT.ordinal){
                return TypeView.TEXT_RECEIVE.ordinal
            } else if(ms.type?.toFloat()!!
                    .toInt() == Message.Type.IMAGE.type){
                return TypeView.IMG_RECEVIE.ordinal
            }
        }
        return 0
    }

    //@SuppressLint("NotifyDataSetChanged")
    fun getMessages(message: ArrayList<Message>) {
        //listMessage.add(message)
        this.listMessage = message
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addMessage(message: Message) {
        if (message.groupId == this.groupId) {
            if (message.messageId != null && message.senderUin != RestClient().getUserId()) {
                listMessage.add(message)
                webSocketListener.seenMessage(HomeActivity.webSocket, message.messageId!!)
            }
            else if (listMessage.size>0 ){
                for (i in listMessage.indices.reversed()) {
                    if (listMessage[i].createdAt.toString() == message.createdAt.toString() && listMessage[i].status == Message.Status.SENDING.ordinal && listMessage[i].type != Message.Type.FIRST_MESSAGE.ordinal) {
                        listMessage[i] = message
                        break
                    }
                }
            }else{
                listMessage.add(message)
            }
            notifyDataSetChanged()
        }

    }

    fun checkSeenMessage(message: Message) {
        for (i in listMessage.indices.reversed()) {
            if (message.messageId == listMessage[i].messageId && message.groupId == this.groupId) {
                listMessage[i] = message
                break
            }
        }
    }

    fun addMessageSeeding(message: Message) {
        if (message.messageId == null && message.status == Message.Status.SENDING.ordinal) {
            listMessage.add(message)
            notifyDataSetChanged()
        }
    }
}
