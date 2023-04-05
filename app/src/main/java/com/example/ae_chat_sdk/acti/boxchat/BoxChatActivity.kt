package com.example.ae_chat_sdk.acti.boxchat

import android.app.ActionBar.LayoutParams
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.ae_chat_sdk.R
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

@Suppress("DEPRECATION")
class BoxChatActivity: AppCompatActivity() {

    lateinit var btnBack: ImageButton
    lateinit var btnNotification: ImageButton
    lateinit var btnSendMessage: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box_chat)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        val radius: Float = 25f;

        val decorView: View = window.decorView
        // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        val rootView: ViewGroup = decorView.findViewById(android.R.id.content)

        // Optional:
        // Set drawable to draw in the beginning of each blurred frame.
        // Can be used in case your layout has a lot of transparent space and your content
        // gets a too low alpha value after blur is applied.
        val windowBackground: Drawable = decorView.background


        findViewById<BlurView>(R.id.blurView).setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)

        init()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        btnNotification.setOnClickListener(View.OnClickListener {

        })

        btnSendMessage.setOnClickListener(View.OnClickListener {

        })
    }

    fun init() {
        btnBack = findViewById(R.id.ibBack)
        btnNotification = findViewById(R.id.ibNotification)
        btnSendMessage = findViewById(R.id.ibSendMessage)
    }
}