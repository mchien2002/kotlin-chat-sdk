package com.example.ae_chat_sdk.acti.intro

import android.os.Bundle
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

class IntroFragment : Fragment(), View.OnClickListener {

    private lateinit var btnSignIn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_intro, container, false)
        btnSignIn = v.findViewById<Button>(R.id.buttonSignIn)
        btnSignIn.setOnClickListener(this)
        return v;
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonSignIn->{
                v.findNavController().navigate(R.id.action_introFragment_to_emailFragment)
            }
        }
    }
}

