package com.example.ae_chat_sdk.acti.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ae_chat_sdk.MainActivity
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.Adapter.OnstreamAdapter
import com.example.ae_chat_sdk.acti.Adapter.RecentAdapter
import com.example.ae_chat_sdk.acti.BottomSheetHome
import com.example.ae_chat_sdk.acti.IClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.w3c.dom.Text

class HomeFragment : Fragment() {

    lateinit var v: View

    // RecyclerView Message Home
    lateinit var rvOnstream: RecyclerView
    lateinit var rvRecent: RecyclerView
    lateinit var recent: ArrayList<String>
    lateinit var onstream: ArrayList<String>

    // Layout Message Home
    lateinit var lLayoutBottomSheetHome: RelativeLayout
    lateinit var rLayoutMessageHome: RelativeLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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


        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)

        init()
        renderDataRecyclerView()
        setBottomSheetBehavior()
        v.findViewById<RelativeLayout>(R.id.rLayoutMenuOption).animate().alpha(0F)
            .setDuration(0).startDelay = 0


        return v
    }


    fun init() {
        lLayoutBottomSheetHome = v.findViewById(R.id.lLayoutBottomSheetHome)
        rLayoutMessageHome = v.findViewById(R.id.rLayoutMessageHome)

        rvOnstream = v.findViewById(R.id.rViewHorizonalOnstream)
        rvRecent = v.findViewById(R.id.rViewVerticalRecent)

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
    }

    private fun setBottomSheetBehavior() {
        BottomSheetBehavior.from(lLayoutBottomSheetHome).apply {
            this.state = BottomSheetBehavior.STATE_EXPANDED
        }
        BottomSheetBehavior.from(lLayoutBottomSheetHome)
            .addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            v.findViewById<RelativeLayout>(R.id.rLayoutMessageHome).animate()
                                .alpha(1F).setDuration(100).startDelay = 0
                            v.findViewById<TextView>(R.id.tViewUsername).animate().alpha(1F)
                                .setDuration(500).startDelay = 0
                            v.findViewById<RelativeLayout>(R.id.rLayoutMenuOption).animate()
                                .alpha(0F).setDuration(0).startDelay = 0

                        }
                        else -> {
                            v.findViewById<RelativeLayout>(R.id.rLayoutMessageHome).animate()
                                .alpha(0F).setDuration(100).startDelay = 0
                            v.findViewById<TextView>(R.id.tViewUsername).animate().alpha(0F)
                                .setDuration(500).startDelay = 0
                            v.findViewById<RelativeLayout>(R.id.rLayoutMenuOption).animate()
                                .alpha(1F).setDuration(0).startDelay = 0
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
    }

}


