package com.example.ae_chat_sdk.acti.home

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.Adapter.ContactAdapter
import com.example.ae_chat_sdk.acti.Adapter.OnstreamAdapter
import com.example.ae_chat_sdk.acti.Adapter.RecentAdapter
import com.example.ae_chat_sdk.acti.IClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton

class HomeFragment : Fragment() {

    lateinit var v: View

    // RecyclerView Message Home
    lateinit var rvOnstream: RecyclerView
    lateinit var rvRecent: RecyclerView
    lateinit var rvListContact: RecyclerView


    lateinit var recent: ArrayList<String>
    lateinit var onstream: ArrayList<String>
    lateinit var contact: ArrayList<String>

    lateinit var rLayoutBottomSheetHome: RelativeLayout
    lateinit var rLayoutMessageHome: RelativeLayout
    lateinit var rLayoutBottomSheetListContact: RelativeLayout

    // BottomSheet
    lateinit var bottomSheetHomeBehavior: BottomSheetBehavior<View>
    lateinit var bottomSheetContactBehavior: BottomSheetBehavior<View>

    //
    lateinit var tvPagename: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

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


        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)

        init()
        setButtonOnClickListener()
        renderDataRecyclerView()
        setBottomSheetBehaviorHome()


        return v
    }

    private fun setButtonOnClickListener() {
        v.findViewById<MaterialButton>(R.id.buttonListContact)
            .setOnClickListener(View.OnClickListener {
                // show Bottom Sheet Contact
                setBottomSheetBehaviorContact()
                v.findViewById<RelativeLayout>(R.id.rLayoutMenuOption).animate().alpha(0F)
                    .setDuration(0).startDelay = 0
                tvPagename.text = "Danh sách liên hệ"
                tvPagename.setTextColor(Color.parseColor("#80FFFFFF"))
                rLayoutBottomSheetListContact.animate().alpha(1F)
                    .setDuration(0).startDelay = 0
                true
            })
    }


    private fun init() {
        rLayoutBottomSheetHome = v.findViewById(R.id.rLayoutBottomSheetHome)
        rLayoutMessageHome = v.findViewById(R.id.rLayoutMessageHome)
        rLayoutBottomSheetListContact = v.findViewById(R.id.rLayoutBottomSheetListContact)

        rvOnstream = v.findViewById(R.id.rViewHorizonalOnstream)
        rvRecent = v.findViewById(R.id.rViewVerticalRecent)
        rvListContact = v.findViewById(R.id.rViewHorizonalContact)

        bottomSheetHomeBehavior = BottomSheetBehavior.from(rLayoutBottomSheetHome)
        bottomSheetContactBehavior = BottomSheetBehavior.from(rLayoutBottomSheetListContact)

        tvPagename = v.findViewById(R.id.tViewPagename)
    }

    private fun renderDataRecyclerView() {
        rvOnstream.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rvOnstream.adapter = OnstreamAdapter(onstream, object : IClickListener {
            override fun clickItem(itemObject: String) {
                Toast.makeText(context, itemObject, Toast.LENGTH_SHORT).show()
            }
        })

        rvRecent.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvRecent.adapter = RecentAdapter(recent, object : IClickListener {
            override fun clickItem(itemObject: String) {
                Toast.makeText(context, itemObject, Toast.LENGTH_SHORT).show()
            }
        })

        rvListContact.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvListContact.adapter = ContactAdapter(contact, object : IClickListener {
            override fun clickItem(itemObject: String) {
                Toast.makeText(context, itemObject, Toast.LENGTH_SHORT).show()
            }
        })
        rvListContact.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
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
                        v.findViewById<RelativeLayout>(R.id.rLayoutMessageHome).animate().alpha(1F)
                            .setDuration(100).startDelay = 0
                        v.findViewById<RelativeLayout>(R.id.rLayoutMenuOption).animate().alpha(0F)
                            .setDuration(500).startDelay = 0

                    }
                    else -> {
                        v.findViewById<RelativeLayout>(R.id.rLayoutMessageHome).animate().alpha(0F)
                            .setDuration(100).startDelay = 0
                        v.findViewById<RelativeLayout>(R.id.rLayoutMenuOption).animate().alpha(1F)
                            .setDuration(500).startDelay = 0
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun setBottomSheetBehaviorContact() {
        bottomSheetContactBehavior.apply {
            this.state = BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheetContactBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        rLayoutBottomSheetListContact.animate().alpha(1F)
                            .setDuration(0).startDelay = 0
                        v.findViewById<RelativeLayout>(R.id.rLayoutMenuOption).animate().alpha(0F)
                            .setDuration(0).startDelay = 0
                        tvPagename.text = "Danh sách liên hệ"
                        tvPagename.setTextColor(Color.parseColor("#80FFFFFF"))
                    }
                    else -> {
                        tvPagename.text = "Username"
                        v.findViewById<RelativeLayout>(R.id.rLayoutMenuOption).animate().alpha(1F)
                            .setDuration(0).startDelay = 0
                        tvPagename.setTextColor(Color.parseColor("#FFFFFF"))
                        rLayoutBottomSheetListContact.animate().alpha(0F)
                            .setDuration(1000).startDelay = 0
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }


}


