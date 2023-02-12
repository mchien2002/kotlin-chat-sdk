package com.example.ae_chat_sdk.acti.login


import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.example.ae_chat_sdk.MainActivity
import com.example.ae_chat_sdk.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EmailFragment : Fragment(), View.OnClickListener, TextWatcher {

    lateinit var btnSendEmail: Button

    lateinit var editEmail: TextInputEditText

    lateinit var layoutEditEmail: TextInputLayout

    lateinit var inputEmailContainer: ViewGroup


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_email, container, false)

        // Declare
        inputEmailContainer = v.findViewById<ViewGroup>(R.id.inputEmailTransition)
        btnSendEmail = inputEmailContainer.findViewById<Button>(R.id.buttonVerify)
        editEmail = inputEmailContainer.findViewById<TextInputEditText>(R.id.editEmail)
        layoutEditEmail = inputEmailContainer.findViewById<TextInputLayout>(R.id.inputEmailLayout)

        // Listen
        btnSendEmail.setOnClickListener(this)
        editEmail.addTextChangedListener(this)
        return v
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonVerify -> {
                v.findNavController().navigate(R.id.action_emailFragment_to_OTPFragment)
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        MainActivity.Email = s.toString().trim()

        fun String.isValidEmail() =
            !TextUtils.isEmpty(MainActivity.Email) && Patterns.EMAIL_ADDRESS.matcher(MainActivity.Email)
                .matches()


        if (!MainActivity.Email.isValidEmail()) {
            layoutEditEmail.error = "Email chưa đúng cú pháp!"
            btnSendEmail.visibility = View.GONE
        } else {
            layoutEditEmail.isErrorEnabled = false
            btnSendEmail.visibility = View.VISIBLE
        }
    }

    fun setButtonEnable(btn: MaterialButton) {
        btn.setBackgroundColor(Color.parseColor("#FFFFB800"))
    }
}