package com.example.ae_chat_sdk.acti.intro


import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
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
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    // Context
    lateinit var context: Context

    // Intro
//    lateinit var ibLogo: TextView

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        context = applicationContext

        val radius: Float = 20f;

        val decorView: View = window.decorView
        // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        val rootView: ViewGroup = decorView.findViewById(android.R.id.content)

        // Optional:
        // Set drawable to draw in the beginning of each blurred frame.
        // Can be used in case your layout has a lot of transparent space and your content
        // gets a too low alpha value after blur is applied.
        val windowBackground: Drawable = decorView.background


        findViewById<BlurView>(R.id.blurView).setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)


        init()
        showInputEmail()
        setListenerOTP()
        checkNewLogin()
    }

    private fun init() {
        // Intro
//        ibLogo = findViewById(R.id.ibIconLogoIntroAfter)

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


        setButtonOnClickListener()

    }

    private fun setButtonOnClickListener() {
        // Email
        btnSendEmail.setOnClickListener(this)

        // OTP
        findViewById<Button>(R.id.bInputEmailAgain).setOnClickListener(this)
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
                    MainActivity.Email = string.toString().trim()
                }
            }
        }
    }

    private fun showInputEmail() {
//        findViewById<ImageButton>(R.id.ibIconTalkWave).animate().alpha(0F)
//            .setDuration(200).startDelay =
//            2200
//
//        findViewById<ImageView>(R.id.ibIconChat).animate()
//            .alpha(0F)
//            .setDuration(200).startDelay = 2500

        Handler(Looper.getMainLooper()).postDelayed({
            BottomSheetBehavior.from(findViewById(R.id.rlInput)).apply {
                this.state = BottomSheetBehavior.STATE_EXPANDED
                this.isDraggable = false
            }
        }, 3000)

//        Handler(Looper.getMainLooper()).postDelayed({
//            ibLogo.visibility = View.VISIBLE
//        }, 3500)

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
            verifyOTP(MainActivity.OTP)
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

    private fun initFocusOTP() {
        inputOTP1.isEnabled = true
        inputOTP1.requestFocus()
    }

    private fun verifyOTP(OTP: String) {
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
                    val appStorage = AppStorage.getInstance(context)
                    appStorage.saveData("User", gson.toJson(response.body()?.data))


                    Log.d(
                        "Success",
                        "thanh cong $user"
                    )
                    Handler(Looper.getMainLooper()).postDelayed({
                        setStartHomeActivity()
                    }, 1000)
                }

            }
        })
//        setStartHomeActivity()
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
            if (event!!.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_DEL && fromEditText.id != R.id.etInputOTP1 && fromEditText.text.isEmpty()) {
                backToEditText.isEnabled = true
                backToEditText.requestFocus()
                backToEditText.setText("")

                fromEditText.clearFocus()
                fromEditText.isEnabled = false

            }
            false
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.bSendEmail -> {
                val call = RegisterRepository().registerByMail(MainActivity.Email)

                call.enqueue(object : Callback<MyResponse> {
                    override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                        Toast.makeText(context, "Không thể gửi mã xác thực!", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<MyResponse>,
                        response: Response<MyResponse>
                    ) {
                        rLayoutWrapInputEmail.visibility = View.GONE
                        rLayoutWrapInputOTP.visibility = View.VISIBLE
                        findViewById<TextView>(R.id.tvEmailInformation).text =
                            "Mã xác thực đã được gửi đến\n" + MainActivity.Email
                        resetOTP()
                        setListenerOTP()

                        rLayoutWrapInputEmail.visibility = View.GONE
                        rLayoutWrapInputOTP.visibility = View.VISIBLE
//                        setStartHomeActivity()

                    }
                })
                rLayoutWrapInputEmail.visibility = View.GONE
                rLayoutWrapInputOTP.visibility = View.VISIBLE
                findViewById<TextView>(R.id.tvEmailInformation).text =
                    "Mã xác thực đã được gửi đến\n" + MainActivity.Email
                resetOTP()
                setListenerOTP()

                rLayoutWrapInputEmail.visibility = View.GONE
                rLayoutWrapInputOTP.visibility = View.VISIBLE

            }
            R.id.bInputEmailAgain -> {
                rLayoutWrapInputEmail.visibility = View.VISIBLE
                rLayoutWrapInputOTP.visibility = View.GONE
            }
        }
    }


    private fun checkNewLogin() {
        val appStorage = context.let { AppStorage.getInstance(it) }
        val userString = appStorage.getData("User", "").toString()
        if (userString.length > 10) {
            setStartHomeActivity()
        }

    }

    private fun setStartHomeActivity() {
        val intent: Intent = Intent(this, HomeActivity::class.java)
        this.startActivity(intent)
    }
}
