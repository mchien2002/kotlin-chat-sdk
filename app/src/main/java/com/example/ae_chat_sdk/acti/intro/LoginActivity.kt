package com.example.ae_chat_sdk.acti.intro


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.home.HomeActivity
import com.example.ae_chat_sdk.data.api.reponsitory.RegisterRepository
import com.example.ae_chat_sdk.data.model.MyResponse
import com.example.ae_chat_sdk.data.model.User
import com.example.ae_chat_sdk.data.storage.AppStorage
import com.example.ae_chat_sdk.utils.BlurUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eightbitlab.com.blurview.BlurView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var email: String
    lateinit var OTP: String

    // Context
    lateinit var context: Context

    // Email
    lateinit var btnSendEmail: Button
    lateinit var eTextEmail: EditText
    lateinit var tLayoutInputEmail: TextInputLayout
    lateinit var rLayoutWrapInputEmail: LinearLayout


    // OTP
    lateinit var rLayoutWrapInputOTP: RelativeLayout
    lateinit var inputOTP1: EditText
    lateinit var inputOTP2: EditText
    lateinit var inputOTP3: EditText
    lateinit var inputOTP4: EditText
    lateinit var inputOTP5: EditText
    lateinit var inputOTP6: EditText

    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        context = applicationContext
        checkLogged()

        BlurUtils.setBlur(this.window, listOf(findViewById<BlurView>(R.id.blurView)), context)

        init()
        showBottomSheet()
        setListenerOTP()
    }

    private fun init() {
        // Email
        btnSendEmail = findViewById(R.id.bSendEmail)
        eTextEmail = findViewById(R.id.etEmail)
        tLayoutInputEmail = findViewById(R.id.tlInputEmail)
        rLayoutWrapInputEmail = findViewById(R.id.llWrapInputEmail)

        // OTP
        rLayoutWrapInputOTP = findViewById(R.id.rlWrapTextInputOTP)
        inputOTP1 = findViewById(R.id.etInputOTP1)
        inputOTP2 = findViewById(R.id.etInputOTP2)
        inputOTP3 = findViewById(R.id.etInputOTP3)
        inputOTP4 = findViewById(R.id.etInputOTP4)
        inputOTP5 = findViewById(R.id.etInputOTP5)
        inputOTP6 = findViewById(R.id.etInputOTP6)

        progressBar = findViewById(R.id.progressBar)

        setButtonOnClickListener()
    }

    private fun checkLogged() {
        val appStorage = context.let { AppStorage.getInstance(it) }
        val userString = appStorage.getData("User", "").toString()
        if (userString.length > 10) {
            setStartHomeActivity()
        }

    }

    private fun setStartHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        this.startActivity(intent)
        finish()
    }

    private fun initFocusOTP() {
        inputOTP1.isEnabled = true
        inputOTP1.requestFocus()
    }

    private fun setKeyListener(fromEditText: EditText, backToEditText: EditText) {
        fromEditText.setOnKeyListener { _, _, event ->
            if (event!!.action == KeyEvent.ACTION_DOWN
                && event.keyCode == KeyEvent.KEYCODE_DEL
                && fromEditText.id != R.id.etInputOTP1
                && fromEditText.text.isEmpty()
            ) {
                backToEditText.isEnabled = true
                backToEditText.requestFocus()
                backToEditText.setText("")

                fromEditText.clearFocus()
                fromEditText.isEnabled = false
            }
            false
        }
    }

    // true -> show form input email
    // false -> show form input otp
    private fun showInput(state: Boolean) {
        if (state) {
            rLayoutWrapInputEmail.visibility = View.VISIBLE
            rLayoutWrapInputOTP.visibility = View.GONE
            return
        }
        rLayoutWrapInputEmail.visibility = View.GONE
        rLayoutWrapInputOTP.visibility = View.VISIBLE
    }

    private fun hideKeyboard() {
        // Auto hide keyboard
        if (currentFocus != null) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                0
            )
        }
    }

    private fun setButtonOnClickListener() {
        // Email
        btnSendEmail.setOnClickListener(View.OnClickListener {

            hideKeyboard()

            progressBar.visibility = View.VISIBLE
            val call = RegisterRepository().registerByMail(email)

            call.enqueue(object : Callback<MyResponse> {
                override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                    Toast.makeText(context, "Không thể gửi mã xác thực!", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(
                    call: Call<MyResponse>,
                    response: Response<MyResponse>
                ) {
                    findViewById<TextView>(R.id.tvEmailInformation).text =
                        "Mã xác thực đã được gửi đến\n" + email
                    resetOTP()
                    setListenerOTP()
                    showInput(false)
                    progressBar.visibility = View.GONE
                }
            })
        })

        // OTP
        findViewById<Button>(R.id.bInputEmailAgain).setOnClickListener(View.OnClickListener {
            showInput(true)
        })
    }

    private fun setListenerEmail() {
        eTextEmail.addTextChangedListener {
            it?.let { string ->
                fun isValidEmail() =
                    !TextUtils.isEmpty(string.toString().trim()) && Patterns.EMAIL_ADDRESS.matcher(
                        string.toString().trim()
                    ).matches()

                if (!isValidEmail()) {
                    tLayoutInputEmail.error = "Email chưa đúng cú pháp!"
                    btnSendEmail.visibility = View.GONE
                } else {
                    tLayoutInputEmail.isErrorEnabled = false
                    btnSendEmail.visibility = View.VISIBLE
                    email = string.toString().trim()
                }
            }
        }
    }

    private fun showBottomSheet() {
        Handler(Looper.getMainLooper()).postDelayed({
            BottomSheetBehavior.from(findViewById(R.id.clInput)).apply {
                this.state = BottomSheetBehavior.STATE_EXPANDED
                this.isDraggable = false
            }
        }, 1000)
        setListenerEmail()
        eTextEmail.isEnabled = true
        eTextEmail.requestFocus()
    }

    private fun setListenerOTP() {
        setTextChangeListener(inputOTP1, inputOTP2)
        setTextChangeListener(inputOTP2, inputOTP3)
        setTextChangeListener(inputOTP3, inputOTP4)
        setTextChangeListener(inputOTP4, inputOTP5)
        setTextChangeListener(inputOTP5, inputOTP6)
        setTextChangeListener(inputOTP6, done = {
            OTP = inputOTP1.text.toString().trim() + inputOTP2.text.toString()
                .trim() + inputOTP3.text.toString().trim() + inputOTP4.text.toString()
                .trim() + inputOTP5.text.toString().trim() + inputOTP6.text.toString().trim()
            verifyOTP()
        })

        setKeyListener(inputOTP2, inputOTP1)
        setKeyListener(inputOTP3, inputOTP2)
        setKeyListener(inputOTP4, inputOTP3)
        setKeyListener(inputOTP5, inputOTP4)
        setKeyListener(inputOTP6, inputOTP5)
    }

    private fun resetOTP() {
        // Disable input OTP
        inputOTP1.isEnabled = false
        inputOTP2.isEnabled = false
        inputOTP3.isEnabled = false
        inputOTP4.isEnabled = false
        inputOTP5.isEnabled = false
        inputOTP6.isEnabled = false

        // Reset text
        inputOTP1.setText("")
        inputOTP2.setText("")
        inputOTP3.setText("")
        inputOTP4.setText("")
        inputOTP5.setText("")
        inputOTP6.setText("")

        initFocusOTP()
    }

    private fun verifyOTP() {
        hideKeyboard()
        progressBar.visibility = View.VISIBLE
        resetOTP()
        val call = RegisterRepository().verifyOTP(email, OTP)
        call.enqueue(object : Callback<MyResponse> {
            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                Log.d(
                    "Error",
                    t.toString()
                )
            }

            override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                if (response.code() == 200) {
                    val gson = Gson()
                    val type = object : TypeToken<User>() {}.type
                    val user = gson.fromJson<User>(gson.toJson(response.body()?.data), type)
                    val appStorage = AppStorage.getInstance(context)
                    appStorage.saveData("User", gson.toJson(response.body()?.data))
                    Log.e("USERID5",user.userId)
                    Log.d(
                        "Success",
                        "thanh cong $user"
                    )
                    Handler(Looper.getMainLooper()).postDelayed({
                        setStartHomeActivity()
                        progressBar.visibility = View.GONE
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

}
