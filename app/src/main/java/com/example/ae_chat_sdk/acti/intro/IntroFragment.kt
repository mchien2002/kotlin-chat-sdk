package com.example.ae_chat_sdk.acti.intro

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.ae_chat_sdk.R

class IntroFragment : Fragment(){

    private lateinit var btnSignIn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_intro, container, false)
        Handler(Looper.getMainLooper()).postDelayed({
            v.findNavController().navigate(R.id.action_introFragment_to_emailFragment)
        }, 3000)
        return v;
    }

}

