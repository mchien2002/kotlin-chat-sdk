package com.example.ae_chat_sdk.acti.login


import android.animation.LayoutTransition
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.ae_chat_sdk.MainActivity
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.home.HomeFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EmailFragment : Fragment(), View.OnClickListener, TextWatcher {


    lateinit var btnSendEmail: Button
    lateinit var editEmail: TextInputEditText

    lateinit var emailTop: TextView

    lateinit var layoutEditEmail: TextInputLayout

    lateinit var layoutTransition1: RelativeLayout
    lateinit var layoutTransition2: RelativeLayout

    lateinit var inputOTP1: EditText
    lateinit var inputOTP2: EditText
    lateinit var inputOTP3: EditText
    lateinit var inputOTP4: EditText
    lateinit var inputOTP5: EditText
    lateinit var inputOTP6: EditText

    lateinit var v:View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_email, container, false)

        // Declare
        btnSendEmail = v.findViewById<Button>(R.id.buttonVerify)
        editEmail = v.findViewById<TextInputEditText>(R.id.editEmail)
        layoutEditEmail = v.findViewById<TextInputLayout>(R.id.inputEmailLayout)
        emailTop = v.findViewById(R.id.txtEmail)
        inputOTP1 = v.findViewById(R.id.inputOTP1)
        inputOTP2 = v.findViewById(R.id.inputOTP2)
        inputOTP3 = v.findViewById(R.id.inputOTP3)
        inputOTP4 = v.findViewById(R.id.inputOTP4)
        inputOTP5 = v.findViewById(R.id.inputOTP5)
        inputOTP6 = v.findViewById(R.id.inputOTP6)


        layoutTransition1 = v.findViewById<RelativeLayout>(R.id.inputEmailTransition)
        layoutTransition2 = v.findViewById<RelativeLayout>(R.id.inputEmailTransition2)


        // Listen
        btnSendEmail.setOnClickListener(this)
        v.findViewById<Button>(R.id.buttonInputEmailAgain).setOnClickListener(this)
        editEmail.addTextChangedListener(this)
        inputOTP1.addTextChangedListener(this)
        inputOTP2.addTextChangedListener(this)
        inputOTP3.addTextChangedListener(this)
        inputOTP4.addTextChangedListener(this)
        inputOTP5.addTextChangedListener(this)
        inputOTP6.addTextChangedListener(this)

//        v.findNavController().navigate(R.id.action_emailFragment_to_homeFragment)
        // slide up

        return v
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonVerify -> {
//                v.findNavController().navigate(R.id.action_emailFragment_to_OTPFragment)
                layoutTransition1.visibility = View.GONE
                layoutTransition2.visibility = View.VISIBLE
                emailTop.text = "Mã xác thực đã được gửi đến\n" + MainActivity.Email
                inputOTP1.isEnabled = true
                inputOTP1.requestFocus()
            }
            R.id.buttonInputEmailAgain -> {
                layoutTransition1.visibility = View.VISIBLE
                layoutTransition2.visibility = View.GONE
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {

        if (editEmail.isFocused) {
            MainActivity.Email = s.toString().trim()

            fun String.isValidEmail() =
                !TextUtils.isEmpty(MainActivity.Email) && Patterns.EMAIL_ADDRESS.matcher(
                    MainActivity.Email
                )
                    .matches()


            if (!MainActivity.Email.isValidEmail()) {
                layoutEditEmail.error = "Email chưa đúng cú pháp!"
                btnSendEmail.visibility = View.GONE
            } else {
                layoutEditEmail.isErrorEnabled = false
                btnSendEmail.visibility = View.VISIBLE
            }
        } else if (inputOTP1.isFocused) {
            if (s.toString().trim().length == 1) {
                inputOTP2.isEnabled = true
                inputOTP2.requestFocus()

                inputOTP1.isEnabled = false
                MainActivity.OTP += inputOTP1.text
            }
        } else if (inputOTP2.isFocused) {
            if (s.toString().trim().length == 1) {
                inputOTP3.isEnabled = true
                inputOTP3.requestFocus()

                inputOTP2.isEnabled = false
                MainActivity.OTP += inputOTP2.text

            }
        } else if (inputOTP3.isFocused) {
            if (s.toString().trim().length == 1) {
                inputOTP4.isEnabled = true
                inputOTP4.requestFocus()

                inputOTP3.isEnabled = false
                MainActivity.OTP += inputOTP3.text

            }
        } else if (inputOTP4.isFocused) {
            if (s.toString().trim().length == 1) {
                inputOTP5.isEnabled = true
                inputOTP5.requestFocus()

                inputOTP4.isEnabled = false
                MainActivity.OTP += inputOTP4.text

            }
        } else if (inputOTP5.isFocused) {
            if (s.toString().trim().length == 1) {
                inputOTP6.isEnabled = true
                inputOTP6.requestFocus()

                inputOTP5.isEnabled = false
                MainActivity.OTP += inputOTP5.text

            }
        } else if (inputOTP6.isFocused) {
            if (s.toString().trim().length == 1) {
                MainActivity.OTP += inputOTP6.text

                verifyOTP(MainActivity.OTP)
                v.findNavController().navigate(R.id.action_emailFragment_to_homeFragment)
            }
        }
    }

    private fun verifyOTP(OTP:String) {
        Log.d("OTP",OTP)
    }

}