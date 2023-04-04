package com.example.ae_chat_sdk.acti.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.MainActivity
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.adapter.ContactAdapter
import com.example.ae_chat_sdk.acti.adapter.OnstreamAdapter
import com.example.ae_chat_sdk.acti.adapter.RecentAdapter
import com.example.ae_chat_sdk.acti.intro.LoginActivity
import com.example.ae_chat_sdk.acti.profile.ProfileActivity
import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.api.reponsitory.RegisterRepository
import com.example.ae_chat_sdk.data.api.reponsitory.UserRepository
import com.example.ae_chat_sdk.data.model.ApiResponse
import com.example.ae_chat_sdk.data.model.MyResponse
import com.example.ae_chat_sdk.data.model.RealPathUtil
import com.example.ae_chat_sdk.data.model.User
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity(), View.OnClickListener {

    // Context
    lateinit var context: Context

    // RecyclerView Message Home
    lateinit var rvOnstream: RecyclerView
    lateinit var rvRecent: RecyclerView
    lateinit var rvListContact: RecyclerView

    lateinit var btnLogOut: Button


    lateinit var recent: ArrayList<String>
    lateinit var onstream: ArrayList<String>
    lateinit var contact: ArrayList<String>

    lateinit var rLayoutBottomSheetHome: RelativeLayout

    // lateinit var rLayoutBottomSheetChangeAvatar: LinearLayout
    lateinit var rLayoutMessageHome: RelativeLayout

    // BottomSheet
    lateinit var bottomSheetHomeBehavior: BottomSheetBehavior<View>

    // Name Page
    lateinit var tvPagename: TextView
    lateinit var tvUserName: TextView

    lateinit var etSearch: EditText

    lateinit var avatarUser: CircleImageView


    lateinit var iViewAvatarUser: ImageView

    var listContact: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // set data for list on Stream
        onstream = ArrayList()
        for (i in 1..20) {
            onstream.add("username # $i")
        }

        // set data for recent chat
        recent = ArrayList()
        for (i in 1..20) {
            recent.add("username # $i")
        }

        contact = ArrayList()
        for (i in 1..20) {
            contact.add("username # $i")
        }

        context = applicationContext

        init()
        setButtonOnClickListener()
        renderDataRecyclerView()
        setBottomSheetBehaviorHome()
<<<<<<< HEAD
        setData()
        onStart()

        etSearch.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val textSearch = etSearch.text
                searchUser(textSearch.toString())
                Log.d("SEARCH",textSearch.toString())
            }
            false
        }
=======
//        setData()
>>>>>>> New-UI
    }

    override fun onStart() {
        super.onStart()
        setData()
    }

    private fun searchUser(searchText : String)
    {
        val token = RestClient().getToken()
        val call = UserRepository().searchUser(token,searchText)
        var userList : List<User>
        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(context, "Không thể gửi mã xác thực!", Toast.LENGTH_SHORT)
                    .show()
            }
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                val gson = Gson()
                val type = object : TypeToken<List<User>>() {}.type
                val user = gson.fromJson<List<User>>(gson.toJson(response.body()?.data), type)
                Log.d("RESPONSE", user.size.toString())
                val listUserId = mutableListOf<String>()
                for (i in 0..user.size - 1) {
                    val data = user[i].userName
                    listUserId.add(data)
                    Log.d("USER", user[i].userName.toString())
                }
                for (i in 0..listUserId.size - 1) {
                    Log.d("USERID", listUserId[i].toString())
                }
                rvOnstream.layoutManager =
                    LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                rvOnstream.adapter = OnstreamAdapter(listUserId as ArrayList<String>, context)
            }
        })

    }
//    private fun renderSearchUser()
//    {
//        rvOnstream.layoutManager =
//            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
//        rvOnstream.adapter = OnstreamAdapter(onstream, context)
//    }

    //    private fun setButtonOnClickListener() {
