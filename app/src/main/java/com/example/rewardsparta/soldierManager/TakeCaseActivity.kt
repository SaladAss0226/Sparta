package com.example.rewardsparta.soldierManager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rewardsparta.History
import com.example.rewardsparta.R
import com.example.rewardsparta.ResponseHistory
import com.example.rewardsparta.reWard.TakeCaseMissionItem
import com.example.rewardsparta.reWard.soldierTakeCaseList
import com.example.rewardsparta.signIn.SignInActivity.Companion.token
import com.example.rewardsparta.signUp.SignUpActivity
import kotlinx.android.synthetic.main.activity_take_case.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TakeCaseActivity : AppCompatActivity() {

    val takeCaseAdapter = TakeCaseAdapter()

    companion object{
        var rewardId = 0
    }

    inner class TakeCaseAPIFunction {

        fun getHistory() {
            val call: Call<ResponseHistory> = SignUpActivity.ApiClient.getClient.getHistory(token)
            call.enqueue(object : Callback<ResponseHistory> {
                override fun onResponse(
                    call: Call<ResponseHistory>?,
                    response: Response<ResponseHistory>?) {

                    println("get history success")
                    val resp = response?.body()

                    val history: List<History> = resp?.history ?: return

                    soldierTakeCaseList.clear()
                    takeCaseAdapter.unAssignList = history.map {
                        TakeCaseMissionItem(
                            it.category,
                            it.chosen,
                            it.descript,
                            it.done,
                            it.fee,
                            it.name,
                            it.reported_descript,
                            it.reward_id
                        )
                    }.also {
                        soldierTakeCaseList.addAll(it)
                    }

                }
                override fun onFailure(call: Call<ResponseHistory>?, t: Throwable?) {
                    println("test $t")     //t會報錯
                    Log.e("error","test")
                }
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_case)

        TakeCaseAPIFunction().getHistory()

        btn_success.setOnClickListener {
            takeCaseAdapter.unAssignList = soldierTakeCaseList.filter { it.done==1 }
//            takeCaseAdapter.notifyDataSetChanged()
        }
        btn_fail.setOnClickListener {
            takeCaseAdapter.unAssignList = soldierTakeCaseList.filter { it.done==0 }
//            takeCaseAdapter.notifyDataSetChanged()
        }
        btn_have_submit.setOnClickListener {     //已回報但尚未公布(進行中)
            takeCaseAdapter.unAssignList = soldierTakeCaseList.filter { it.chosen==1 && it.done==null && it.reported_descript!==null}
//            takeCaseAdapter.notifyDataSetChanged()
        }
        btn_havenot_submit.setOnClickListener {  //未回報(進行中)
            takeCaseAdapter.unAssignList = soldierTakeCaseList.filter { it.chosen==1 && it.done==null && it.reported_descript==null}
//            takeCaseAdapter.notifyDataSetChanged()
        }
        btn_unAssign.setOnClickListener {
            takeCaseAdapter.unAssignList = soldierTakeCaseList.filter { it.chosen==0 }
//            takeCaseAdapter.notifyDataSetChanged()
        }

            TakeCaseAdapter.setToClick(object :TakeCaseAdapter.mItemClickListener {
            override fun toClick(items: TakeCaseMissionItem) {
                if(items.chosen==1 && items.done==null && items.reported_descript==null){   //若此任務為未回報(進行中)
                    rewardId = items.reward_id
                    startActivity(Intent(this@TakeCaseActivity,SubmitActivity::class.java))
                }
                else Toast.makeText(this@TakeCaseActivity,"此任務無法進行任務回報",Toast.LENGTH_SHORT).show()

            }
        })

        recyclerview.layoutManager = LinearLayoutManager(this@TakeCaseActivity)
        recyclerview.adapter = takeCaseAdapter

    }

    override fun onBackPressed() {
        startActivity(Intent(this,SoldierActivity::class.java))
    }

}
