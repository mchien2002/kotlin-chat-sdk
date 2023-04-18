package com.example.ae_chat_sdk.acti.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.MainActivity
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.adapter.OnstreamAdapter
import com.example.ae_chat_sdk.acti.adapter.RecentAdapter
import com.example.ae_chat_sdk.acti.intro.LoginActivity
import com.example.ae_chat_sdk.acti.profile.ProfileActivity
import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.api.reponsitory.UserRepository
import com.example.ae_chat_sdk.data.api.service.WebSocketListener
import com.example.ae_chat_sdk.data.model.ApiResponse
import com.example.ae_chat_sdk.data.model.Message
import com.example.ae_chat_sdk.data.model.User
import com.example.ae_chat_sdk.data.socket.SocketConstant
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity(), View.OnClickListener {

    // Context
    lateinit var context: Context

    // RecyclerView Message Home
    lateinit var rvOnstream: RecyclerView
    lateinit var rvRecent: RecyclerView
    lateinit var rvListContact: RecyclerView

    lateinit var btnLogOut: Button


    lateinit var ctLayoutBottomSheetHome: ConstraintLayout

    lateinit var rLayoutMessageHome: RelativeLayout

    // BottomSheet
    lateinit var bottomSheetHomeBehavior: BottomSheetBehavior<View>

    // Name Page
    lateinit var tvPagename: TextView
    lateinit var tvUserName: TextView
    lateinit var tvEmail: TextView

    lateinit var etSearch: EditText

    lateinit var avatarUser: CircleImageView


    var listContact: Boolean = false

    companion object {
        lateinit var recentAdapter: RecentAdapter
        lateinit var webSocket: WebSocket
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        connectSocket()

        setBlur()

        // set data for list on Stream
        searchUser("")


        context = applicationContext

        init()
        setButtonOnClickListener()
        renderDataRecyclerView()
        setBottomSheetBehaviorHome()
        onStart()

//        val gson = Gson()
//        val type = object : TypeToken<User>() {}.type
//        val appStorage = AppStorage.getInstance(context)
//        val userString: String = appStorage.getData("User", "").toString()
//        val user = gson.fromJson<User>(userString, type)
        val myUser: User = AppStorage.getInstance(context).getUserLocal()
        val webSocketListener = WebSocketListener()
        webSocketListener.getListGroup(webSocket, myUser.userId)


        etSearch.addTextChangedListener {
            it?.let { string ->
                searchUser(etSearch.text.toString())
            }
        }
        setData()
    }


    private fun connectSocket() {
        val client = OkHttpClient()

        val request: Request = Request.Builder().url(SocketConstant.URL).build()
        val webSocketListener = WebSocketListener()
        val ws: WebSocket = client.newWebSocket(request, webSocketListener)

        webSocket = ws

    }

    private fun setBlur() {
        val radius: Float = 20f;

        val decorView: View = window.decorView
        // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        val rootView: ViewGroup = decorView.findViewById(android.R.id.content)

        // Optional:
        // Set drawable to draw in the beginning of each blurred frame.
        // Can be used in case your layout has a lot of transparent space and your content
        // gets a too low alpha value after blur is applied.
        val windowBackground: Drawable = decorView.background


        findViewById<BlurView>(R.id.blurView).setupWith(rootView)
            .setFrameClearDrawable(windowBackground).setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius).setBlurAutoUpdate(true)
        findViewById<BlurView>(R.id.blurViewBottomSheet).setupWith(rootView)
            .setFrameClearDrawable(windowBackground).setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius).setBlurAutoUpdate(true)
    }

    override fun onStart() {
        super.onStart()
        setData()
    }

    private fun searchUser(searchText: String) {
        val token = RestClient().getToken()
        val call = UserRepository().searchUser(token, searchText)
        var userList: List<User>
        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(context, "Không thể gửi mã xác thực!", Toast.LENGTH_SHORT).show()

            }

            override fun onResponse(
                call: Call<ApiResponse>, response: Response<ApiResponse>
            ) {
                if (response.code() == 403) {
                    setStartLoginActivity()
                    val appStorage = context?.let { AppStorage.getInstance(it) }
                    val userString = appStorage?.clearData()
                } else if (response.code() == 200) {
                    val gson = Gson()
                    val type = object : TypeToken<List<User>>() {}.type
                    val user = gson.fromJson<List<User>>(gson.toJson(response.body()?.data), type)
                    if (user != null) {
                        val listUserId: List<User> = user
                        rvOnstream.layoutManager =
                            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                        rvOnstream.adapter = OnstreamAdapter(listUserId as List<User>, context)
                    }
                }

            }
        })

    }

    private fun setButtonOnClickListener() {
        findViewById<MaterialButton>(R.id.mbListContact).setOnClickListener(View.OnClickListener {
            listContact = true
            bottomSheetHomeBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        })

        findViewById<CircleImageView>(R.id.ivAvatar).setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent(context, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        })
    }

    private fun init() {
        ctLayoutBottomSheetHome = findViewById(R.id.ctBottomSheetHome)
        // rLayoutBottomSheetChangeAvatar = findViewById(R.id.rlBottomSheetChangeAvatar)
        rLayoutMessageHome = findViewById(R.id.rlMessageHome)

        rvOnstream = findViewById(R.id.rvHorizonalOnstream)
        rvRecent = findViewById(R.id.rvVerticalRecent)
        rvListContact = findViewById(R.id.rvHorizonalContact)

        bottomSheetHomeBehavior = BottomSheetBehavior.from(ctLayoutBottomSheetHome)

        tvPagename = findViewById(R.id.tvPageName)

        etSearch = findViewById(R.id.etInputSearch)

        avatarUser = findViewById(R.id.ivAvatar)

        tvUserName = findViewById(R.id.tvUsername)
        tvEmail = findViewById(R.id.tvEmail)

        btnLogOut = findViewById(R.id.mbLogOut)
        btnLogOut.setOnClickListener(this)
    }

    private fun setBottomSheetBehaviorHome() {
        bottomSheetHomeBehavior.apply {
            this.state = BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheetHomeBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {

                        findViewById<RelativeLayout>(R.id.rlMenuOption).animate().alpha(0F)
                            .setDuration(500).startDelay = 0
                        findViewById<RelativeLayout>(R.id.rlMessageHome).animate().alpha(1F)
                            .setDuration(100).startDelay = 0

                        if (!listContact) {
                            tvPagename.setTextColor(Color.parseColor("#FFB0294B"))
                            tvPagename.text = "Username"
                            findViewById<RelativeLayout>(R.id.rlHome).visibility = View.VISIBLE
                            findViewById<RelativeLayout>(R.id.rlListContact).visibility = View.GONE
                        } else {
                            tvPagename.setTextColor(Color.parseColor("#FF400012"))
                            tvPagename.text = "Danh sách liên hệ"
                            findViewById<RelativeLayout>(R.id.rlHome).visibility = View.GONE
                            findViewById<RelativeLayout>(R.id.rlListContact).visibility =
                                View.VISIBLE
                        }
                        tvPagename.visibility = View.VISIBLE
                        listContact = false
                    }
                    else -> {
                        tvPagename.visibility = View.INVISIBLE
                        findViewById<RelativeLayout>(R.id.rlHome).visibility = View.VISIBLE
                        findViewById<RelativeLayout>(R.id.rlListContact).visibility = View.GONE
                        findViewById<RelativeLayout>(R.id.rlMenuOption).animate().alpha(1F)
                            .setDuration(500).startDelay = 0
                        findViewById<RelativeLayout>(R.id.rlMessageHome).animate().alpha(0F)
                            .setDuration(100).startDelay = 0
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun setData() {
        val appStorage = AppStorage.getInstance(context)

        val myUser: User = AppStorage.getInstance(context).getUserLocal()
        tvUserName.text = myUser.userName.toString()
        tvEmail.text = myUser.email.toString()

        val imgLocal = appStorage?.getData("avatar", "").toString()
        if (imgLocal.length > 1) {
            Glide.with(this).load(imgLocal).into(avatarUser)
        } else if (myUser.avatar == null) {
            val imageUrl =
                "https://3.bp.blogspot.com/-SMNLs_5XfVo/VHvNUx8dWZI/AAAAAAAAQnY/NWdkO4JPE_M/s1600/Avatar-Facebook-an-danh-trang-4.jpg"
            Glide.with(this).load(imageUrl).into(avatarUser)
        } else {
            val imageUrl = ApiConstant.URL_AVATAR + myUser.avatar
            Glide.with(this).load(imageUrl).into(avatarUser)
        }
    }

    override fun onClick(view: View) {
        when (view?.id) {
            R.id.mbLogOut -> {
                setStartLoginActivity()
                val appStorage = context?.let { AppStorage.getInstance(it) }
                val userString = appStorage?.clearData()
            }
            R.id.ivAvatar -> {
                Toast.makeText(applicationContext, "this is toast message 1", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun renderDataRecyclerView() {
        recentAdapter = RecentAdapter(context)
        rvRecent.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvRecent.adapter = recentAdapter
        recentAdapter.notifyDataSetChanged()
    }

    private fun setStartLoginActivity() {
        val intent: Intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent);
    }
}

