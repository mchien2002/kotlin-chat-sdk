package com.example.ae_chat_sdk.acti.profile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.home.HomeActivity
import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.api.reponsitory.UserRepository
import com.example.ae_chat_sdk.data.model.MyResponse
import com.example.ae_chat_sdk.data.model.RealPathUtil
import com.example.ae_chat_sdk.data.model.User
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class ProfileActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    private var IMAGE_PATH = ""

    lateinit var tvUserName: TextView
    lateinit var tvEmail: TextView
    lateinit var etInputEmail: EditText
    lateinit var etInputUsername: EditText

    lateinit var iViewAvatarUser: ImageView

    lateinit var context: Context
    lateinit var appStorage: AppStorage
    lateinit var myUser: User

    companion object {
        const val MY_IMAGES = "imgFile"
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideSystemUI()

        setContentView(R.layout.activity_profile)
        context = applicationContext
        appStorage = AppStorage.getInstance(context)
        myUser = AppStorage.getInstance(context).getUserLocal()

        init()
        setUserData()
    }

    private fun setStartHomeActivity() {
        val intent = Intent(context, HomeActivity::class.java)
        this.startActivity(intent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        WindowInsetsControllerCompat(
            window,
            window.decorView.findViewById(android.R.id.content)
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun setUserData() {
        tvUserName.text = myUser.userName.toString()
        tvEmail.text = myUser.email.toString()
        etInputEmail.setText(myUser.email.toString())
        etInputUsername.setText((myUser.userName.toString()))

        val imageUrl = ApiConstant.URL_IMAGE + myUser.avatar
        Glide.with(context).load(imageUrl).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.avatardefault)
            .error(R.drawable.avatardefault).into(iViewAvatarUser)
    }

    private fun init() {
        iViewAvatarUser = findViewById(R.id.ivAvatar)
        tvUserName = findViewById(R.id.tvUsername)
        tvEmail = findViewById(R.id.tvEmail)
        etInputEmail = findViewById(R.id.etInputEmail)
        etInputUsername = findViewById(R.id.etInputUsername)
        setButtonOnClickListener()
    }

    private fun setButtonOnClickListener() {
        findViewById<ImageButton>(R.id.ibBack).setOnClickListener(View.OnClickListener {
            if (etInputEmail.text.toString().trim() != myUser.email.toString().trim()
                || etInputUsername.text.toString().trim() != myUser.userName.toString().trim()) {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("Bạn có muốn lưu thay đổi không?")
                alertDialogBuilder.setMessage("Vui lòng kiểm tra lại thông tin trước khi lưu!")
                alertDialogBuilder.setPositiveButton("Lưu") { _, _ ->
                    // Save
                    val token2 = RestClient().getToken()
                    myUser = AppStorage.getInstance(context).getUserLocal()
                    val user: User = User(
                        myUser.avatar,
                        myUser.createdAt,
                        myUser.email,
                        myUser.fullName,
                        myUser.localName,
                        myUser.phone,
                        token2,
                        myUser.userId,
                        etInputUsername.text.toString().trim()
                    )
                    val call = UserRepository().updateUser(
                        token2, user
                    )
                    call.enqueue(object : Callback<MyResponse> {
                        override fun onResponse(
                            call: Call<MyResponse>?,
                            response: Response<MyResponse>?
                        ) {
                            val gson = Gson()
                            val appStorage = AppStorage.getInstance(context!!)
                            if (response != null) {
                                appStorage.saveData("User", gson.toJson(response.body()?.data))
                            }
                            Toast.makeText(
                                applicationContext,
                                "Cập nhật thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                            setStartHomeActivity()
                        }

                        override fun onFailure(call: Call<MyResponse>?, t: Throwable?) {
                            Toast.makeText(
                                applicationContext, "Cập nhật không thành công", Toast.LENGTH_SHORT
                            ).show()
                            setStartHomeActivity()
                        }
                    })
                }
                alertDialogBuilder.setNegativeButton("Thoát") { _, _ ->
                    setStartHomeActivity()
                }
                alertDialogBuilder.show()
            } else {
                setStartHomeActivity()
            }
        })
        findViewById<ImageButton>(R.id.ibChangeAvatar).setOnClickListener(View.OnClickListener {
            showBottomSheetChangeAvatar()
        })
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
                            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // Ứng dụng đã được cấp quyền
                        // Tiến hành đọc tệp tin
                        Log.d("DANG111", "Doc duoc")
                    } else {
                        Log.d("DANG222", "Khong doc duoc")
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
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
        Log.d("TOKEN", token2)
        Log.d("USERID", userId)
        val call = UserRepository().uploadAvatarUser(
            token2, userId, partbodyavatar
        )
        call.enqueue(object : Callback<MyResponse> {
            override fun onResponse(call: Call<MyResponse>?, response: Response<MyResponse>?) {

                val gson = Gson()
                if (response != null) {
                    appStorage.saveData("User", gson.toJson(response.body()?.data))
                }
                setLocalAvatar()
            }

            override fun onFailure(call: Call<MyResponse>?, t: Throwable?) {
                Toast.makeText(
                    applicationContext, "Cập nhật ảnh thất bại!", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setLocalAvatar() {
        val imageUrl = ApiConstant.URL_IMAGE + AppStorage.getInstance(context).getUserLocal().avatar
        Glide.with(context).load(imageUrl).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.avatardefault)
            .error(R.drawable.avatardefault).into(iViewAvatarUser)
    }
}