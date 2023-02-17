package com.example.ae_chat_sdk.acti.BottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ae_chat_sdk.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmailBottomFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_email, container, false)




        return view
    }
}