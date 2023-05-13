package com.example.ae_chat_sdk.acti.adapter

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
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
import com.example.ae_chat_sdk.data.model.Video
import com.example.ae_chat_sdk.databinding.*
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MessageAdapter(
    val context: Context, val appCompatActivity: AppCompatActivity, val groupId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val webSocketListener: WebSocketListener = WebSocketListener()
    var listMessage: ArrayList<Message> = ArrayList()
    var mediaPlayer: MediaPlayer? = null

    enum class TypeView(val typeView: Int) {
        FIRST_MESSAGE(0),
        TEXT_SEND(1),
        TEXT_RECEIVE(2),
        IMG_SEND(3),
        IMG_RECEVIE(4),
        AUDIO_SEND(5),
        AUDIO_RECEIVE(6),
        VIDEO_SEND(7),
        VIDEO_RECEIVE(8)
    }

    inner class MessageSenderHolder(private val binding: LayoutFrameMessageSenderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var tvMessageContent: TextView
        lateinit var ivCheckSeen: CircleImageView
        lateinit var ivImageMessage: ImageView
        lateinit var cvImageMessage: CardView
        lateinit var llAudio: LinearLayout
        lateinit var ibPlay: ImageButton
        lateinit var aniAudio: LottieAnimationView
        lateinit var vvVideoMessage: VideoView
        lateinit var cvVideoMessage: CardView
        lateinit var ibPlayVideo: ImageButton
        fun bind() {
            tvMessageContent = binding.tvMessageContent
            ivCheckSeen = binding.ivCheckSeen
            ivImageMessage = binding.ivImageMessage
            cvImageMessage = binding.cvImageMessage
            llAudio = binding.llAudio
            ibPlay = binding.ibPlay
            aniAudio = binding.animationView
            vvVideoMessage = binding.vvVideoMessage
            cvVideoMessage = binding.cvVideoMessage
            ibPlayVideo = binding.ibPlayVideo
//            val layoutParams = vvVideoMessage.layoutParams
//            layoutParams.width = 400
//            layoutParams.height = 250
//            vvVideoMessage.layoutParams = layoutParams
//            ivThumbnail = binding.ivThumbnail
        }
    }

    inner class MessageReceiverHolder(private val binding: LayoutFrameMessageReceiverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var tvMessageContent: TextView
        lateinit var ivAvatar: CircleImageView
        lateinit var ivImageMessage: ImageView
        lateinit var cvImageMessage: CardView
        lateinit var llAudio: LinearLayout
        lateinit var ibPlay: ImageButton
        lateinit var aniAudio: LottieAnimationView
        lateinit var vvVideoMessage: VideoView
        lateinit var cvVideoMessage: CardView
        lateinit var ibPlayVideo: ImageButton
        fun bind() {
            tvMessageContent = binding.tvMessageContent
            ivAvatar = binding.ivAvatar
            ivImageMessage = binding.ivImageMessage
            cvImageMessage = binding.cvImageMessage
            llAudio = binding.llAudio
            ibPlay = binding.ibPlay
            aniAudio = binding.animationView
            vvVideoMessage = binding.vvVideoMessage
            cvVideoMessage = binding.cvVideoMessage
            ibPlayVideo = binding.ibPlayVideo
            val layoutParams = vvVideoMessage.layoutParams
            layoutParams.width = 400
            layoutParams.height = 250
            vvVideoMessage.layoutParams = layoutParams
        }
    }

    inner class BeginMessageHolder(private val binding: LayoutFrameMessageBeginBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.tvMessageBegin.text = "Bạn đã bắt đầu cuộc trò chuyện."
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TypeView.TEXT_SEND.ordinal,
            TypeView.IMG_SEND.ordinal,
            TypeView.AUDIO_SEND.ordinal,
            TypeView.VIDEO_SEND.ordinal -> {
                val view = LayoutFrameMessageSenderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                MessageSenderHolder(view)
            }
            TypeView.TEXT_RECEIVE.ordinal,
            TypeView.IMG_RECEVIE.ordinal,
            TypeView.AUDIO_RECEIVE.ordinal,
            TypeView.VIDEO_RECEIVE.ordinal -> {
                val view = LayoutFrameMessageReceiverBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                MessageReceiverHolder(view)
            }
            else -> {
                val view = LayoutFrameMessageBeginBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                BeginMessageHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return listMessage.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message: Message = listMessage[position]
        val senderUin = message.senderUin
        var audioPlaying = false

        when (getItemViewType(position)) {
            TypeView.FIRST_MESSAGE.ordinal -> {
                (holder as BeginMessageHolder).bind()
            }
            TypeView.TEXT_RECEIVE.ordinal -> {
                (holder as MessageReceiverHolder).bind()
                holder.tvMessageContent.visibility = View.VISIBLE

                holder.tvMessageContent.text = message.message
                holder.ivAvatar.visibility = View.INVISIBLE


                // Edit frame
                if (position < listMessage.size - 1 && position > 0) {
                    var messBefore = listMessage[position - 1]
                    var messAfter = listMessage[position + 1]
                    if (messBefore.senderUin == senderUin
                        && messAfter.senderUin == senderUin
                        && messBefore.type?.toFloat()!!.toInt() == Message.Type.TEXT.ordinal
                        && messAfter.type?.toFloat()!!.toInt() == Message.Type.TEXT.ordinal
                    ) {
                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_receive_two)

                    } else if ((messBefore.senderUin != senderUin
                                || messBefore.type?.toFloat()!!
                            .toInt() == Message.Type.IMAGE.ordinal
                                || messBefore.type?.toFloat()!!
                            .toInt() == Message.Type.FIRST_MESSAGE.ordinal)
                        && messAfter.senderUin == senderUin
                        && messAfter.type?.toFloat()!!.toInt() == Message.Type.TEXT.ordinal
                    ) {

                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_receive_one_bottom)

                    } else if (messBefore.senderUin == senderUin
                        && (messAfter.senderUin != senderUin || messAfter.type?.toFloat()!!
                            .toInt() == Message.Type.IMAGE.ordinal)
                        && messBefore.type?.toFloat()!!.toInt() == Message.Type.TEXT.ordinal
                    ) {
                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_receive_one_top)
                    } else {
                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_receive_four)
                    }
                } else if (position == listMessage.size - 1) {
                    if (listMessage[position - 1].senderUin == senderUin
                        && listMessage[position - 1].type?.toFloat()!!
                            .toInt() == Message.Type.TEXT.ordinal
                    ) {
                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_receive_one_top)
                    } else {
                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_receive_four)
                    }
                } else if (position == 0) {
                    if (listMessage[position + 1].senderUin == senderUin
                        && listMessage[position + 1].type?.toFloat()!!
                            .toInt() == Message.Type.TEXT.ordinal
                    ) {
                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_receive_one_bottom)

                    } else {
                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_receive_four)

                    }
                }
            }
            TypeView.IMG_RECEVIE.ordinal -> {
                (holder as MessageReceiverHolder).bind()
                holder.cvImageMessage.visibility = View.VISIBLE

                holder.ivAvatar.visibility = View.INVISIBLE
                if (message.attachment == null) {
                    holder.ivImageMessage.setImageDrawable(
                        ContextCompat.getDrawable(
                            context, R.drawable.image_default
                        )
                    );
                } else {
                    val gson = Gson()
                    val img = gson.fromJson(gson.toJson(message.attachment), Image::class.java)
                    val url = ApiConstant.URL_IMAGE + img.url
                    holder.ivImageMessage.setOnClickListener(View.OnClickListener {
                        val intent = Intent(context, PhotoActivity::class.java)
                        intent.putExtra("imageUrl", url)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    })
                    GlobalScope.launch(Dispatchers.Main) {
                        Glide.with(context).load(url).placeholder(R.drawable.image_default)
                            .error(R.drawable.image_default)
                            .apply(RequestOptions().override(600, 800))
                            .into(holder.ivImageMessage)
                    }
                }
            }
            TypeView.AUDIO_RECEIVE.ordinal -> {
                (holder as MessageReceiverHolder).bind()
                holder.llAudio.visibility = View.VISIBLE
                holder.ibPlay.setOnClickListener(View.OnClickListener {
                    if (audioPlaying) {
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null
                        audioPlaying = false
                        holder.ibPlay.background =
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_play
                            )
                        holder.aniAudio.pauseAnimation()
                    } else {
                        val gson = Gson()
                        val img = gson.fromJson(gson.toJson(message.attachment), Image::class.java)
                        val url = ApiConstant.URL_AUDIO + img.url

                        GlobalScope.launch(Dispatchers.Main) {
                            mediaPlayer = MediaPlayer().apply {
                                setDataSource(url)
                                prepareAsync()
                                setOnCompletionListener {
                                    this.stop()
                                    this.release()
                                    mediaPlayer = null
                                    audioPlaying = false
                                    holder.ibPlay.background =
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.ic_play
                                        )

                                    holder.aniAudio.pauseAnimation()
                                }
                                setOnErrorListener { mediaPlayer, i, i2 ->
                                    audioPlaying = false
                                    holder.ibPlay.background =
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.ic_play
                                        )

                                    holder.aniAudio.pauseAnimation()
                                    true
                                }
                                setOnPreparedListener { mediaPlayer ->
                                    mediaPlayer.start()
                                }

                            }

                        }

                        audioPlaying = true
                        holder.ibPlay.background =
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_pause
                            )
                        holder.aniAudio.playAnimation()
                    }

                })
            }
            TypeView.VIDEO_RECEIVE.ordinal -> {
                (holder as MessageReceiverHolder).bind()
                var videoPlaying = false

                holder.cvVideoMessage.visibility = View.VISIBLE
                if (message.attachment != null) {
                    val gson = Gson()
                    val video = gson.fromJson(gson.toJson(message.attachment), Video::class.java)
                    val url = ApiConstant.URL_VIDEO + video.url
                    val videoUri = Uri.parse(url)
                    Log.d("HSJDHFKS", videoUri.toString())

                    holder.vvVideoMessage.setVideoPath(videoUri.toString())
                    holder.vvVideoMessage.seekTo(1);
                    holder.vvVideoMessage.setOnClickListener(View.OnClickListener {
                        if (videoPlaying) {
                            videoPlaying = false
                            holder.ibPlayVideo.visibility = View.VISIBLE
                            holder.vvVideoMessage.pause()
                        }
                    })
                    holder.vvVideoMessage.setOnCompletionListener {
                        videoPlaying = false
                        holder.ibPlayVideo.visibility = View.VISIBLE
                    }
                    holder.ibPlayVideo.setOnClickListener(View.OnClickListener {
                        if (!videoPlaying) {
                            holder.ibPlayVideo.visibility = View.GONE
                            holder.vvVideoMessage.start()
                            videoPlaying = true
                        }
                    })
                }

            }
            TypeView.TEXT_SEND.ordinal -> {
                (holder as MessageSenderHolder).bind()
                holder.tvMessageContent.visibility = View.VISIBLE

                holder.tvMessageContent.text = message.message


                // Edit frame
                if (position < listMessage.size - 1 && position > 0) {
                    var messBefore = listMessage[position - 1]
                    var messAfter = listMessage[position + 1]
                    if (messBefore.senderUin == senderUin
                        && messAfter.senderUin == senderUin
                        && messBefore.type?.toFloat()!!.toInt() == Message.Type.TEXT.ordinal
                        && messAfter.type?.toFloat()!!.toInt() == Message.Type.TEXT.ordinal
                    ) {
                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_send_two)

                    } else if ((messBefore.senderUin != senderUin
                                || messBefore.type?.toFloat()!!
                            .toInt() == Message.Type.IMAGE.ordinal
                                || messBefore.type?.toFloat()!!
                            .toInt() == Message.Type.FIRST_MESSAGE.ordinal)
                        && messAfter.senderUin == senderUin
                        && messAfter.type?.toFloat()!!.toInt() == Message.Type.TEXT.ordinal
                    ) {

                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_send_one_bottom)

                    } else if (messBefore.senderUin == senderUin
                        && (messAfter.senderUin != senderUin || messAfter.type?.toFloat()!!
                            .toInt() == Message.Type.IMAGE.ordinal)
                        && messBefore.type?.toFloat()!!.toInt() == Message.Type.TEXT.ordinal
                    ) {
                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_send_one_top)
                    } else {
                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_send_four)
                    }
                } else if (position == listMessage.size - 1) {
                    if (listMessage[position - 1].senderUin == senderUin
                        && listMessage[position - 1].type?.toFloat()!!
                            .toInt() == Message.Type.TEXT.ordinal
                    ) {
                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_send_one_top)
                    } else {
                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_send_four)
                    }
                } else if (position == 0) {
                    if (listMessage[position + 1].senderUin == senderUin
                        && listMessage[position + 1].type?.toFloat()!!
                            .toInt() == Message.Type.TEXT.ordinal
                    ) {
                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_send_one_bottom)

                    } else {
                        holder.tvMessageContent.setBackgroundResource(R.drawable.bg_message_send_four)

                    }
                }
            }
            TypeView.IMG_SEND.ordinal -> {
                (holder as MessageSenderHolder).bind()
                holder.cvImageMessage.visibility = View.VISIBLE

                if (message.attachment == null) {
                    GlobalScope.launch(Dispatchers.Main) {
                        Glide.with(context).load("").skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.image_default).error(R.drawable.image_default)
                            .apply(RequestOptions().override(600, 800))
                            .into(holder.ivImageMessage)
                    }
                } else {
                    val gson = Gson()
                    val img = gson.fromJson(gson.toJson(message.attachment), Image::class.java)
                    val url = ApiConstant.URL_IMAGE + img.url
                    holder.ivImageMessage.setOnClickListener(View.OnClickListener {
                        val intent = Intent(context, PhotoActivity::class.java)
                        intent.putExtra("imageUrl", url)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    })
                    GlobalScope.launch(Dispatchers.Main) {
                        Glide.with(context).load(url).placeholder(R.drawable.image_default)
                            .error(R.drawable.image_default)
                            .apply(RequestOptions().override(600, 800))
                            .into(holder.ivImageMessage)
                    }
                }
            }
            TypeView.AUDIO_SEND.ordinal -> {
                (holder as MessageSenderHolder).bind()
                Log.d("CHKADDFSK", message.attachment.toString())

                holder.llAudio.visibility = View.VISIBLE
                holder.ibPlay.setOnClickListener(View.OnClickListener {
                    if (audioPlaying) {
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null
                        audioPlaying = false
                        holder.ibPlay.background =
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_play
                            )

                        holder.aniAudio.pauseAnimation()
                    } else {
                        val gson = Gson()
                        val img = gson.fromJson(gson.toJson(message.attachment), Image::class.java)
                        val url = ApiConstant.URL_AUDIO + img.url
                        GlobalScope.launch(Dispatchers.Main) {
                            mediaPlayer = MediaPlayer().apply {
                                setDataSource(url)
                                prepareAsync()
                                setOnCompletionListener {
                                    this.stop()
                                    this.release()
                                    mediaPlayer = null
                                    audioPlaying = false
                                    holder.ibPlay.background =
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.ic_play
                                        )

                                    holder.aniAudio.pauseAnimation()
                                }
                                setOnErrorListener { mediaPlayer, i, i2 ->
                                    audioPlaying = false
                                    holder.ibPlay.background =
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.ic_play
                                        )

                                    holder.aniAudio.pauseAnimation()
                                    true
                                }
                                setOnPreparedListener { mediaPlayer ->
                                    mediaPlayer.start()
                                }

                            }

                        }

                        audioPlaying = true
                        holder.ibPlay.background =
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_pause
                            )
                        holder.aniAudio.playAnimation()
                    }

                })
            }
            TypeView.VIDEO_SEND.ordinal -> {
                (holder as MessageSenderHolder).bind()
                var videoPlaying = false

                holder.cvVideoMessage.visibility = View.VISIBLE
//                val url_video = Uri.parse("http://techslides.com/demos/sample-videos/small.mp4")
//                holder.vvVideoMessage.setVideoPath(url_video.toString())
//                holder.vvVideoMessage.seekTo(1);
//                holder.vvVideoMessage.setOnClickListener(View.OnClickListener {
//                    if (videoPlaying) {
//                        videoPlaying = false
//                        holder.ibPlayVideo.visibility = View.VISIBLE
//                        holder.vvVideoMessage.pause()
//                    }
//                })
//                holder.vvVideoMessage.setOnCompletionListener {
//                    videoPlaying = false
//                    holder.ibPlayVideo.visibility = View.VISIBLE
//                }
//                holder.ibPlayVideo.setOnClickListener(View.OnClickListener {
//                    if (!videoPlaying) {
//                        holder.ibPlayVideo.visibility = View.GONE
//                        holder.vvVideoMessage.start()
//                        videoPlaying = true
//                    }
//                })

                if (message.attachment != null) {
                    val gson = Gson()
                    val video = gson.fromJson(gson.toJson(message.attachment), Video::class.java)
                    val url = ApiConstant.URL_VIDEO + video.url
//                    holder.vvVideoMessage.setOnClickListener(View.OnClickListener {
//                        val intent = Intent(context, PhotoActivity::class.java)
//                        intent.putExtra("imageUrl", url)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        context.startActivity(intent)
//                    })
                    Log.d("HSJDHFKS", url)

                    holder.vvVideoMessage.setVideoPath(url)
                    holder.vvVideoMessage.seekTo(1);
                    holder.vvVideoMessage.setOnClickListener(View.OnClickListener {
                        if (videoPlaying) {
                            videoPlaying = false
                            holder.ibPlayVideo.visibility = View.VISIBLE
                            holder.vvVideoMessage.pause()
                        }
                    })
                    holder.vvVideoMessage.setOnCompletionListener {
                        videoPlaying = false
                        holder.ibPlayVideo.visibility = View.VISIBLE
                    }
                    holder.ibPlayVideo.setOnClickListener(View.OnClickListener {
                        if (!videoPlaying) {
                            holder.ibPlayVideo.visibility = View.GONE
                            holder.vvVideoMessage.start()
                            videoPlaying = true
                        }
                    })
                }
            }
        }

        if (holder is MessageSenderHolder) {
            holder.ivCheckSeen.visibility = View.VISIBLE
            if (message.status == Message.Status.SEEN.ordinal) {
                Glide.with(context)
                    .load(ApiConstant.URL_IMAGE + listMessage[position].senderAvatar)
                    .placeholder(R.drawable.avatardefault).error(R.drawable.avatardefault)
                    .into(holder.ivCheckSeen)
            } else if (message.status == Message.Status.SENDING.ordinal) {
                holder.ivCheckSeen.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context, R.drawable.ic_check_sending
                    )
                )
            } else if (message.status == Message.Status.SENT.ordinal) {

                holder.ivCheckSeen.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context, R.drawable.ic_check_seen
                    )
                )
            } else if (message.status == Message.Status.SEEN.ordinal && listMessage[position + 1].status != Message.Status.SEEN.ordinal) {
                if (listMessage[position].senderAvatar != null) {
                    Glide.with(context)
                        .load(ApiConstant.URL_IMAGE + listMessage[position].senderAvatar)
                        .into(holder.ivCheckSeen)
                } else {
                    Glide.with(context).load(R.drawable.avatardefault)
                        .into(holder.ivCheckSeen)
                }
            }
            if (position > 0 && position < listMessage.size - 1) {
                if (message.status == Message.Status.SEEN.ordinal
                    && listMessage[position + 1].status == Message.Status.SEEN.ordinal
                ) {
                    holder.ivCheckSeen.visibility = View.INVISIBLE
                }
            }
        }
        if (holder is MessageReceiverHolder) {
            holder.ivAvatar.visibility = View.INVISIBLE
            Glide.with(context).load(ApiConstant.URL_IMAGE + message.senderAvatar)
                .placeholder(R.drawable.avatardefault).error(R.drawable.avatardefault)
                .into(holder.ivAvatar)

            if (position < listMessage.size - 1 && position > 0) {
                var messAfter = listMessage[position + 1]
                if (messAfter.senderUin != senderUin)
                    holder.ivAvatar.visibility = View.VISIBLE
            } else if (position == listMessage.size - 1) {
                holder.ivAvatar.visibility = View.VISIBLE
            } else if (position == 0) {
                holder.ivAvatar.visibility = View.VISIBLE
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        val ms: Message = listMessage[position]
        ms.message?.let { Log.d("message", it) }

        if (ms.message == "" && ms.type?.toFloat()!!
                .toInt() == Message.Type.FIRST_MESSAGE.ordinal
        ) {
            return TypeView.FIRST_MESSAGE.ordinal
        } else if (ms.senderUin == RestClient().getUserId()) {
            when (ms.type?.toFloat()!!.toInt()) {
                Message.Type.TEXT.ordinal -> return TypeView.TEXT_SEND.ordinal
                Message.Type.IMAGE.ordinal -> return TypeView.IMG_SEND.ordinal
                Message.Type.AUDIO.ordinal -> return TypeView.AUDIO_SEND.ordinal
                Message.Type.VIDEO.ordinal -> return TypeView.VIDEO_SEND.ordinal
            }
        } else if (ms.senderUin != RestClient().getUserId()) {
            when (ms.type?.toFloat()!!.toInt()) {
                Message.Type.TEXT.ordinal -> return TypeView.TEXT_RECEIVE.ordinal
                Message.Type.IMAGE.ordinal -> return TypeView.IMG_RECEVIE.ordinal
                Message.Type.AUDIO.ordinal -> return TypeView.AUDIO_RECEIVE.ordinal
                Message.Type.VIDEO.ordinal -> return TypeView.VIDEO_SEND.ordinal
            }
        }
        return 0
    }

    //@SuppressLint("NotifyDataSetChanged")
    fun setMessages(message: ArrayList<Message>) {
        this.listMessage = message
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addMessage(message: Message) {
        if (message.groupId == this.groupId) {
            if (message.messageId != null && message.senderUin != RestClient().getUserId()) {
                listMessage.add(message)
                webSocketListener.seenMessage(HomeActivity.webSocket, message.messageId!!)
            } else if (listMessage.size > 0) {
                for (i in listMessage.indices.reversed()) {
                    if (listMessage[i].createdAt.toString() == message.createdAt.toString()
                        && listMessage[i].status == Message.Status.SENDING.ordinal
                        && listMessage[i].type != Message.Type.FIRST_MESSAGE.ordinal
                    ) {
                        listMessage[i] = message
                        break
                    }
                }
            } else {
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
