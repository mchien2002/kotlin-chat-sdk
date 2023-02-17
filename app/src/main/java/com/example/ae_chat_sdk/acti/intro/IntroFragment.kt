package com.example.ae_chat_sdk.acti.intro

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.ae_chat_sdk.R

class IntroFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_intro, container, false)

        val ivIconIntro = v.findViewById<ImageView>(R.id.ivIconLogoIntro)
        val lnLayoutIntro = v.findViewById<LinearLayout>(R.id.layoutIntro)
        val ivLetterIntro = v.findViewById<ImageView>(R.id.ivLetterLogoIntro)

        Handler(Looper.getMainLooper()).postDelayed({
            ivLetterIntro.setBackgroundResource(R.drawable.logo_letter_sigma2)
            lnLayoutIntro.setBackgroundResource(R.color.color2)

        }, 3000)


        ivIconIntro.animate().alpha(0F).setDuration(200).setStartDelay(2500)
        ivLetterIntro.animate().translationY(-1635F).setDuration(500).setStartDelay(2500)

        Handler(Looper.getMainLooper()).postDelayed({
            v.findNavController().navigate(R.id.action_introFragment_to_emailFragment)
        }, 3400)
        return v;
    }

}

