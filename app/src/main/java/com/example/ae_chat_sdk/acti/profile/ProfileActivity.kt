package com.example.ae_chat_sdk.acti.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import com.example.ae_chat_sdk.R

class ProfileActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        init()
    }

    private fun init(){
        setButtonOnClickListener()
    }

    private fun setButtonOnClickListener() {
        findViewById<ImageButton>(R.id.ibBack).setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}