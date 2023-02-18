package com.example.ae_chat_sdk.acti.intro

import android.content.Context
import android.content.Intent
import android.media.Image
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
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.ae_chat_sdk.MainActivity
import com.example.ae_chat_sdk.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class IntroFragment : Fragment(), View.OnClickListener, TextWatcher {

    // Intro


    // Email
    lateinit var btnSendEmail: Button
    lateinit var eTextEmail: EditText
    lateinit var tLayoutInputEmail: TextInputLayout
    lateinit var rLayoutWrapInputEmail: RelativeLayout

    // OTP
    lateinit var rLayoutWrapInputOTP: RelativeLayout
    lateinit var inputOTP1: EditText
    lateinit var inputOTP2: EditText
    lateinit var inputOTP3: EditText
    lateinit var inputOTP4: EditText
    lateinit var inputOTP5: EditText
    lateinit var inputOTP6: EditText

    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_intro, container, false)


        init()
        setOnClickListener(this)
        addTextChangedListener(this)
        showInputEmail()



        return v;
    }

    private fun addTextChangedListener(introFragment: IntroFragment) {

        eTextEmail.addTextChangedListener(introFragment)
        inputOTP1.addTextChangedListener(introFragment)
        inputOTP2.addTextChangedListener(introFragment)
        inputOTP3.addTextChangedListener(introFragment)
        inputOTP4.addTextChangedListener(introFragment)
        inputOTP5.addTextChangedListener(introFragment)
        inputOTP6.addTextChangedListener(introFragment)
    }


    private fun setOnClickListener(introFragment: IntroFragment) {
        // Email
        btnSendEmail.setOnClickListener(introFragment)


        // OTP
        v.findViewById<Button>(R.id.buttonInputEmailAgain).setOnClickListener(introFragment)
    }


    private fun init() {
        // Intro


        // Email
        btnSendEmail = v.findViewById(R.id.buttonSendEmail)
        eTextEmail = v.findViewById(R.id.eTextEmail)
        tLayoutInputEmail = v.findViewById(R.id.tLayoutInputEmail)
        rLayoutWrapInputEmail = v.findViewById(R.id.rLayoutWrapInputEmail)

        // OTP

        rLayoutWrapInputOTP = v.findViewById(R.id.rLayoutWrapTextInputOTP)
        inputOTP1 = v.findViewById(R.id.eTextInputOTP1)
        inputOTP2 = v.findViewById(R.id.eTextInputOTP2)
        inputOTP3 = v.findViewById(R.id.eTextInputOTP3)
        inputOTP4 = v.findViewById(R.id.eTextInputOTP4)
        inputOTP5 = v.findViewById(R.id.eTextInputOTP5)
        inputOTP6 = v.findViewById(R.id.eTextInputOTP6)


    }

    private fun showInputEmail() {
        v.findViewById<ImageView>(R.id.iViewLetterLogoIntro).animate().alpha(0F).setDuration(200).setStartDelay(2500)

        v.findViewById<ImageView>(R.id.iViewIconLogoIntroBefore).animate()
            .alpha(0F)
            .setDuration(200).setStartDelay(2500)

        Handler(Looper.getMainLooper()).postDelayed({
            BottomSheetBehavior.from(v.findViewById(R.id.rLayoutInput)).apply {
                this.state = BottomSheetBehavior.STATE_EXPANDED
            }

        }, 3000)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.buttonSendEmail -> {
//                v.findNavController().navigate(R.id.action_emailFragment_to_OTPFragment)
                rLayoutWrapInputEmail.visibility = View.GONE
                rLayoutWrapInputOTP.visibility = View.VISIBLE
                v.findViewById<TextView>(R.id.tViewEmailInformation).text = "Mã xác thực đã được gửi đến\n" + MainActivity.Email
                inputOTP1.isEnabled = true
                inputOTP1.requestFocus()
            }
            R.id.buttonInputEmailAgain -> {
                rLayoutWrapInputEmail.visibility = View.VISIBLE
                rLayoutWrapInputOTP.visibility = View.GONE
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        if (eTextEmail.isFocused) {
            MainActivity.Email = s.toString().trim()

            fun String.isValidEmail() =
                !TextUtils.isEmpty(MainActivity.Email) && Patterns.EMAIL_ADDRESS.matcher(
                    MainActivity.Email
                )
                    .matches()


            if (!MainActivity.Email.isValidEmail()) {
                tLayoutInputEmail.error = "Email chưa đúng cú pháp!"
                btnSendEmail.visibility = View.GONE
            } else {
                tLayoutInputEmail.isErrorEnabled = false
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
                v.findNavController().navigate(R.id.action_introFragment_to_homeFragment)
            }
        }
    }

    private fun verifyOTP(OTP: String) {
        Log.d("OTP", OTP)
    }
}

