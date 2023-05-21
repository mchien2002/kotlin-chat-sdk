package com.example.ae_chat_sdk.acti.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.adapter.RecentAdapter
import com.example.ae_chat_sdk.acti.adapter.SearchAdpater
import com.example.ae_chat_sdk.acti.intro.LoginActivity
import com.example.ae_chat_sdk.acti.profile.ProfileActivity
import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.api.reponsitory.UserRepository
import com.example.ae_chat_sdk.data.api.service.WebSocketListener
import com.example.ae_chat_sdk.data.model.ApiResponse
import com.example.ae_chat_sdk.data.model.MyResponse
import com.example.ae_chat_sdk.data.model.User
import com.example.ae_chat_sdk.data.model.UserState
import com.example.ae_chat_sdk.data.socket.SocketConstant
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.example.ae_chat_sdk.utils.BlurUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
    lateinit var rvSearch: RecyclerView

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


    var draggingUp: Boolean = false
    var onSearch: Boolean = false

//    appStorage.saveData("User", gson.toJson(response.body()?.data))

    companion object {
        lateinit var recentAdapter: RecentAdapter
        lateinit var webSocket: WebSocket
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // A function to hide NavigationBar
        hideSystemUI()

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
        addTextChangeListener()
        setButtonOnClickListener()
        searchUser("")
        renderDataRecyclerView()
        setBottomSheetBehaviorHome()
        setUserData()
        checkState()
        swState.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Switch đã được bật
                // Thực hiện hành động khi switch được bật
                UserRepository().updateUserState(RestClient().getToken(),RestClient().getUserId(),UserState.PRIAVTE.ordinal)
            } else {
                // Switch đã được tắt
                // Thực hiện hành động khi switch được tắt
                UserRepository().updateUserState(RestClient().getToken(),RestClient().getUserId(),UserState.PUBLIC.ordinal)

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        WindowInsetsControllerCompat(
            window,
            window.decorView.findViewById(android.R.id.content)
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
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
        progressBar.visibility = View.VISIBLE

        val call = UserRepository().searchUser(token, searchText)
        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(context, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<ApiResponse>, response: Response<ApiResponse>
            ) {
                if (response.code() == 403) {
                    setStartLoginActivity()
                    progressBar.visibility = View.GONE
                } else if (response.code() == 200) {
                    val gson = Gson()
                    val type = object : TypeToken<List<User>>() {}.type
                    val user =
                        gson.fromJson<List<User>>(gson.toJson(response.body()?.data), type)
                    if (user != null) {
                        val listUser: List<User> = user
                        rvSearch.layoutManager =
                            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        rvSearch.adapter = SearchAdpater(listUser, context)
                    }
                    progressBar.visibility = View.GONE

                }

            }
        })
    }

    private fun setButtonOnClickListener() {

        // Open Profile
        avatarUser.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            finish()
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

    }

    private fun init() {
        ctLayoutBottomSheetHome = findViewById(R.id.ctBottomSheetHome)
        rLayoutMessageHome = findViewById(R.id.rlMessageHome)
        rLayoutOption = findViewById(R.id.rlMenuOption)
        rLayoutOnStream = findViewById(R.id.rlOnstream)
        rvOnstream = findViewById(R.id.rvHorizonalOnstream)
        rvRecent = findViewById(R.id.rvVerticalRecent)
        rvSearch = findViewById(R.id.rvHorizonalSearch)

        bottomSheetHomeBehavior = BottomSheetBehavior.from(ctLayoutBottomSheetHome)

        etSearch = findViewById(R.id.etInputSearch)
        etSearch.clearFocus()

        avatarUser = findViewById(R.id.ivAvatar)

        tvUserName = findViewById(R.id.tvUsername)
        tvEmail = findViewById(R.id.tvEmail)
        tvPagename = findViewById(R.id.tvPageName)

        btnLogOut = findViewById(R.id.mbLogOut)

        val appStorage = AppStorage.getInstance(context)
        swState = findViewById(R.id.switchBlock)
        //swState.isChecked = appStorage.getSWStatus()

        progressBar = findViewById(R.id.progressBar)
    }
    private fun checkState(){
        val gson = Gson()
        val type = object : TypeToken<User>() {}.type
        val call = UserRepository().getUserProfile(RestClient().getToken(), RestClient().getUserId())
        call.enqueue(object : Callback<MyResponse> {
            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                Log.e("CCCCC", t.toString())
            }

            override fun onResponse(
                call: Call<MyResponse>, response: Response<MyResponse>
            ) {
                val userTemp =
                    gson.fromJson<User>(gson.toJson(response.body()?.data), type)
                if(userTemp != null){
                    if (userTemp.state == UserState.PUBLIC.ordinal) {
                        swState.isChecked = false
                    } else if (userTemp.state == UserState.PRIAVTE.ordinal) {
                        swState.isChecked = true
                    }
                }
            }
        })
    }

    private fun addTextChangeListener() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (etSearch.text.toString() == "") {
                    onSearch = false
                    findViewById<RelativeLayout>(R.id.rlHome).visibility = View.VISIBLE
                    findViewById<RelativeLayout>(R.id.rlListSearch).visibility = View.GONE

                    etSearch.clearFocus()
                } else {
                    onSearch = true
                    findViewById<RelativeLayout>(R.id.rlHome).visibility = View.GONE
                    findViewById<RelativeLayout>(R.id.rlListSearch).visibility = View.VISIBLE
                    searchUser(etSearch.text.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }


    override fun onBackPressed() {
        if (etSearch.visibility == View.VISIBLE) {
            onSearch = false
            findViewById<RelativeLayout>(R.id.rlHome).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.rlListSearch).visibility = View.GONE

            etSearch.clearFocus()
        } else {
            super.onBackPressed()

        }
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
                        tvPagename.animate().alpha(1F).setDuration(500).setListener(null)
                        rLayoutOption.visibility = View.GONE
                        rLayoutMessageHome.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        draggingUp = true
                        rLayoutMessageHome.visibility = View.GONE
                        if (tvPagename.alpha == 1F) {
                            tvPagename.animate().alpha(0F).setDuration(500).setListener(null)
                        }
                        findViewById<RelativeLayout>(R.id.rlHome).visibility = View.VISIBLE
                        findViewById<RelativeLayout>(R.id.rlListSearch).visibility = View.GONE
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
        val myUser: User = AppStorage.getInstance(context).getUserLocal()
        Log.d("CCCCCCCCCCCCC", myUser.toString())
        tvPagename.text = myUser.userName.toString()
        tvUserName.text = myUser.userName.toString()
        tvEmail.text = myUser.email.toString()
        val imageUrl = ApiConstant.URL_IMAGE + myUser.avatar
        Glide.with(this).load(imageUrl).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.avatardefault)
            .error(R.drawable.avatardefault).into(avatarUser)

    }

    private fun renderDataRecyclerView() {
        recentAdapter = RecentAdapter(progressBar,context)
        rvRecent.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvRecent.adapter = recentAdapter
        recentAdapter.notifyDataSetChanged()

    }

    private fun setStartLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent);
    }
}

