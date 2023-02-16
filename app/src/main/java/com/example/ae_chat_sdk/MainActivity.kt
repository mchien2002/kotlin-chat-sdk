package com.example.ae_chat_sdk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.ae_chat_sdk.acti.home.HomeFragment
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
//        setContentView(R.layout.fragment_otp)

        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
