package com.example.rewardsparta.missionManager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rewardsparta.Hunter
import com.example.rewardsparta.R
import com.example.rewardsparta.RequestChooseHunter
import com.example.rewardsparta.ResponseChooseHunter
import com.example.rewardsparta.missionManager.ChooseHunterAdapter.Companion.unAssignList
import com.example.rewardsparta.nobodyManager.NobodyActivity.Companion.hunterList
import com.example.rewardsparta.nobodyManager.NobodyActivity.Companion.itemId
import com.example.rewardsparta.home.HomeActivity
import com.example.rewardsparta.signIn.SignInActivity.Companion.token
import com.example.rewardsparta.signUp.SignUpActivity
import kotlinx.android.synthetic.main.activity_choose_hunter.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseHunterActivity : AppCompatActivity() {

    val chooseHunterAdapter = ChooseHunterAdapter()

    inner class ChooseHunterAPIFunction {

        fun sendChoice(token:String,item:RequestChooseHunter,itemId:Int) {
            val call: Call<ResponseChooseHunter> = SignUpActivity.ApiClient.getClient.sendChoice(token,item,itemId)
            call.enqueue(object : Callback<ResponseChooseHunter> {
                override fun onResponse(
                    call: Call<ResponseChooseHunter>?,
                    response: Response<ResponseChooseHunter>?) {

                    println("********${response!!.code()}")
                    if(response!!.isSuccessful){
                        println("choose success")
                        Toast.makeText(this@ChooseHunterActivity,"選擇成功",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@ChooseHunterActivity, HomeActivity::class.java))
                    }

                }
                override fun onFailure(call: Call<ResponseChooseHunter>?, t: Throwable?) {
                    println("test $t")     //t會報錯
                    Log.e("error","test")

                }
            })
        }

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_hunter)

        unAssignList = hunterList
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = chooseHunterAdapter


        ChooseHunterAdapter.setToClick(object :
            ChooseHunterAdapter.mItemClickListener {
            override fun toClick(items: Hunter) {
                //實作點擊事件

                ChooseHunterAPIFunction().sendChoice(token,RequestChooseHunter(items.user_rewards_id), itemId)

            }
        })
    }
}
