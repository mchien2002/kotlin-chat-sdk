package com.example.ae_chat_sdk.acti.login

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.ae_chat_sdk.MainActivity
import com.example.ae_chat_sdk.R

class OTPFragment : Fragment(), View.OnClickListener {

    lateinit var btnInputEmailAgain:Button
    lateinit var txtEmail:TextView
    lateinit var txtCountDown:TextView
    lateinit var btnSendOTPAgain:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v:View = inflater.inflate(R.layout.fragment_otp, container, false)
        // Declare
        btnInputEmailAgain=v.findViewById<Button>(R.id.buttonInputEmailAgain)
        txtEmail=v.findViewById<TextView>(R.id.txt_Email)
        txtCountDown=v.findViewById<TextView>(R.id.txt_countdown)
        btnSendOTPAgain=v.findViewById<TextView>(R.id.button_OTPsendAgain)

        val regex = """(?:\G(?!^)|(?<=^[^@]{2}|@))[^@](?!\.[^.]+$)""".toRegex()
        txtEmail.text = "Mã xác thực đã được gửi đến\n"+ MainActivity.Email.replace(regex, "*")

        object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                txtCountDown.setText("00:"+millisUntilFinished / 1000)
            }

            override fun onFinish() {
                txtCountDown.setText("00:00")
                btnSendOTPAgain.setTextColor(Color.parseColor("#FFE4A400"))
            }
        }.start()


        // Listen
        btnInputEmailAgain.setOnClickListener(this)
        return v
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonInputEmailAgain->{
                v.findNavController().navigate(R.id.action_OTPFragment_to_emailFragment)
            }
        }
    }
}