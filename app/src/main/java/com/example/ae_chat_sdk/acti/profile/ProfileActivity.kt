package com.example.ae_chat_sdk.acti.profile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.data.api.ApiConstant
import com.example.ae_chat_sdk.data.api.RestClient
import com.example.ae_chat_sdk.data.api.reponsitory.UserRepository
import com.example.ae_chat_sdk.data.model.MyResponse
import com.example.ae_chat_sdk.data.model.RealPathUtil
import com.example.ae_chat_sdk.data.model.User
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.google.android.material.bottomsheet.BottomSheetDialog
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

    companion object {
        const val MY_IMAGES = "imgFile"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        context = applicationContext
        init()
        setAvatar()
    }

    private fun setAvatar() {
        val appStorage = AppStorage.getInstance(context)
        val myUser: User = AppStorage.getInstance(context).getUserLocal()
        tvUserName.text = myUser.userName.toString()
        tvEmail.text = myUser.email.toString()
        etInputEmail.setText(myUser.email.toString())
        etInputUsername.setText((myUser.userName.toString()))

        val imgLocal = appStorage?.getData("avatar", "").toString()
        if (imgLocal.length > 1) {
            Glide.with(this).load(imgLocal).into(iViewAvatarUser)
        } else if (myUser.avatar == null) {
            val imageUrl =
                "https://3.bp.blogspot.com/-SMNLs_5XfVo/VHvNUx8dWZI/AAAAAAAAQnY/NWdkO4JPE_M/s1600/Avatar-Facebook-an-danh-trang-4.jpg"
            Glide.with(this).load(imageUrl).into(iViewAvatarUser)
        } else {
            val imageUrl = ApiConstant.URL_AVATAR + myUser.avatar
            Log.d("link", imageUrl.toString())
            Glide.with(this).load(imageUrl).into(iViewAvatarUser)
        }
    }

    private fun init() {
        iViewAvatarUser = findViewById(R.id.ivAvatar)
        // avatarUser = findViewById(R.id.ivAvatar)
        tvUserName = findViewById(R.id.tvUsername)
        tvEmail = findViewById(R.id.tvEmail)
        etInputEmail = findViewById(R.id.etInputEmail)
        etInputUsername = findViewById(R.id.etInputUsername)
        setButtonOnClickListener()
    }

    private fun setButtonOnClickListener() {
        findViewById<ImageButton>(R.id.ibBack).setOnClickListener(View.OnClickListener {
            finish()
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
                setLocalAvatar()
            }

            override fun onFailure(call: Call<MyResponse>?, t: Throwable?) {
                Toast.makeText(
                    applicationContext, "this is toast message 2", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setLocalAvatar() {
        val imageUrl = IMAGE_PATH
        Log.d("link", imageUrl.toString())
        Glide.with(this).load(imageUrl).into(iViewAvatarUser)
        val appStorage = AppStorage.getInstance(context!!)
        appStorage.saveData("avatar", IMAGE_PATH)
    }
}