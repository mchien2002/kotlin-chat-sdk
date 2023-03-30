package com.example.ae_chat_sdk.acti.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import com.example.ae_chat_sdk.MainActivity
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.home.HomeActivity
import com.example.ae_chat_sdk.data.api.reponsitory.RegisterRepository
import com.example.ae_chat_sdk.data.model.MyResponse
import com.example.ae_chat_sdk.data.model.User
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {

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
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
        showInputEmail()
    }

    private fun init() {
        // Intro
        iViewLogo = findViewById(R.id.iViewIconLogoIntroAfter)

        // Email
        btnSendEmail = findViewById(R.id.buttonSendEmail)
        eTextEmail = findViewById(R.id.eTextEmail)
        tLayoutInputEmail = findViewById(R.id.tLayoutInputEmail)
        rLayoutWrapInputEmail = findViewById(R.id.rLayoutWrapInputEmail)

        // OTP
        rLayoutWrapInputOTP = findViewById(R.id.rLayoutWrapTextInputOTP)
        inputOTP1 = findViewById(R.id.eTextInputOTP1)
        inputOTP2 = findViewById(R.id.eTextInputOTP2)
        inputOTP3 = findViewById(R.id.eTextInputOTP3)
        inputOTP4 = findViewById(R.id.eTextInputOTP4)
        inputOTP5 = findViewById(R.id.eTextInputOTP5)
        inputOTP6 = findViewById(R.id.eTextInputOTP6)

        // Email
        btnSendEmail.setOnClickListener(this)

        // OTP
        findViewById<Button>(R.id.buttonInputEmailAgain).setOnClickListener(this)

    }

    private fun setListenerEmail() {
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
    private fun showInputEmail() {
        findViewById<ImageView>(R.id.iViewLetterLogoIntro).animate().alpha(0F).setDuration(200)
            .setStartDelay(2200)

        findViewById<ImageView>(R.id.iViewIconLogoIntroBefore).animate()
            .alpha(0F)
            .setDuration(200).setStartDelay(2500)

        Handler(Looper.getMainLooper()).postDelayed({
            BottomSheetBehavior.from(findViewById(R.id.rLayoutInput)).apply {
                this.state = BottomSheetBehavior.STATE_EXPANDED
                this.isDraggable = false
            }
        }, 3000)

        Handler(Looper.getMainLooper()).postDelayed({
            iViewLogo.visibility = View.VISIBLE
        }, 3500)




        setListenerEmail()
        eTextEmail.isEnabled = true


//        iViewLogo.animate().alpha(1F).setDuration(200).setStartDelay(3000)
    }

    private fun setListenerOTP() {
        setTextChangeListener(inputOTP1, inputOTP2)
        setTextChangeListener(inputOTP2, inputOTP3)
        setTextChangeListener(inputOTP3, inputOTP4)
        setTextChangeListener(inputOTP4, inputOTP5)
        setTextChangeListener(inputOTP5, inputOTP6)
        setTextChangeListener(inputOTP6, done = {
            MainActivity.OTP = inputOTP1.text.toString().trim() + inputOTP2.text.toString()
                .trim() + inputOTP3.text.toString().trim() + inputOTP4.text.toString()
                .trim() + inputOTP5.text.toString().trim() + inputOTP6.text.toString().trim()
            Toast.makeText(applicationContext, MainActivity.OTP, Toast.LENGTH_SHORT).show()
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

    private fun initFocusOTP() {
        inputOTP1.isEnabled = true
        inputOTP1.requestFocus()
    }

    private fun verifyOTP(OTP: String) {
        Log.d("OTP", OTP)
        resetOTP()
        val call = RegisterRepository().verifyOTP(MainActivity.Email, MainActivity.OTP)
        call.enqueue(object : Callback<MyResponse> {
            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                Log.d(
                    "Error",
                    t.toString()
                )
            }

            override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                if (response.code() == 200) {
                    //val user: User = response.body()?.data as User
                    val gson = Gson()
                    val type = object : TypeToken<User>() {}.type
                    val user = gson.fromJson<User>(gson.toJson(response.body()?.data), type)
                    val appStorage = AppStorage.getInstance(applicationContext!!)
                    appStorage.saveData("User", gson.toJson(response.body()?.data))


                    Log.d(
                        "Success",
                        "thanh cong $user"
                    )
                    Handler(Looper.getMainLooper()).postDelayed({
//                        findNavController().navigate(R.id.action_introFragment_to_homeFragment)
                        setStartHomeActivity()
                    }, 1000)
                }

            }
        })

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
            if (event!!.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_DEL && fromEditText.id != R.id.eTextInputOTP1 && fromEditText.text.isEmpty()) {
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

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.buttonSendEmail -> {
                val call = RegisterRepository().registerByMail(MainActivity.Email)

                call.enqueue(object : Callback<MyResponse> {
                    override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                        Log.d(
                            "Error",
                            "We can't send OTP to your Email Address. " + t.toString()
                        )
                    }

                    override fun onResponse(
                        call: Call<MyResponse>,
                        response: Response<MyResponse>
                    ) {
                        rLayoutWrapInputEmail.visibility = View.GONE
                        rLayoutWrapInputOTP.visibility = View.VISIBLE

//                        findNavController().navigate(R.id.action_introFragment_to_homeFragment)

//                        findViewById<TextView>(R.id.tViewEmailInformation).text =
//                            "Mã xác thực đã được gửi đến\n" + MainActivity.Email
//                        resetOTP()
//                        setListenerOTP()

                        setStartHomeActivity()
                    }
                })

            }
            R.id.buttonInputEmailAgain -> {
                rLayoutWrapInputEmail.visibility = View.VISIBLE
                rLayoutWrapInputOTP.visibility = View.GONE
            }
        }
    }

    private fun setStartHomeActivity(){
        val intent: Intent = Intent(this, HomeActivity::class.java)
        this.startActivity(intent);
    }
}