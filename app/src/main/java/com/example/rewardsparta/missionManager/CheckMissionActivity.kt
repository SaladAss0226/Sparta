package com.example.rewardsparta.missionManager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.rewardsparta.R
import com.example.rewardsparta.RequestEndMission
import com.example.rewardsparta.ResponseEndMission
import com.example.rewardsparta.nobodyManager.NobodyActivity.Companion.postId
import com.example.rewardsparta.nobodyManager.NobodyActivity.Companion.reportedDescript
import com.example.rewardsparta.nobodyManager.NobodyActivity.Companion.reportedImage
import com.example.rewardsparta.home.HomeActivity
import com.example.rewardsparta.signIn.SignInActivity.Companion.token
import com.example.rewardsparta.signUp.SignUpActivity
import kotlinx.android.synthetic.main.activity_check_mission.*
import kotlinx.android.synthetic.main.dialog_end_mission.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckMissionActivity : AppCompatActivity() {

    inner class EndMissionAPIFunction {

        fun endMission(item:RequestEndMission) {
            val call: Call<ResponseEndMission> = SignUpActivity.ApiClient.getClient.endMission(token,item,postId )
            call.enqueue(object : Callback<ResponseEndMission> {
                override fun onResponse(
                    call: Call<ResponseEndMission>?,
                    response: Response<ResponseEndMission>?) {

                    println("********************${response!!.code()}")
                    if(response!!.isSuccessful){
                        println("End Mission success")
                        Toast.makeText(this@CheckMissionActivity,"結案成功!",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@CheckMissionActivity,HomeActivity::class.java))
                    }

                }
                override fun onFailure(call: Call<ResponseEndMission>?, t: Throwable?) {
                    println("test $t")     //t會報錯
                    Log.e("error","test")

                }
            })
        }

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_mission)

        println("***********${reportedImage}")
        Glide.with(this)
            .load(reportedImage)
            .into(imgv_download)

        tv_reported_descript.text = reportedDescript

        btn_success.setOnClickListener {            //任務成功
            val dialog = DialogEndMission(this)     //再把指定購買的項目items傳過去
            dialog.show()
            dialog.btn_accept.setOnClickListener {
                if (dialog.dialog_et_key.text.length >= 1) {
                    EndMissionAPIFunction().endMission(
                        RequestEndMission(1, dialog.dialog_et_key.text.toString())
                    )
                } else Toast.makeText(this, "請輸入金鑰", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                println("*******************${dialog.dialog_et_key.text}")
            }
        }


        btn_fail.setOnClickListener {                 //任務失敗
            val dialog = DialogEndMission(this)     //再把指定購買的項目items傳過去
            dialog.show()
            dialog.btn_accept.setOnClickListener {
                if (dialog.dialog_et_key.text.length >= 1) {
                    EndMissionAPIFunction().endMission(
                        RequestEndMission(0, dialog.dialog_et_key.text.toString())
                    )
                } else Toast.makeText(this, "請輸入金鑰", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

    }

}
