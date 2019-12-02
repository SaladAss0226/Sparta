package com.example.rewardsparta.reWard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.rewardsparta.R
import com.example.rewardsparta.RequestAddMission
import com.example.rewardsparta.ResponseAddMission
import com.example.rewardsparta.reWard.ReWardActivity.Companion.rewardAdapter
import com.example.rewardsparta.signIn.SignInActivity.Companion.token
import com.example.rewardsparta.signUp.SignUpActivity
import kotlinx.android.synthetic.main.activity_add_mission.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMissionActivity : AppCompatActivity() {

    inner class AddMissionAPIFunction {

        fun sendAddMission(item:RequestAddMission) {
            val call: Call<ResponseAddMission> = SignUpActivity.ApiClient.getClient.sendAddMission(token,item)
            call.enqueue(object : Callback<ResponseAddMission> {
                override fun onResponse(
                    call: Call<ResponseAddMission>?,
                    response: Response<ResponseAddMission>?) {

                    println(response?.code())
                    println(token)

                    if(response!!.isSuccessful) {
                        println("post mission success")
                        val response = response?.body()

                        missionList.add(
                            MissionItem(
                                response.reward.id,
                                response.reward.descript,
                                response.reward.name,
                                null,
                                null,
                                response.reward.budget,
                                response.reward.category,
                                null,
                                0,
                                response.reward.user_id
                            )
                        )
                        rewardAdapter.notifyDataSetChanged()
                        startActivity(Intent(this@AddMissionActivity,ReWardActivity::class.java))
                    }



                }
                override fun onFailure(call: Call<ResponseAddMission>?, t: Throwable?) {
                    println("test $t")     //t會報錯
                    Log.e("error","test")

                }
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mission)

        btn_send.setOnClickListener {

            if(et_name.text.length>0 && et_budget.text.length>0 && et_descript.text.length>0 && radioGroup.checkedRadioButtonId!==null){
                AddMissionAPIFunction().sendAddMission(RequestAddMission(
                    et_name.text.toString(),
                    if (radioGroup.checkedRadioButtonId == R.id.rb_kill) 1 else 2,
                    et_descript.text.toString(),
                    et_budget.text.toString().toInt()
                ))
            }
            else Toast.makeText(this,"請輸入所有欄位",Toast.LENGTH_SHORT).show()

        }





    }
}
