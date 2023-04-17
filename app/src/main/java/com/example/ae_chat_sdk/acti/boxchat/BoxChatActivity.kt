package com.example.ae_chat_sdk.acti.boxchat

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.MainActivity
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.adapter.MessageAdapter
import com.example.ae_chat_sdk.data.api.service.WebSocketListener
import com.example.ae_chat_sdk.data.model.Message
import de.hdodenhof.circleimageview.CircleImageView
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import java.util.*

@Suppress("DEPRECATION")
class BoxChatActivity: AppCompatActivity() {

    private lateinit var context: Context

    lateinit var rvMessage: RecyclerView

    lateinit var btnBack: ImageButton
    lateinit var btnNotification: ImageButton
    lateinit var btnSendMessage: ImageButton

    lateinit var ivAvatar : CircleImageView

    lateinit var tvUsername : TextView
    lateinit var etInputMessage : EditText

    lateinit var groupId : String

    val webSocketListener: WebSocketListener = WebSocketListener()

    companion object {
        lateinit var messageAdapter: MessageAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box_chat)

        context = applicationContext



        setBlur()
        init()
        setOnClickListener()

        setAvartar()

        val extras = intent.extras
        groupId = extras?.getString("GroupId").toString()

        webSocketListener.receiveMessage(MainActivity.webSocket,groupId.toString())

//        val list:ArrayList<String> =ArrayList()
//        listMessage.add(Message("1", Message.Type.FIRST_MESSAGE,Message.Status.RECEIVED,Message.GroupType.PRIVATE,"fdfsfds", "", Date(2023,4,6),Date(2023,4,6),"TanPhat","fsfsdfad","","",list,list, ""))

        messageAdapter =MessageAdapter( context)
        rvMessage.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvMessage.adapter = messageAdapter

        messageAdapter.notifyDataSetChanged()
//            findViewById<RecyclerView>(R.id.rvMessage).apply{
//            adapter = MessageAdapter(listMessage, context)
//        }
    }

    private fun setAvartar(){
        val extras = intent.extras
        val imageUrl = extras?.getString("avatar")
        Glide.with(context).load(imageUrl).into(ivAvatar)
        val username = extras?.getString("username")
        tvUsername.text = username
    }

    private fun setBlur() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        val radius: Float = 25f;

        val decorView: View = window.decorView
        // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        val rootView: ViewGroup = decorView.findViewById(android.R.id.content)

        // Optional:
        // Set drawable to draw in the beginning of each blurred frame.
        // Can be used in case your layout has a lot of transparent space and your content
        // gets a too low alpha value after blur is applied.
        val windowBackground: Drawable = decorView.background


        findViewById<BlurView>(R.id.blurView).setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
    }

    private fun setOnClickListener() {
        btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        btnNotification.setOnClickListener(View.OnClickListener {

        })

        btnSendMessage.setOnClickListener(View.OnClickListener {

        })
    }

    fun init() {
        btnBack = findViewById(R.id.ibBack)
        btnNotification = findViewById(R.id.ibNotification)
        btnSendMessage = findViewById(R.id.ibSendMessage)
        rvMessage = findViewById(R.id.rvMessage)
        ivAvatar = findViewById(R.id.ivAvatar)
        tvUsername = findViewById(R.id.tvUsername)
        etInputMessage = findViewById(R.id.etInputMessage)

        btnSendMessage.setOnClickListener(View.OnClickListener {
            if (etInputMessage.text.trim() != "" && etInputMessage.text.trim() != null){
                val message : Message = Message()
                message.type = Message.Type.TEXT.ordinal
                message.groupType = Message.GroupType.PUBLIC.ordinal
                message.message = etInputMessage.text.toString()
                message.groupId = groupId
                webSocketListener.sendMessage(MainActivity.webSocket, message)
            }
            Log.e("CHECKK","ooooooooooooo|")

        })
    }
}