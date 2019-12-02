package com.example.rewardsparta.soldierManager

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
import com.example.rewardsparta.nobodyManager.NobodyActivity
import com.example.rewardsparta.nobodyManager.NobodyActivity.Companion.hunterList
import com.example.rewardsparta.nobodyManager.NobodyActivity.Companion.itemId
import com.example.rewardsparta.nobodyManager.NobodyActivity.Companion.reportedDescript
import com.example.rewardsparta.nobodyManager.NobodyActivity.Companion.reportedImage
import com.example.rewardsparta.reWard.PostMissionItem
import com.example.rewardsparta.reWard.soldierPostMissionList
import com.example.rewardsparta.signIn.SignInActivity.Companion.token
import com.example.rewardsparta.signUp.SignUpActivity
import com.example.rewardsparta.soldierManager.PostAdapter.Companion.unAssignList
import kotlinx.android.synthetic.main.activity_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostActivity : AppCompatActivity() {

    val postAdapter = PostAdapter()

    companion object{
        var itemId = 0
    }

    inner class SoldierAPIFunction {

        fun getHistory() {
            val call: Call<ResponseHistory> = SignUpActivity.ApiClient.getClient.getHistory(token)
            call.enqueue(object : Callback<ResponseHistory> {
                override fun onResponse(
                    call: Call<ResponseHistory>?,
                    response: Response<ResponseHistory>?) {

                    println("get history success")
                    val response = response?.body()

                    soldierPostMissionList.removeAll(soldierPostMissionList)
                    for(i in 0 until response!!.posts.size){
                        soldierPostMissionList.add(
                            PostMissionItem(
                                response.posts[i].id,
                                response.posts[i].descript,
                                response.posts[i].name,
                                response.posts[i].reported_descript,
                                response.posts[i].hunters,
                                response.posts[i].budget,
                                response.posts[i].category,
                                response.posts[i].done,
                                response.posts[i].chosen,
                                response.posts[i].user_id,
                                response.posts[i].img
                            )
                        )
                    }

                    unAssignList = soldierPostMissionList
                    recyclerview.layoutManager = LinearLayoutManager(this@PostActivity)
                    recyclerview.adapter = postAdapter

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
        setContentView(R.layout.activity_post)

        SoldierAPIFunction().getHistory()

        btn_success.setOnClickListener {
            unAssignList = soldierPostMissionList.filter { it.done==1 }
            postAdapter.notifyDataSetChanged()
        }
        btn_fail.setOnClickListener {
            unAssignList = soldierPostMissionList.filter { it.done==0 }
            postAdapter.notifyDataSetChanged()
        }
        btn_have_submit.setOnClickListener {
            unAssignList = soldierPostMissionList.filter { it.chosen==1 && it.done==null && it.report_descript!==null}
            postAdapter.notifyDataSetChanged()
        }
        btn_havenot_submit.setOnClickListener {
            unAssignList = soldierPostMissionList.filter { it.chosen==1 && it.done==null && it.report_descript==null}
            postAdapter.notifyDataSetChanged()
        }
        btn_unAssign.setOnClickListener {
            unAssignList = soldierPostMissionList.filter { it.chosen==0 }
            postAdapter.notifyDataSetChanged()
        }


        PostAdapter.setToClick(object :PostAdapter.mItemClickListener {
            override fun toClick(items: PostMissionItem) {
                if(items.chosen==0){   //若此任務為未指派
                    hunterList = items.hunters
                    itemId = items.id
                    startActivity(Intent(this@PostActivity, ChooseHunterActivity::class.java))
                }
                else if(items.chosen==1 && items.done==null && items.report_descript!==null){      //若此任務進行中且已回報
                    reportedImage = items.img!!
                    reportedDescript = items.report_descript
                    NobodyActivity.postId = items.id
                    startActivity(Intent(this@PostActivity, CheckMissionActivity::class.java))
                }
                else Toast.makeText(this@PostActivity,"此任務已指派或已結案",Toast.LENGTH_SHORT).show()

            }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this,SoldierActivity::class.java))
    }
}
