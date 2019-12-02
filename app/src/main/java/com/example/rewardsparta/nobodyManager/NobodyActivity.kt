package com.example.rewardsparta.nobodyManager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rewardsparta.Hunter
import com.example.rewardsparta.R
import com.example.rewardsparta.ResponseHistory
import com.example.rewardsparta.missionManager.CheckMissionActivity
import com.example.rewardsparta.missionManager.ChooseHunterActivity
import com.example.rewardsparta.nobodyManager.NobodyAdapter.Companion.unAssignList
import com.example.rewardsparta.home.HomeActivity
import com.example.rewardsparta.reWard.PostMissionItem
import com.example.rewardsparta.reWard.nobodyMissionList
import com.example.rewardsparta.signIn.SignInActivity.Companion.token
import com.example.rewardsparta.signUp.SignUpActivity
import kotlinx.android.synthetic.main.activity_nobody.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NobodyActivity : AppCompatActivity() {

    val noBodyAdapter = NobodyAdapter()

    companion object{
        var hunterList = listOf<Hunter>()
        var reportedImage = ""
        var reportedDescript = ""
        var itemId = 0
        var postId = 0
    }

    inner class NobodyAPIFunction {

        fun getHistory() {
            val call: Call<ResponseHistory> = SignUpActivity.ApiClient.getClient.getHistory(token)
            call.enqueue(object : Callback<ResponseHistory> {
                override fun onResponse(
                    call: Call<ResponseHistory>?,
                    response: Response<ResponseHistory>?) {

                    println("get history success")
                    val resp = response?.body()

                    nobodyMissionList.removeAll(nobodyMissionList)
                    for(i in 0 until resp!!.posts.size){
                        nobodyMissionList.add(
                            PostMissionItem(
                                resp.posts[i].id,
                                resp.posts[i].descript,
                                resp.posts[i].name,
                                resp.posts[i].reported_descript,
                                resp.posts[i].hunters,
                                resp.posts[i].budget,
                                resp.posts[i].category,
                                resp.posts[i].done,
                                resp.posts[i].chosen,
                                resp.posts[i].user_id,
                                resp.posts[i].img
                            )
                        )
                    }

                    unAssignList = nobodyMissionList
                    recyclerview.layoutManager = LinearLayoutManager(this@NobodyActivity)
                    recyclerview.adapter = noBodyAdapter




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
        setContentView(R.layout.activity_nobody)

        NobodyAPIFunction().getHistory()

        btn_success.setOnClickListener {
            unAssignList = nobodyMissionList.filter { it.done==1 }
            noBodyAdapter.notifyDataSetChanged()
        }
        btn_fail.setOnClickListener {
            unAssignList = nobodyMissionList.filter { it.done==0 }
            noBodyAdapter.notifyDataSetChanged()
        }
        btn_have_submit.setOnClickListener {           //已回報(進行中
            unAssignList = nobodyMissionList.filter { it.chosen==1 && it.done==null && it.report_descript!==null}
            noBodyAdapter.notifyDataSetChanged()

        }
        btn_havenot_submit.setOnClickListener {        //未回報(進行中
            unAssignList = nobodyMissionList.filter { it.chosen==1 && it.done==null && it.report_descript==null}
            noBodyAdapter.notifyDataSetChanged()
        }
        btn_unAssign.setOnClickListener {
            unAssignList = nobodyMissionList.filter { it.chosen==0 }
            noBodyAdapter.notifyDataSetChanged()
        }






        NobodyAdapter.setToClick(object :NobodyAdapter.mItemClickListener {
            override fun toClick(items: PostMissionItem) {
                if(items.chosen==0){   //若此任務為未指派
                    hunterList = items.hunters
                    itemId = items.id
                    startActivity(Intent(this@NobodyActivity,
                        ChooseHunterActivity::class.java))
                }
                else if(items.chosen==1 && items.done==null && items.report_descript!==null){      //若此任務進行中且已回報
                    reportedImage = items.img!!
                    reportedDescript = items.report_descript
                    postId = items.id
                    startActivity(Intent(this@NobodyActivity,CheckMissionActivity::class.java))
                }
                else Toast.makeText(this@NobodyActivity,"此任務已指派或已結案",Toast.LENGTH_SHORT).show()

            }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this,HomeActivity::class.java))
    }
}
