package com.example.ae_chat_sdk.acti.boxchat

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.adapter.MessageAdapter
import com.example.ae_chat_sdk.acti.home.HomeActivity
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.api.service.WebSocketListener
import com.example.ae_chat_sdk.data.model.*
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.example.ae_chat_sdk.utils.BlurUtils
import com.example.ae_chat_sdk.utils.DateTimeUtil
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.ByteString
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.util.*

@Suppress("DEPRECATION")
class BoxChatActivity : AppCompatActivity() {

    private lateinit var context: Context

    lateinit var rvMessage: RecyclerView

    var isBottom : Boolean = false

    lateinit var btnBack: ImageButton
//    lateinit var btnNotification: ImageButton
    lateinit var btnSendMessage: ImageButton
    lateinit var ibImage: ImageButton
    lateinit var ibMic: ImageButton
    lateinit var ibExpand: ImageButton
    lateinit var ivAvatar: CircleImageView
    lateinit var tvUsername: TextView
    lateinit var etInputMessage: EditText

    lateinit var tvActiveStatus : TextView
    lateinit var ivOnline:ImageView

    lateinit var groupId: String
    lateinit var messageId: String
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    private var IMAGE_PATH = ""


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
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvMessage.layoutManager =linearLayoutManager
            rvMessage.adapter = messageAdapter
        messageAdapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                if (!isBottom) {
                    rvMessage.scrollToPosition(messageAdapter!!.itemCount - 1)
                    isBottom = true
                }
            }
        })
        etInputMessage.requestFocus()
        // Hiện bàn phím mềm nhưng éo được
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(etInputMessage, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun init() {
        btnBack = findViewById(R.id.ibBack)
//        btnNotification = findViewById(R.id.ibNotification)
        btnSendMessage = findViewById(R.id.ibSendMessage)
        rvMessage = findViewById(R.id.rvMessage)
        ibImage = findViewById(R.id.ibImage)
        ibMic = findViewById(R.id.ibMic)
        ibExpand = findViewById(R.id.ibExpand)
        tvActiveStatus = findViewById(R.id.tvActiveStatus)
        ivAvatar = findViewById(R.id.ivAvatar)
        tvUsername = findViewById(R.id.tvUsername)
        etInputMessage = findViewById(R.id.etInputMessage)
        ivOnline = findViewById(R.id.ivOnline)
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
        val status = intent.getIntExtra("status",2)
        val lastTimeOnline = intent.getLongExtra("lastTimeOnline",0)
        tvUsername.text = username
        val dateValue = Date(lastTimeOnline)
        Log.e("TIMEON",dateValue.toString())
        if(status == 2){
            Toast.makeText(context, "Mất kết nối!", Toast.LENGTH_SHORT).show()
        }else {
            if(status == UserOnlineStatus.UserStatus.ONLINE.ordinal){
                tvActiveStatus.text = "Đang hoạt động"
                ivOnline.visibility = View.VISIBLE
            }else{
                tvActiveStatus.text = DateTimeUtil().getElapsedTimeInWords(dateValue)
                ivOnline.visibility = View.GONE
            }
        }

    }


    private fun setOnClickListener() {
        btnBack.setOnClickListener {
            messageAdapter = null
            finish()
        }

//        btnNotification.setOnClickListener {
//        }

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
                rvMessage.scrollToPosition(messageAdapter!!.itemCount - 1)
            }
        }

        ibExpand.setOnClickListener {
            showOptions(true)
        }

        ibImage.setOnClickListener{
            openGallery()
        }
    }
    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    // Convert imageBitmap to byte array
                    val stream = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val imageData = stream.toByteArray()
                    // Get width and height of the image
                    val width = imageBitmap.width
                    val height = imageBitmap.height
                    // Send media message
                    //WebSocketListener.sendMediaMessage(Message(), imageData, width, height)
                }
                REQUEST_IMAGE_PICK -> {
                    val uri: Uri? = data?.data
                    val path = uri?.let { RealPathUtil.getRealPath(this, it) }
                    // Read data of the image
                    val file = File(path)
                    val inputStream = FileInputStream(file)
                    val fileSize = file.length().toInt()
                    val buffer = ByteArray(fileSize)
                    inputStream.read(buffer)
                    val byteString = ByteString.of(*buffer)
                    val myUser: User = AppStorage.getInstance(context).getUserLocal()
                    val message = Message()
                    message.type = Message.Type.IMAGE.ordinal
                    message.groupType = Message.GroupType.PUBLIC.ordinal
                    message.message = etInputMessage.text.toString()
                    message.groupId = groupId
                    message.status = Message.Status.SENDING.ordinal
                    message.senderUin = myUser.userId
                    message.senderAvatar = myUser.avatar.toString()
                    message.senderName = myUser.userName
                    message.createdAt = Date()
                    // Send media message
                    WebSocketListener.sendMediaMessage(Message(), byteString)
                    inputStream.close()
                }
            }
        }
    }
}
