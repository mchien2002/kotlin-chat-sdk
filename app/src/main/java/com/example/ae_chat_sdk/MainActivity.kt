package com.example.ae_chat_sdk

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.ae_chat_sdk.data.api.service.WebSocketListener
import com.example.ae_chat_sdk.data.socket.SocketConstant
import com.example.ae_chat_sdk.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

    companion object {
        var Email: String = ""
        var OTP: String = ""
    }

    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_home)

        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val client=OkHttpClient()

        val apiKey=""

        val chanelId=1

        val request:Request=Request.Builder().url(SocketConstant.URL).build()
        val webSocketListener= WebSocketListener()
        val ws =client.newWebSocket(request,webSocketListener)

    }

}
