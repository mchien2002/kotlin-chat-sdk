package com.example.ae_chat_sdk

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.ae_chat_sdk.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        var Email: String = ""
        var OTP: String = ""
    }

    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.layout_popup_chat)

        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

}
