package com.example.ae_chat_sdk.acti.boxchat

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
import de.hdodenhof.circleimageview.CircleImageView
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Suppress("DEPRECATION")
class BoxChatActivity : AppCompatActivity() {

    private lateinit var context: Context

    lateinit var rvMessage: RecyclerView

    lateinit var btnBack: ImageButton
    lateinit var btnNotification: ImageButton
    lateinit var btnSendMessage: ImageButton

    lateinit var ivAvatar: CircleImageView

    lateinit var tvUsername: TextView
    lateinit var etInputMessage: EditText

    lateinit var scrollView : ScrollView



    lateinit var groupId: String
    lateinit var messageId: String

    val webSocketListener: WebSocketListener = WebSocketListener()

    companion object {
        var messageAdapter: MessageAdapter? = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box_chat)

        context = applicationContext

        val extras = intent.extras
        groupId = extras?.getString("GroupId").toString()
        setBlur()
        init()
        setOnClickListener()

        setAvartar()

        val intent: Intent = intent
        groupId = intent.getStringExtra("GroupId").toString()
        webSocketListener.receiveMessage(HomeActivity.webSocket, groupId.toString())

        messageId = intent.getStringExtra("lastmessage").toString()
        if (messageId!=null){
            webSocketListener.seenMessage(HomeActivity.webSocket,messageId)
        }

//        val list:ArrayList<String> =ArrayList()
//        listMessage.add(Message("1", Message.Type.FIRST_MESSAGE,Message.Status.RECEIVED,Message.GroupType.PRIVATE,"fdfsfds", "", Date(2023,4,6),Date(2023,4,6),"TanPhat","fsfsdfad","","",list,list, ""))

        messageAdapter = MessageAdapter(context,groupId)
        rvMessage.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvMessage.adapter = messageAdapter

        messageAdapter!!.notifyDataSetChanged()
//            findViewById<RecyclerView>(R.id.rvMessage).apply{
//            adapter = MessageAdapter(listMessage, context)
//        }
        //adjustScrollView(scrollView, rvMessage)
    }
    fun adjustScrollView(scrollview: ScrollView, view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
                val bottomMargin = keypadHeight
                val layoutParams = scrollview.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.bottomMargin = bottomMargin
                scrollview.layoutParams = layoutParams
            } else {
                val layoutParams = scrollview.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.bottomMargin = 0
                scrollview.layoutParams = layoutParams
            }
        }
    }

    private fun setAvartar() {
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

        val radius: Float = 25f

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClickListener() {
        btnBack.setOnClickListener(
            View.OnClickListener {
                //super.onDestroy()
                messageAdapter = null
                finish()

            }
        )

        btnNotification.setOnClickListener(
            View.OnClickListener {
            }
        )

        btnSendMessage.setOnClickListener(
            View.OnClickListener {
                if (etInputMessage.text.trim() != "") {
                    val myUser: User = AppStorage.getInstance(context).getUserLocal()
                    val message: Message = Message()
                    message.type = Message.Type.TEXT.ordinal
                    message.groupType = Message.GroupType.PUBLIC.ordinal
                    message.message = etInputMessage.text.toString()
                    message.groupId = groupId
                    message.status = Message.Status.SENDING.ordinal
                    message.senderUin = myUser.userId
                    message.senderAvatar = myUser.avatar.toString()
                    message.senderName = myUser.userName
                    val currentDate = LocalDate.now()
                    val currentTime = LocalTime.now()
                    val dateTime = LocalDateTime.of(currentDate, currentTime)
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val formattedDateTime = dateTime.format(formatter)
                    message.createdAt = formattedDateTime
                    messageAdapter!!.addMessageSeeding(message)
                    webSocketListener.sendMessage(HomeActivity.webSocket, message)
                    //rvMessage.smoothScrollToPosition(messageAdapter.itemCount - 1)
                    etInputMessage.text.clear()
                }
            }
        )
    }

    fun init() {
        btnBack = findViewById(R.id.ibBack)
        btnNotification = findViewById(R.id.ibNotification)
        btnSendMessage = findViewById(R.id.ibSendMessage)
        rvMessage = findViewById(R.id.rvMessage)
        ivAvatar = findViewById(R.id.ivAvatar)
        tvUsername = findViewById(R.id.tvUsername)
        etInputMessage = findViewById(R.id.etInputMessage)

        //scrollView = findViewById(R.id.scrollView)
    }
//    fun addMessage(message: Message) {
//        messageAdapter.addItem(message)
//        rvMessage.scrollToPosition(messageAdapter.itemCount - 1)
//    }
}
