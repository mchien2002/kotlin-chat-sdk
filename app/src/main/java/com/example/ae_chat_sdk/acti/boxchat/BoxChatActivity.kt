package com.example.ae_chat_sdk.acti.boxchat

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.adapter.MessageAdapter
import com.example.ae_chat_sdk.acti.home.HomeActivity
import com.example.ae_chat_sdk.data.api.service.WebSocketListener
import com.example.ae_chat_sdk.data.model.Message
import com.example.ae_chat_sdk.data.model.User
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.example.ae_chat_sdk.utils.BlurUtils
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

@Suppress("DEPRECATION")
class BoxChatActivity : AppCompatActivity() {

    private lateinit var context: Context

    lateinit var rvMessage: RecyclerView

    lateinit var btnBack: ImageButton
    lateinit var btnNotification: ImageButton
    lateinit var btnSendMessage: ImageButton
    lateinit var ibImage: ImageButton
    lateinit var ibMic: ImageButton
    lateinit var ibExpand: ImageButton
    lateinit var ivAvatar: CircleImageView
    lateinit var tvUsername: TextView
    lateinit var etInputMessage: EditText

    lateinit var groupId: String
    lateinit var messageId: String

    private val webSocketListener: WebSocketListener = WebSocketListener()

    companion object {
        var messageAdapter: MessageAdapter? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box_chat)
        context = applicationContext

        BlurUtils.setBlur(window, listOf(findViewById(R.id.blurView)), context)

        init()
        setProfileReceiver()

        groupId = intent.getStringExtra("GroupId").toString()
        webSocketListener.receiveMessage(HomeActivity.webSocket, groupId)
        messageId = intent.getStringExtra("lastmessage").toString()
        if (messageId != null) {
            webSocketListener.seenMessage(HomeActivity.webSocket, messageId)
        }
        messageAdapter = MessageAdapter(context, groupId)
        rvMessage.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvMessage.adapter = messageAdapter
        messageAdapter!!.notifyDataSetChanged()
    }

    private fun init() {
        btnBack = findViewById(R.id.ibBack)
        btnNotification = findViewById(R.id.ibNotification)
        btnSendMessage = findViewById(R.id.ibSendMessage)
        rvMessage = findViewById(R.id.rvMessage)
        ibImage = findViewById(R.id.ibImage)
        ibMic = findViewById(R.id.ibMic)
        ibExpand = findViewById(R.id.ibExpand)
        ivAvatar = findViewById(R.id.ivAvatar)
        tvUsername = findViewById(R.id.tvUsername)
        etInputMessage = findViewById(R.id.etInputMessage)
        setOnClickListener()
        setOnFocusChangeListener()
        addTextChangedListener()
    }

    private fun addTextChangedListener() {
        etInputMessage.addTextChangedListener {
            it?.let { string->
                if(string.toString().trim()!=""){
                    showOptions(false)
                }
            }
        }
    }

    private fun setOnFocusChangeListener() {
        etInputMessage.setOnFocusChangeListener { _, hasFocus ->
            showOptions(!hasFocus)
        }
    }

    private fun showOptions(state: Boolean) {
        if (state) {
            ibExpand.visibility = View.GONE
            ibMic.visibility = View.VISIBLE
            ibImage.visibility = View.VISIBLE
        } else {
            ibExpand.visibility = View.VISIBLE
            ibMic.visibility = View.GONE
            ibImage.visibility = View.GONE
        }
    }

    private fun setProfileReceiver() {
        val imageUrl = intent.getStringExtra("avatar")
        Glide.with(context).load(imageUrl).into(ivAvatar)
        val username = intent.getStringExtra("username")
        tvUsername.text = username
    }


    private fun setOnClickListener() {
        btnBack.setOnClickListener {
            messageAdapter = null
            finish()
        }

        btnNotification.setOnClickListener {
        }

        btnSendMessage.setOnClickListener {
            if (etInputMessage.text.trim() != "") {
                val myUser: User = AppStorage.getInstance(context).getUserLocal()
                val message = Message()
                message.type = Message.Type.TEXT.ordinal
                message.groupType = Message.GroupType.PUBLIC.ordinal
                message.message = etInputMessage.text.toString()
                message.groupId = groupId
                message.status = Message.Status.SENDING.ordinal
                message.senderUin = myUser.userId
                message.senderAvatar = myUser.avatar.toString()
                message.senderName = myUser.userName
                message.createdAt = Date()
                messageAdapter!!.addMessageSeeding(message)
                webSocketListener.sendMessage(HomeActivity.webSocket, message)
                etInputMessage.text.clear()
            }
        }

        ibExpand.setOnClickListener {
            showOptions(true)
        }
    }




}
