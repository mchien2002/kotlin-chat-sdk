package com.example.ae_chat_sdk.acti.popupwindow

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.ae_chat_sdk.R

class PopUpWindowBoxChat: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.layout_popup_chat)

        // ...

    }

}