//        findViewById<MaterialButton>(R.id.mbListContact)
//            .setOnClickListener(View.OnClickListener {
//                findViewById<RelativeLayout>(R.id.rlMenuOption).animate().alpha(0F)
//                    .setDuration(0).startDelay = 0
//                tvPagename.text = "Danh sách liên hệ"
//                tvPagename.setTextColor(Color.parseColor("#80FFFFFF"))
//                listContact = true
//                true
//            })
//    }
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
        rLayoutBottomSheetHome = findViewById(R.id.rlBottomSheetHome)
        // rLayoutBottomSheetChangeAvatar = findViewById(R.id.rlBottomSheetChangeAvatar)
        rLayoutMessageHome = findViewById(R.id.rlMessageHome)

        rvOnstream = findViewById(R.id.rvHorizonalOnstream)
        rvRecent = findViewById(R.id.rvVerticalRecent)
        rvListContact = findViewById(R.id.rvHorizonalContact)

        bottomSheetHomeBehavior = BottomSheetBehavior.from(rLayoutBottomSheetHome)

        tvPagename = findViewById(R.id.tvPageName)

        etSearch = findViewById(R.id.etInputSearch)

        avatarUser = findViewById(R.id.ivAvatar)

//        iViewAvatarUser = findViewById(R.id.ivAvatarUser)

//        val ivAvatar = findViewById<CircleImageView>(R.id.ivAvatar)
//        ivAvatar.setOnClickListener {
//            showBottomSheetChangeAvatar()
//        }


        tvUserName = findViewById(R.id.tvUsername)

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
                        listContact = false
                    }
                    else -> {
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

//    private fun setData() {
//        val gson = Gson()
//        val type = object : TypeToken<User>() {}.type
//        val appStorage = AppStorage.getInstance(context)
//        val userString: String = appStorage.getData("User", "").toString()
//        val user = gson.fromJson<User>(userString, type)
//        tvUserName.text = user.userName.toString()
//        Log.d("Notify", user.userName.toString())
//
//        val imgLocal = appStorage?.getData("avatar", "").toString()
//        if(imgLocal.length > 1)
//        {
//            Glide.with(this)
//                .load(imgLocal)
//                .into(avatarUser)
//        }
//        else if (user.avatar == null) {
//            val imageUrl =
//                "https://3.bp.blogspot.com/-SMNLs_5XfVo/VHvNUx8dWZI/AAAAAAAAQnY/NWdkO4JPE_M/s1600/Avatar-Facebook-an-danh-trang-4.jpg"
//            Glide.with(this)
//                .load(imageUrl)
//                .into(avatarUser)
//        } else {
//            val imageUrl = ApiConstant.URL_AVATAR + user.avatar
//            Log.d("link", imageUrl.toString())
//            Glide.with(this)
//                .load(imageUrl)
//                .into(avatarUser)
//        }
//    }

<<<<<<< HEAD
        val imgLocal = appStorage?.getData("avatar", "").toString()
        if(imgLocal.length > 1)
        {
            Glide.with(this)
                .load(imgLocal)
                .into(avatarUser)
        }
        else if (user.avatar == null) {
            val imageUrl =
                "https://3.bp.blogspot.com/-SMNLs_5XfVo/VHvNUx8dWZI/AAAAAAAAQnY/NWdkO4JPE_M/s1600/Avatar-Facebook-an-danh-trang-4.jpg"
            Glide.with(this)
                .load(imageUrl)
                .into(avatarUser)
        } else {
            val imageUrl = ApiConstant.URL_AVATAR + user.avatar
            Log.d("link", imageUrl.toString())
            Glide.with(this)
                .load(imageUrl)
                .into(avatarUser)
        }
    }

=======
    private fun setLocalAvatar()
    {
        val imageUrl = IMAGE_PATH
        Log.d("link", imageUrl.toString())
        Glide.with(this)
            .load(imageUrl)
            .into(avatarUser)
        val appStorage = AppStorage.getInstance(context!!)
        appStorage.saveData("avatar", IMAGE_PATH)
    }
>>>>>>> New-UI

    override fun onClick(view: View) {
        when (view?.id) {
            R.id.mbLogOut -> {
                setStartLoginActivity()
                val appStorage = context?.let { AppStorage.getInstance(it) }
                val userString = appStorage?.clearData()
                Log.d("SHOW DATAAAAAA", userString.toString())
            }
            R.id.ivAvatar -> {
                Toast.makeText(applicationContext, "this is toast message 1", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun renderDataRecyclerView() {
        rvOnstream.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rvOnstream.adapter = OnstreamAdapter(onstream, context)

        rvRecent.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvRecent.adapter = RecentAdapter(recent, context)

        rvListContact.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvListContact.adapter = ContactAdapter(contact, context)
        rvListContact.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun setStartLoginActivity() {
        val intent: Intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent);
    }
}

