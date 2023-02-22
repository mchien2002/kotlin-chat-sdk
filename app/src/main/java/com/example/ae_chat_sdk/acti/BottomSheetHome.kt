package com.example.ae_chat_sdk.acti

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ae_chat_sdk.R
import com.example.ae_chat_sdk.acti.Adapter.OnstreamAdapter
import com.example.ae_chat_sdk.acti.Adapter.RecentAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetHome(
    val onStreamList: ArrayList<String>,
    val recentList: ArrayList<String>,
    val iClickListener: IClickListener
) : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState).apply {
            (this as? BottomSheetDialog)
                ?.behavior
                ?.state=BottomSheetBehavior.STATE_EXPANDED

        }
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet_main, null)
        bottomSheetDialog.setContentView(view)

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

        return bottomSheetDialog
    }
}