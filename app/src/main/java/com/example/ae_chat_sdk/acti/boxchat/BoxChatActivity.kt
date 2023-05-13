package com.example.ae_chat_sdk.acti.boxchat

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

@Suppress("DEPRECATION")
class BoxChatActivity : AppCompatActivity() {


    lateinit var rvMessage: RecyclerView
    lateinit var context: Context

    var isBottom : Boolean = false

    lateinit var btnBack: ImageButton

    lateinit var btnSendMessage: ImageButton
    lateinit var ibImage: ImageButton
    lateinit var ibMic: ImageButton
    lateinit var ibExpand: ImageButton
    lateinit var ivAvatar: CircleImageView
    lateinit var tvUsername: TextView
    lateinit var etInputMessage: EditText

    lateinit var tvActiveStatus: TextView
    lateinit var ivOnline: ImageView


    lateinit var messageId: String
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    private var IMAGE_PATH = ""

    lateinit var progressBar: ProgressBar
    var onMic = false

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false

    private val webSocketListener: WebSocketListener = WebSocketListener()

    companion object {
        var messageAdapter: MessageAdapter? = null
        var groupId: String? = null
    }


    @RequiresApi(VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideSystemUI()


        setContentView(R.layout.activity_box_chat)
        context = applicationContext

        BlurUtils.setBlur(window, listOf(findViewById(R.id.blurView)), context)

        init()
        setProfileReceiver()

        if (groupId==null){
            groupId = intent.getStringExtra("GroupId")
        }
        messageId = intent.getStringExtra("lastmessage").toString()
        if(groupId != null){
            webSocketListener.receiveMessage(HomeActivity.webSocket, groupId!!)
            if (messageId!=null){
                webSocketListener.seenMessage(HomeActivity.webSocket, messageId)
            }
            showMessage()
        }else{
            messageAdapter = MessageAdapter(context,this,"")
        }

        etInputMessage.requestFocus()
    }

    @RequiresApi(VERSION_CODES.R)
    private fun hideSystemUI() {
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        WindowInsetsControllerCompat(
            window,
            window.decorView.findViewById(android.R.id.content)
        ).let { controller ->
            controller.hide(
                WindowInsetsCompat.Type.navigationBars()
            )

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }


    public fun reCreateActivity() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        recreate();
    }

    @RequiresApi(VERSION_CODES.O)
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
        progressBar = findViewById(R.id.progressBar)



