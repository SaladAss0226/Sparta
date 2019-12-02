package com.example.rewardsparta.reWard

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.rewardsparta.R
import kotlinx.android.synthetic.main.dialog_take_case.*

class DialogTakeCase(context: Context, val items:MissionItem) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_take_case)


        //設定dialog的任務名稱 圖片
        dialog_imgv.setImageResource(if(items.category==1) R.drawable.knife else R.drawable.icon_collect)
        dialog_tv_title.text = items.name

        btn_cancel.setOnClickListener { dismiss() }


    }
}