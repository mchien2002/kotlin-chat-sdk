package com.example.ae_chat_sdk.acti.home

import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.Adapter.OnstreamAdapter
import com.example.ae_chat_sdk.acti.Adapter.RecentAdapter
import org.w3c.dom.Text

class HomeFragment : Fragment() {

    lateinit var rvOnstream: RecyclerView
    lateinit var onstream:ArrayList<String>

    lateinit var rvRecent: RecyclerView
    lateinit var recent:ArrayList<String>

    lateinit var layoutHome: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        onstream=ArrayList()
        for(i in 1..20){
            onstream.add("username # $i")
        }


        recent=ArrayList()
        for(i in 1..20){
            recent.add("username # $i")
        }

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)

        layoutHome=v.findViewById(R.id.layoutHome)

        rvOnstream=v.findViewById(R.id.horizonalOnstream)
        rvOnstream.layoutManager=LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
        rvOnstream.adapter=OnstreamAdapter(onstream)

        rvRecent=v.findViewById(R.id.verticalRecent)
        rvRecent.layoutManager=LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        rvRecent.adapter= RecentAdapter(onstream)


        return v

    }




}


