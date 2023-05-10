package com.example.ae_chat_sdk.acti.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
import com.example.ae_chat_sdk.data.model.User
import com.example.ae_chat_sdk.data.socket.SocketConstant
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.example.ae_chat_sdk.utils.BlurUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import eightbitlab.com.blurview.BlurView
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {

    // Context
    lateinit var context: Context

    // RecyclerView Message Home
    lateinit var rvOnstream: RecyclerView
    lateinit var rvRecent: RecyclerView
    lateinit var rvListContact: RecyclerView

    lateinit var btnLogOut: Button
    lateinit var swState: SwitchCompat

    lateinit var ctLayoutBottomSheetHome: ConstraintLayout
    lateinit var rLayoutMessageHome: RelativeLayout
    lateinit var rLayoutOption: RelativeLayout
    lateinit var rLayoutOnStream: RelativeLayout

    // BottomSheet
    lateinit var bottomSheetHomeBehavior: BottomSheetBehavior<View>

    // Name Page
    lateinit var tvPagename: TextView
    lateinit var tvUserName: TextView
    lateinit var tvEmail: TextView

    lateinit var etSearch: EditText

    lateinit var avatarUser: CircleImageView

    lateinit var progressBar: ProgressBar

    var listContact: Boolean = false
    var draggingUp: Boolean = false

    companion object {
        lateinit var recentAdapter: RecentAdapter
        lateinit var webSocket: WebSocket
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        context = applicationContext

        BlurUtils.setBlur(
            this.window, listOf(
                findViewById<BlurView>(R.id.blurView),
                findViewById<BlurView>(R.id.blurViewBottomSheet)
            ), context
        )

        connectSocket()
        init()
        setButtonOnClickListener()
        searchUser("")
        renderDataRecyclerView()
        setBottomSheetBehaviorHome()
        setUserData()
    }

    private fun connectSocket() {
        val client = OkHttpClient()

        val request: Request = Request.Builder().url(SocketConstant.URL).build()
        val webSocketListener = WebSocketListener()
        val ws: WebSocket = client.newWebSocket(request, webSocketListener)

        webSocket = ws

        val myUser: User = AppStorage.getInstance(context).getUserLocal()
        webSocketListener.getListGroup(webSocket, myUser.userId)

    }

    private fun searchUser(searchText: String) {
        val token = RestClient().getToken()
        val call = UserRepository().searchUser(token, searchText)
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
        // Open list Contact
        findViewById<MaterialButton>(R.id.mbListContact).setOnClickListener(View.OnClickListener {
            listContact = true
            bottomSheetHomeBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        })

        // Open Profile
        avatarUser.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        })

        // Log out
        btnLogOut.setOnClickListener(View.OnClickListener {
            setStartLoginActivity()
            val client = OkHttpClient()

            val request: Request = Request.Builder().url(SocketConstant.URL).build()
            val appStorage = context?.let { AppStorage.getInstance(it) }
            appStorage?.clearData()
            WebSocketListener.exitSocket()
            finish()
        })

        // Update state
        swState.setOnCheckedChangeListener(OnCheckedChangeListener { compoundButton, b ->
            if (swState.isChecked) {
                rLayoutOnStream.visibility = View.VISIBLE
            } else {
                rLayoutOnStream.visibility = View.GONE
            }
        })
    }

    private fun init() {
        ctLayoutBottomSheetHome = findViewById(R.id.ctBottomSheetHome)
        rLayoutMessageHome = findViewById(R.id.rlMessageHome)
        rLayoutOption = findViewById(R.id.rlMenuOption)
        rLayoutOnStream = findViewById(R.id.rlOnstream)
        rvOnstream = findViewById(R.id.rvHorizonalOnstream)
        rvRecent = findViewById(R.id.rvVerticalRecent)
        rvListContact = findViewById(R.id.rvHorizonalContact)

        bottomSheetHomeBehavior = BottomSheetBehavior.from(ctLayoutBottomSheetHome)

        etSearch = findViewById(R.id.etInputSearch)

        etSearch.addTextChangedListener {
            it?.let { string ->
                searchUser(etSearch.text.toString())
            }
        }

        avatarUser = findViewById(R.id.ivAvatar)

        tvUserName = findViewById(R.id.tvUsername)
        tvEmail = findViewById(R.id.tvEmail)
        tvPagename = findViewById(R.id.tvPageName)

        btnLogOut = findViewById(R.id.mbLogOut)
        swState = findViewById(R.id.switchStatus)

        progressBar = findViewById(R.id.progressBar)
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
                        draggingUp = false
                        if (listContact) {
                            listContact = false
                            tvPagename.setTextColor(Color.parseColor("#FF400012"))
                            tvPagename.text = "Danh sách liên hệ"
                            findViewById<RelativeLayout>(R.id.rlHome).visibility = View.GONE
                            findViewById<RelativeLayout>(R.id.rlListContact).visibility =
                                View.VISIBLE
                        }
                        if(tvPagename.alpha==0F){
                            tvPagename.animate().alpha(1F).setDuration(500).setListener(null)
                        }

                        rLayoutOption.visibility = View.GONE
                        rLayoutMessageHome.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        draggingUp = true
                        rLayoutMessageHome.visibility = View.GONE
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            tvPagename.visibility = View.GONE
//
//                        }, 100)
                        if(tvPagename.alpha==1F){
                            tvPagename.animate().alpha(0F).setDuration(500).setListener(null)
                        }


                        if (!listContact) {
                            tvPagename.setTextColor(Color.parseColor("#FFB0294B"))
                            tvPagename.text = "Username"
                            findViewById<RelativeLayout>(R.id.rlHome).visibility = View.VISIBLE
                            findViewById<RelativeLayout>(R.id.rlListContact).visibility = View.GONE
                        }
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        tvPagename.visibility = View.VISIBLE

                        if (draggingUp) {
                            draggingUp = false
                            rLayoutMessageHome.visibility = View.VISIBLE
                            tvPagename.animate().alpha(1F).setDuration(500).setListener(null)

                        } else {
                            draggingUp = true
                            rLayoutOption.visibility = View.VISIBLE
                            tvPagename.animate().alpha(0F).setDuration(500).setListener(null)


                            // Auto hide keyboard
                            if (currentFocus != null) {
                                val inputMethodManager =
                                    getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                                inputMethodManager.hideSoftInputFromWindow(
                                    currentFocus!!.windowToken,
                                    0
                                )
                            }
                        }


                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun setUserData() {
        val appStorage = AppStorage.getInstance(context)
        val myUser: User = AppStorage.getInstance(context).getUserLocal()
        tvUserName.text = myUser.userName.toString()
        tvEmail.text = myUser.email.toString()

        val imgLocal = appStorage?.getData("avatar", "").toString()
        if (imgLocal.length > 1) {
            Glide.with(this).load(imgLocal).into(avatarUser)
        } else if (myUser.avatar != null) {
            val imageUrl = ApiConstant.URL_IMAGE + myUser.avatar
            Glide.with(this).load(imageUrl).into(avatarUser)
        }
    }

    private fun renderDataRecyclerView() {
        progressBar.visibility = View.VISIBLE
        recentAdapter = RecentAdapter(context)
        rvRecent.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvRecent.adapter = recentAdapter
        recentAdapter.notifyDataSetChanged()
//        progressBar.visibility = View.GONE
        Handler(Looper.getMainLooper()).postDelayed({
            progressBar.visibility = View.GONE
        }, 3000)
    }

    private fun setStartLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent);
    }
}

