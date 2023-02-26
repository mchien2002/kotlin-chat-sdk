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
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.ae_chat_sdk.MainActivity
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.data.api.RestApi
import com.example.ae_chat_sdk.data.api.service.BaseService
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IntroFragment : Fragment(), View.OnClickListener {

    // Intro
    lateinit var iViewLogo: ImageView

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
        showInputEmail()



        return v;
    }

    private fun setListenerEmail() {
//        rLayoutWrapInputOTP.setOnClickListener {
//            val inputManager =
//                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputManager.hideSoftInputFromWindow(rLayoutWrapInputEmail.windowToken, 0)
//        }
        eTextEmail.addTextChangedListener {
            it?.let { string ->
                fun String.isValidEmail() =
                    !TextUtils.isEmpty(string.toString().trim()) && Patterns.EMAIL_ADDRESS.matcher(
                        string.toString().trim()
                    )
                        .matches()


                if (!string.toString().trim().isValidEmail()) {
                    tLayoutInputEmail.error = "Email chưa đúng cú pháp!"
                    btnSendEmail.visibility = View.GONE
                } else {
                    tLayoutInputEmail.isErrorEnabled = false
                    btnSendEmail.visibility = View.VISIBLE
                    MainActivity.Email = string.toString().trim()
                }
            }
        }
    }

    private fun setListenerOTP() {
//        rLayoutWrapInputOTP.setOnClickListener {
//            val inputManager =
//                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputManager.hideSoftInputFromWindow(rLayoutWrapInputEmail.windowToken, 0)
//        }
        setTextChangeListener(inputOTP1, inputOTP2)
        setTextChangeListener(inputOTP2, inputOTP3)
        setTextChangeListener(inputOTP3, inputOTP4)
        setTextChangeListener(inputOTP4, inputOTP5)
        setTextChangeListener(inputOTP5, inputOTP6)
        setTextChangeListener(inputOTP6, done = {
            MainActivity.OTP = inputOTP1.text.toString().trim() + inputOTP2.text.toString()
                .trim() + inputOTP3.text.toString().trim() + inputOTP4.text.toString()
                .trim() + inputOTP5.text.toString().trim() + inputOTP6.text.toString().trim()
            Toast.makeText(context, MainActivity.OTP, Toast.LENGTH_SHORT).show()
            verifyOTP(MainActivity.OTP)
        })

        setKeyListener(inputOTP2, inputOTP1)
        setKeyListener(inputOTP3, inputOTP2)
        setKeyListener(inputOTP4, inputOTP3)
        setKeyListener(inputOTP5, inputOTP4)
        setKeyListener(inputOTP6, inputOTP5)
    }

    private fun resetOTP() {
        inputOTP1.isEnabled = false
        inputOTP2.isEnabled = false
        inputOTP3.isEnabled = false
        inputOTP4.isEnabled = false
        inputOTP5.isEnabled = false
        inputOTP6.isEnabled = false


        inputOTP1.setText("")
        inputOTP2.setText("")
        inputOTP3.setText("")
        inputOTP4.setText("")
        inputOTP5.setText("")
        inputOTP6.setText("")

        initFocusOTP()
    }

    private fun initFocusEmail() {
        eTextEmail.isEnabled = true

//        eTextEmail.postDelayed({
//            eTextEmail.requestFocus()
//            val inputMethodManager =
//                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputMethodManager.showSoftInput(eTextEmail, InputMethodManager.SHOW_FORCED)
//        }, 4000)
    }

    private fun initFocusOTP() {
        inputOTP1.isEnabled = true
        inputOTP1.requestFocus()
//        inputOTP1.postDelayed({
//            inputOTP1.requestFocus()
//            val inputMethodManager =
//                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputMethodManager.showSoftInput(eTextEmail, InputMethodManager.SHOW_FORCED)
//        }, 500)
    }


    private fun setTextChangeListener(
        fromEditText: EditText,
        targetEditText: EditText? = null,
        done: (() -> Unit)? = null
    ) {
        fromEditText.addTextChangedListener {
            it?.let { string ->
                if (string.isNotEmpty()) {
                    targetEditText?.let { editText ->
                        editText.isEnabled = true
                        editText.requestFocus()
                    } ?: run {
                        done?.let { done ->
                            done()
                        }
                    }
                    fromEditText.clearFocus()
                    fromEditText.isEnabled = false
                }
            }
        }
    }


    private fun setKeyListener(fromEditText: EditText, backToEditText: EditText) {
        fromEditText.setOnKeyListener { _, _, event ->
            if (null != event && KeyEvent.KEYCODE_DEL == event.keyCode) {
                backToEditText.isEnabled = true
                backToEditText.requestFocus()
                backToEditText.setText("")

                fromEditText.clearFocus()
                fromEditText.isEnabled = false

            }
            Log.d("KeyCode", event.keyCode.toString())
            false
        }
    }

    private fun setOnClickListener(introFragment: IntroFragment) {
        // Email
        btnSendEmail.setOnClickListener(introFragment)


        // OTP
        v.findViewById<Button>(R.id.buttonInputEmailAgain).setOnClickListener(introFragment)
    }


    private fun init() {
        // Intro
        iViewLogo = v.findViewById(R.id.iViewIconLogoIntroAfter)

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
        v.findViewById<ImageView>(R.id.iViewLetterLogoIntro).animate().alpha(0F).setDuration(200)
            .setStartDelay(2200)

        v.findViewById<ImageView>(R.id.iViewIconLogoIntroBefore).animate()
            .alpha(0F)
            .setDuration(200).setStartDelay(2500)

        Handler(Looper.getMainLooper()).postDelayed({
            BottomSheetBehavior.from(v.findViewById(R.id.rLayoutInput)).apply {
                this.state = BottomSheetBehavior.STATE_EXPANDED
                this.isDraggable = false
            }
        }, 3000)

        Handler(Looper.getMainLooper()).postDelayed({
            iViewLogo.visibility = View.VISIBLE
        }, 3500)




        setListenerEmail()
        initFocusEmail()

//        iViewLogo.animate().alpha(1F).setDuration(200).setStartDelay(3000)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.buttonSendEmail -> {
                Log.d("Email:", MainActivity.Email)

                // Test API
                val restApi = RestApi.getAPI().create(BaseService::class.java)
                val call = restApi.registerAccount("tranhuynhtanphat9380@gmail.com")
                call.enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d(
                            "Error",
                            "We can't send OTP to your Email Address. " + t.toString()
                        )
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        rLayoutWrapInputEmail.visibility = View.GONE
                        rLayoutWrapInputOTP.visibility = View.VISIBLE
                        v.findViewById<TextView>(R.id.tViewEmailInformation).text =
                            "Mã xác thực đã được gửi đến\n" + MainActivity.Email
                        resetOTP()
                        setListenerOTP()
                    }
                })
            }
            R.id.buttonInputEmailAgain -> {
                rLayoutWrapInputEmail.visibility = View.VISIBLE
                rLayoutWrapInputOTP.visibility = View.GONE
            }
        }
    }


    private fun verifyOTP(OTP: String) {
        Log.d("OTP", OTP)
        resetOTP()
        Handler(Looper.getMainLooper()).postDelayed({
            v.findNavController().navigate(R.id.action_introFragment_to_homeFragment)
        }, 1000)
    }
}

