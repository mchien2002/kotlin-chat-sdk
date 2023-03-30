package com.example.ae_chat_sdk.acti.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.adapter.ContactAdapter
import com.example.ae_chat_sdk.acti.adapter.OnstreamAdapter
import com.example.ae_chat_sdk.acti.adapter.RecentAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton

class HomeActivity : AppCompatActivity() {

    // Context
    lateinit var context: Context

    // RecyclerView Message Home
    lateinit var rvOnstream: RecyclerView
    lateinit var rvRecent: RecyclerView
    lateinit var rvListContact: RecyclerView


    lateinit var recent: ArrayList<String>
    lateinit var onstream: ArrayList<String>
    lateinit var contact: ArrayList<String>

    lateinit var rLayoutBottomSheetHome: RelativeLayout

    // BottomSheet
    lateinit var bottomSheetHomeBehavior: BottomSheetBehavior<View>

    // Name Page
    lateinit var tvPagename: TextView

    var listContact: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // set data for list on Stream
        onstream = ArrayList()
        for (i in 1..20) {
            onstream.add("username # $i")
        }

        // set data for recent chat
        recent = ArrayList()
        for (i in 1..20) {
            recent.add("username # $i")
        }

        contact = ArrayList()
        for (i in 1..20) {
            contact.add("username # $i")
        }

        context = applicationContext

        init()
        setButtonOnClickListener()
        renderDataRecyclerView()
        setBottomSheetBehaviorHome()
    }

    private fun init() {
        rLayoutBottomSheetHome = findViewById(R.id.rlBottomSheetHome)
//        rLayoutBottomSheetListContact = findViewById(R.id.rlBottomSheetListContact)

        rvOnstream = findViewById(R.id.rvHorizonalOnstream)
        rvRecent = findViewById(R.id.rvVerticalRecent)
        rvListContact = findViewById(R.id.rvHorizonalContact)

        bottomSheetHomeBehavior = BottomSheetBehavior.from(rLayoutBottomSheetHome)

        tvPagename = findViewById(R.id.tvPageName)
    }

    private fun setButtonOnClickListener() {
        findViewById<MaterialButton>(R.id.mbListContact)
            .setOnClickListener(View.OnClickListener {
                listContact = true
                bottomSheetHomeBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                true
            })
    }

    private fun setBottomSheetBehaviorHome() {
        bottomSheetHomeBehavior.apply {
            this.state = BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheetHomeBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {

                        findViewById<RelativeLayout>(R.id.rlMenuOption).animate().alpha(0F)
                            .setDuration(500).startDelay = 0
                        findViewById<RelativeLayout>(R.id.rlMessageHome).animate().alpha(1F)
                            .setDuration(100).startDelay = 0

                        if (!listContact) {
                            tvPagename.setTextColor(Color.parseColor("#FFFFFF"))
                            tvPagename.text = "Username"
                            findViewById<RelativeLayout>(R.id.rlHome).visibility = View.VISIBLE
                            findViewById<RelativeLayout>(R.id.rlListContact).visibility =
                                View.GONE
                        } else {
                            tvPagename.setTextColor(Color.parseColor("#80FFFFFF"))
                            tvPagename.text = "Danh sách liên hệ"
                            findViewById<RelativeLayout>(R.id.rlHome).visibility = View.GONE
                            findViewById<RelativeLayout>(R.id.rlListContact).visibility =
                                View.VISIBLE
                        }
                        listContact = false
                    }
                    else -> {

                        findViewById<RelativeLayout>(R.id.rlHome).visibility = View.VISIBLE
                        findViewById<RelativeLayout>(R.id.rlListContact).visibility = View.GONE
                        findViewById<RelativeLayout>(R.id.rlMenuOption).animate().alpha(1F)
                            .setDuration(500).startDelay = 0
                        findViewById<RelativeLayout>(R.id.rlMessageHome).animate().alpha(0F)
                            .setDuration(100).startDelay = 0

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun renderDataRecyclerView() {
        rvOnstream.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rvOnstream.adapter = OnstreamAdapter(onstream, context)

        rvRecent.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvRecent.adapter = RecentAdapter(recent, context)

        rvListContact.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvListContact.adapter = ContactAdapter(contact, context)
        rvListContact.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
    }

}