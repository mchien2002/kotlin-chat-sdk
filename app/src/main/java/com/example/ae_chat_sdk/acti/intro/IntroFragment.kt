package com.example.ae_chat_sdk.acti.intro

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.ae_chat_sdk.R

class IntroFragment : Fragment() {


    private lateinit var ic: ImageView
    lateinit var layoutItr: LinearLayout
    lateinit var logo: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_intro, container, false)
        ic = v.findViewById<ImageView>(R.id.iconLogo)
        layoutItr = v.findViewById<LinearLayout>(R.id.layoutItro)
        logo = v.findViewById<ImageView>(R.id.logo)

//        Handler(Looper.getMainLooper()).postDelayed({
//
//        }, 2700)

        Handler(Looper.getMainLooper()).postDelayed({
            logo.setBackgroundResource(R.drawable.logo2)
            layoutItr.setBackgroundResource(R.color.color2)

        }, 2200)


        ic.animate().alpha(0F).setDuration(200).setStartDelay(2000)
        logo.animate().translationY(-1550F).setDuration(500).setStartDelay(2000)


        Handler(Looper.getMainLooper()).postDelayed({
//            layoutInputEmail.animate().translationY(-1850F).setDuration(200).setStartDelay(2500)
            v.findNavController().navigate(R.id.action_introFragment_to_emailFragment)
        }, 2600)
        return v;
    }

}

