package com.example.ae_chat_sdk.acti.media

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.MediaController
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.example.ae_chat_sdk.R
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PhotoActivity : AppCompatActivity() {
    lateinit var photoView: PhotoView
    lateinit var videoView: VideoView
    lateinit var btnBack: ImageButton
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

        videoView = findViewById(R.id.vvVideo_View)
        photoView = findViewById(R.id.photo_view)
    }

    private fun setData() {
        val topic = intent.getStringExtra("topic")
        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)
        mediaController.setAnchorView(videoView)
        if (topic == "image") {
            GlobalScope.launch(Dispatchers.Main) {
                photoView.visibility = View.VISIBLE
                val url = intent.getStringExtra("mediaUrl")
                Glide.with(applicationContext).load(url).placeholder(R.drawable.image_default)
                    .error(R.drawable.image_default).into(photoView)
            }

        } else if (topic == "video") {
            GlobalScope.launch(Dispatchers.Main) {
                videoView.visibility = View.VISIBLE
                val url = intent.getStringExtra("mediaUrl")
                val videoUri = Uri.parse("http://techslides.com/demos/sample-videos/small.mp4")

                videoView.setVideoPath(videoUri.toString())
                videoView.seekTo(1);

                mediaController.setAnchorView(videoView)
                videoView.start()
            }
        }
    }

    private fun setOnClickListener() {
        btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}