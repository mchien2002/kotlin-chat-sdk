package com.example.ae_chat_sdk.acti.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.adapter.ContactAdapter
import com.example.ae_chat_sdk.acti.adapter.OnstreamAdapter
import com.example.ae_chat_sdk.acti.adapter.RecentAdapter
import com.example.ae_chat_sdk.acti.intro.LoginActivity
import com.example.ae_chat_sdk.acti.profile.ProfileActivity
import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.api.reponsitory.UserRepository
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

    lateinit var avatarUser: CircleImageView

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    private var IMAGE_PATH = ""

    companion object {
        const val MY_IMAGES = "imgFile"
    }


    var listContact: Boolean = false

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
        setData()
    }


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

        avatarUser = findViewById(R.id.ivAvatar)

        val ivAvatar = findViewById<CircleImageView>(R.id.ivAvatar)
        ivAvatar.setOnClickListener {
            showBottomSheetChangeAvatar()
        }


        tvUserName = findViewById(R.id.tvUsername)

        btnLogOut = findViewById(R.id.mbLogOut)
        btnLogOut.setOnClickListener(this)
    }

    private fun showBottomSheetChangeAvatar() {
        val bottomSheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_bottom_sheet_change_avatar, null)
        bottomSheet.setContentView(view)

        val btnChupAnh = view.findViewById<Button>(R.id.btnChupAnh)
        val btnChonAnh = view.findViewById<Button>(R.id.btnChonAnh)
        val btnXoaAnh = view.findViewById<Button>(R.id.btnXoaAnh)

        btnChupAnh.setOnClickListener {
            openCamera()
            bottomSheet.dismiss()
        }

        btnChonAnh.setOnClickListener {
            openGallery()
            bottomSheet.dismiss()
        }

        btnXoaAnh.setOnClickListener {
            // Do something when Button 3 is clicked
            bottomSheet.dismiss()
        }

        bottomSheet.show()
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        this.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    Log.d("DANG1", imageBitmap.toString())
                }
                REQUEST_IMAGE_PICK -> {

                    val uri: Uri? = data?.data
                    IMAGE_PATH =
                        uri?.let { RealPathUtil.getRealPath(this, it).toString() }.toString()
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // Ứng dụng đã được cấp quyền
                        // Tiến hành đọc tệp tin
                        Log.d("DANG111", "Doc duoc")
                    } else {
                        Log.d("DANG222", "Khong doc duoc")
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            1
                        )
                    }
                    ChangeAvatarUser()
                }
            }
        }
    }

    fun ChangeAvatarUser() {

        val file = File(IMAGE_PATH)
        val requestFile: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val partbodyavatar = MultipartBody.Part.createFormData("imgFile", file.name, requestFile)
        val token2 = RestClient().getToken()
        val userId = RestClient().getUserId()
        Log.d("TOKEN",token2)
        Log.d("USERID",userId)
        val call =
            UserRepository().uploadAvatarUser(
                token2,
                userId,
                partbodyavatar
            )
        call.enqueue(object : Callback<MyResponse> {
            override fun onResponse(call: Call<MyResponse>?, response: Response<MyResponse>?) {
                setLocalAvatar()
            }

            override fun onFailure(call: Call<MyResponse>?, t: Throwable?) {
                Toast.makeText(
                    applicationContext, "this is toast message 2", Toast.LENGTH_SHORT
                ).show()
            }
        })
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
                            tvPagename.setTextColor(Color.parseColor("#FFFFFF"))
                            tvPagename.text = "Username"
                            findViewById<RelativeLayout>(R.id.rlHome).visibility = View.VISIBLE
                            findViewById<RelativeLayout>(R.id.rlListContact).visibility = View.GONE
                        } else {
                            tvPagename.setTextColor(Color.parseColor("#80FFFFFF"))
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

    private fun setData() {
        val gson = Gson()
        val type = object : TypeToken<User>() {}.type
        val appStorage = AppStorage.getInstance(context)
        val userString: String = appStorage.getData("User", "").toString()
        val user = gson.fromJson<User>(userString, type)
        tvUserName.text = user.userName.toString()
        Log.d("Notify", user.userName.toString())

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