        setOnClickListener()
        setOnFocusChangeListener()
        addTextChangedListener()
    }

    public fun showMessage() {
        messageAdapter = MessageAdapter(context,this, groupId!!)
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvMessage.layoutManager = linearLayoutManager
        rvMessage.adapter = messageAdapter
        messageAdapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                if (!isBottom) {
                    rvMessage.scrollToPosition(messageAdapter!!.itemCount - 1)
                    isBottom = true
                }
            }
        })
    }

    private fun addTextChangedListener() {
        etInputMessage.addTextChangedListener {
            it?.let { string ->
                if (string.toString().trim() != "") {
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
        if (imageUrl != "") {
            Glide.with(context).load(imageUrl).placeholder(R.drawable.avatardefault)
                .error(R.drawable.avatardefault).into(ivAvatar)
        }
        val username = intent.getStringExtra("username")
        val status = intent.getIntExtra("status", 2)
        val lastTimeOnline = intent.getLongExtra("lastTimeOnline", 0)
        tvUsername.text = username
        val dateValue = Date(lastTimeOnline)
        Log.e("TIMEON", dateValue.toString())
        if (status == 2) {
            Toast.makeText(context, "Mất kết nối! Dong 70 trong box chat", Toast.LENGTH_SHORT).show()
        } else {
            if (status == UserOnlineStatus.UserStatus.ONLINE.ordinal) {
                tvActiveStatus.text = "Đang hoạt động"
                ivOnline.visibility = View.VISIBLE
            }else{
                tvActiveStatus.text = DateTimeUtil().getElapsedTimeInWords(dateValue)
                ivOnline.visibility = View.GONE
            }
        }

    }

    @RequiresApi(VERSION_CODES.O)
    private fun setOnClickListener() {
        btnBack.setOnClickListener {
            messageAdapter = null
            finish()
            groupId = null

//            val intent: Intent = Intent(context, HomeActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(intent)
        }

        btnSendMessage.setOnClickListener {
            val myUser: User = AppStorage.getInstance(context).getUserLocal()
            if (etInputMessage.text.trim() != "" && groupId != null) {
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
            }else if (etInputMessage.text.trim() != "" && groupId == null){
                val userIdMe = RestClient().getUserId()
                val userId = intent.getStringExtra("userId")
                val newList: List<String> = listOf(userIdMe, userId.toString())
                val message = Message()
                message.type = Message.Type.TEXT.ordinal
                message.groupType = Message.GroupType.PUBLIC.ordinal
                message.message = etInputMessage.text.toString()
                message.status = Message.Status.SENDING.ordinal
                message.senderUin = myUser.userId
                message.senderAvatar = myUser.avatar.toString()
                message.senderName = myUser.userName
                //messageAdapter!!.addMessageSeeding(message)
                message.createdAt = Date()
                WebSocketListener.createGroup(newList,Message.GroupType.PUBLIC.ordinal,null,userIdMe,userIdMe,message)
                etInputMessage.text.clear()
            }
        }

        ibExpand.setOnClickListener {
            showOptions(true)
        }

        ibImage.setOnClickListener {
            openGallery()
        }

        ibMic.setOnClickListener {
            showBottomSheetAudioRecord()
        }
    }

    @RequiresApi(VERSION_CODES.O)
    private fun showBottomSheetAudioRecord() {
        val bottomSheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_bottom_sheet_audio_record, null)
        bottomSheet.setContentView(view)


        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions, 0)
        }



        bottomSheet.window?.decorView?.let {
            WindowInsetsControllerCompat(
                bottomSheet.window!!,
                it.findViewById(android.R.id.content)
            ).let { controller ->
                controller.hide(
                    WindowInsetsCompat.Type.navigationBars()
                )
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        val btnMic: ImageButton = view.findViewById(R.id.ibMicRecord)
        val btnDelete: ImageButton = view.findViewById(R.id.ibDelete)
        val btnSendAudio: ImageButton = view.findViewById(R.id.ibSend)
        val lotMic: LottieAnimationView = view.findViewById(R.id.animation_view)


        lotMic.setOnClickListener {
            onMic = false
            btnSendAudio.visibility = View.VISIBLE
            btnMic.visibility = View.VISIBLE
            lotMic.visibility = View.GONE
            stopRecording()
        }
        btnMic.setOnClickListener {
            onMic = true
            lotMic.visibility = View.VISIBLE
            btnMic.visibility = View.GONE
            btnDelete.visibility = View.VISIBLE
            btnSendAudio.visibility = View.GONE

            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val permissions = arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                ActivityCompat.requestPermissions(this, permissions, 0)
            }

            startRecording()


        }

        btnSendAudio.setOnClickListener {
            val file = File(output)
            val inputStream = FileInputStream(file)
            val fileSize = file.length().toInt()
            val byteArray = ByteArray(fileSize)
            inputStream.read(byteArray)
            val base64String = Base64.getEncoder().encodeToString(byteArray)
            val myUser: User = AppStorage.getInstance(context).getUserLocal()
            val message = Message()
            message.type = Message.Type.AUDIO.ordinal
            message.groupType = Message.GroupType.PUBLIC.ordinal
            message.message = ""
            message.groupId = groupId
            message.status = Message.Status.SENDING.ordinal
            message.senderUin = myUser.userId
            message.senderAvatar = myUser.avatar.toString()
            message.senderName = myUser.userName
            message.createdAt = Date()
            // Send media message
            WebSocketListener.sendMediaMessage(message, base64String)
            inputStream.close()
            bottomSheet.hide()
        }

        btnDelete.setOnClickListener {
            if (onMic) {
                stopRecording()
            }
            output = null
            btnDelete.visibility = View.GONE
            btnSendAudio.visibility = View.GONE
            btnMic.visibility = View.VISIBLE
            lotMic.visibility = View.GONE

            onMic = false
        }

        bottomSheet.show()
    }

    @SuppressLint("RestrictedApi")
    private fun startRecording() {
        output = "${externalCacheDir?.absolutePath}" + "/recording.mp3"
        mediaRecorder = if (VERSION.SDK_INT >= VERSION_CODES.S) {
            MediaRecorder(applicationContext)
        } else {
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(output)
            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
            start()
        }
        state = true
    }

    private fun stopRecording() {
        if (state) {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            state = false
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    @RequiresApi(VERSION_CODES.O)
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
                    val byteArray = ByteArray(fileSize)
                    inputStream.read(byteArray)
                    val base64String = Base64.getEncoder().encodeToString(byteArray)
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
                    messageAdapter!!.addMessageSeeding(message)
                    // Send media message
                    WebSocketListener.sendMediaMessage(message, base64String)
                    inputStream.close()
                }
            }
        }
    }
}
