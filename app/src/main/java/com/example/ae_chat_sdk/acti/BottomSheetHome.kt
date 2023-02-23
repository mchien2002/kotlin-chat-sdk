package com.example.ae_chat_sdk.acti

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.Adapter.OnstreamAdapter
import com.example.ae_chat_sdk.acti.Adapter.RecentAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetHome(
    val onStreamList: ArrayList<String>,
    val recentList: ArrayList<String>,
    val iClickListener: IClickListener
) : BottomSheetDialogFragment() {

    lateinit var bottomDialog: Dialog
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    lateinit var rootView: View


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)

//        val view: View =
//            LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet_main, null)
//        bottomSheetDialog.setContentView(view)


//        val rvOnStream: RecyclerView = view.findViewById(R.id.rViewHorizonalOnstream)
//        val horizontalLinearLayoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        rvOnStream.layoutManager = horizontalLinearLayoutManager
//        val onstreamAdapter = OnstreamAdapter(onStreamList, object : IClickListener {
//            override fun clickItem(itemObject: String) {
//                fun clickItem(itemObject: String) {
//                    iClickListener.clickItem(itemObject)
//                }
//            }
//        })
//        rvOnStream.adapter = onstreamAdapter
//
//        val rvRecent: RecyclerView = view.findViewById(R.id.rViewVerticalRecent)
//        val verticalLinearLayoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        rvRecent.layoutManager = verticalLinearLayoutManager
//        val recentAdapter = RecentAdapter(recentList, object : IClickListener {
//            override fun clickItem(itemObject: String) {
//                fun clickItem(itemObject: String) {
//                    iClickListener.clickItem(itemObject)
//                }
//            }
//        })
//        rvRecent.adapter = recentAdapter

        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.layout_bottom_sheet_home, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bottomSheetBehavior=BottomSheetBehavior.from(view.parent as View)
//        bottomSheetBehavior.state=BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.peekHeight=500
        bottomSheetBehavior.isDraggable=true



        val rvOnStream: RecyclerView = view.findViewById(R.id.rViewHorizonalOnstream)
        val horizontalLinearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvOnStream.layoutManager = horizontalLinearLayoutManager
        val onstreamAdapter = OnstreamAdapter(onStreamList, object : IClickListener {
            override fun clickItem(itemObject: String) {
                fun clickItem(itemObject: String) {
                    iClickListener.clickItem(itemObject)
                }
            }
        })
        rvOnStream.adapter = onstreamAdapter

        val rvRecent: RecyclerView = view.findViewById(R.id.rViewVerticalRecent)
        val verticalLinearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvRecent.layoutManager = verticalLinearLayoutManager
        val recentAdapter = RecentAdapter(recentList, object : IClickListener {
            override fun clickItem(itemObject: String) {
                fun clickItem(itemObject: String) {
                    iClickListener.clickItem(itemObject)
                }
            }
        })
        rvRecent.adapter = recentAdapter
    }
}