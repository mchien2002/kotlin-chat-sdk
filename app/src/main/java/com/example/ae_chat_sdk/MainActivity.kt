package com.example.ae_chat_sdk

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.ae_chat_sdk.acti.intro.LoginActivity

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)
        Handler(Looper.getMainLooper()).postDelayed({
            setStartLoginActivity()
        }, 3000)
    }


    private fun setStartLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }



}
