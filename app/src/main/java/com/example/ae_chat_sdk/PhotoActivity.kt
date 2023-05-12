package com.example.ae_chat_sdk

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.github.chrisbanes.photoview.PhotoView

class PhotoActivity : AppCompatActivity() {
    lateinit var photoView: PhotoView
    lateinit var btnBack: ImageButton
    lateinit var tvUsername: TextView
    lateinit var context: Context

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        context = applicationContext

        hideSystemUI()
        init()
        setData()
        setOnClickListener()
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

    private fun init() {
        btnBack = findViewById(R.id.ibBack)
        tvUsername = findViewById(R.id.tvUsername)

        photoView = findViewById(R.id.photo_view)
    }

    private fun setData() {
        val appStorage = AppStorage.getInstance(context)
        tvUsername.text = appStorage.getUserLocal().userName

        val url = intent.getStringExtra("imageUrl")
        Glide.with(applicationContext).load(url).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.drawable.image_default).into(photoView)
    }

    private fun setOnClickListener() {
        btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}