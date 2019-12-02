package com.example.rewardsparta.missionManager

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.rewardsparta.R
import kotlinx.android.synthetic.main.dialog_take_case.*

class DialogEndMission(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_end_mission)

        btn_cancel.setOnClickListener { dismiss() }


    }
}