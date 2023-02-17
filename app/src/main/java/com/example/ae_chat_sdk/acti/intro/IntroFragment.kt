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
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.R.id.rlInputEmail
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class IntroFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_intro, container, false)

        val ivIconIntro = v.findViewById<ImageView>(R.id.ivIconLogoIntro)
        val ivLetterInto=v.findViewById<ImageView>(R.id.ivLetterLogoIntro)
        val rlInputEmail = v.findViewById<RelativeLayout>(R.id.rlInputEmail)



        ivIconIntro.animate()
            .translationY(-500F)
            .setDuration(200).setStartDelay(2500)
        ivLetterInto.animate().alpha(0F).setDuration(0).setStartDelay(2500)

        Handler(Looper.getMainLooper()).postDelayed({
            rlInputEmail.visibility=View.VISIBLE
            BottomSheetBehavior.from(rlInputEmail).apply {
                peekHeight = 0
                isHideable=false
                this.setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }, 3000)


//        Handler(Looper.getMainLooper()).postDelayed({
//            v.findNavController().navigate(R.id.action_introFragment_to_emailFragment)
//        }, 3400)
        return v;
    }

}

