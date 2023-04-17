package com.example.ae_chat_sdk

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.ae_chat_sdk.acti.intro.LoginActivity
import com.example.ae_chat_sdk.data.api.service.WebSocketListener
import com.example.ae_chat_sdk.data.socket.SocketConstant
import com.example.ae_chat_sdk.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class MainActivity : AppCompatActivity(){

    lateinit var ibLogo:ImageButton


    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_intro)

        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStartLoginActivity()
    }


    private fun setStartLoginActivity() {
        val intent:Intent= Intent(this, LoginActivity::class.java )
        this.startActivity(intent)
    }



}
