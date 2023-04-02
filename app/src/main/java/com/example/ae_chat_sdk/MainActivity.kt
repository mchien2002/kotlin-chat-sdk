package com.example.ae_chat_sdk

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.ae_chat_sdk.acti.intro.LoginActivity
import com.example.ae_chat_sdk.data.api.service.WebSocketListener
import com.example.ae_chat_sdk.data.socket.SocketConstant
import com.example.ae_chat_sdk.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity(){

    lateinit var ibLogo:ImageButton

    companion object {
        var Email: String = ""
        var OTP: String = ""
    }

    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_intro)

        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectSocket()
        setStartLoginActivity()
    }


    private fun setStartLoginActivity() {
        val intent:Intent= Intent(this, LoginActivity::class.java )
        this.startActivity(intent)
    }

    private fun connectSocket(){
        val client=OkHttpClient()

        val apiKey=""

        val chanelId=1

        val request:Request=Request.Builder().url(SocketConstant.URL).build()
        val webSocketListener= WebSocketListener()
        val ws =client.newWebSocket(request,webSocketListener)
    }

}
