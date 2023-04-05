package com.example.ae_chat_sdk.acti.boxchat

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ae_chat_sdk.MainActivity
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.adapter.MessageAdapter
import com.example.ae_chat_sdk.data.api.service.WebSocketListener
import com.example.ae_chat_sdk.data.model.Message
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


        val webSocketListener: WebSocketListener = WebSocketListener()
        webSocketListener.receiveMessage(MainActivity.webSocket)

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
    }
}