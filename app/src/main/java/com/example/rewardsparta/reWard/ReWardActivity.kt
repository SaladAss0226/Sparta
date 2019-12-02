package com.example.rewardsparta.reWard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rewardsparta.*
import com.example.rewardsparta.home.HomeActivity
import com.example.rewardsparta.home.HomeActivity.Companion.ROLE
import com.example.rewardsparta.home.HomeActivity.Companion.userId
import com.example.rewardsparta.reWard.RewardAdapter.Companion.unAssignList
import com.example.rewardsparta.signIn.SignInActivity
import com.example.rewardsparta.signIn.SignInActivity.Companion.token
import com.example.rewardsparta.signUp.SignUpActivity
import kotlinx.android.synthetic.main.activity_reward.*
import kotlinx.android.synthetic.main.dialog_take_case.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReWardActivity : AppCompatActivity() {

    companion object{

        val rewardAdapter = RewardAdapter()
    }

    var itemId = 0    //懸賞榜項目ID

    //取得懸賞榜資料的API
    inner class RewardAPIFunction {
        fun getReward() {
            val call: Call<ResponseReward> = SignUpActivity.ApiClient.getClient.getRewardList(SignInActivity.token)
            call.enqueue(object : Callback<ResponseReward> {
                override fun onResponse(
                    call: Call<ResponseReward>?,
                    response: Response<ResponseReward>?) {

                    if(response?.code()==200){
                        println("get rewardList success")
                        val response = response?.body()

                        missionList.removeAll(missionList)
                        for(i in 0 until response.reward.size) {
                            missionList.add(
                                MissionItem(
                                    response!!.reward[i].id,
                                    response!!.reward[i].descript,
                                    response!!.reward[i].name,
                                    response!!.reward[i].report_descript,
                                    response!!.reward[i].hunters,
                                    response!!.reward[i].budget,
                                    response!!.reward[i].category,
                                    response!!.reward[i].done,
                                    response!!.reward[i].chosen,
                                    response!!.reward[i].user_id
                                )
                            )

                        }
                        unAssignList = missionList
                        recyclerview.layoutManager = LinearLayoutManager(this@ReWardActivity)
                        recyclerview.adapter = rewardAdapter
                    }


                }
                override fun onFailure(call: Call<ResponseReward>?, t: Throwable?) {
                    println("test $t")     //t會報錯
                    Log.e("error","test")

                }
            })
        }

    }

    //傭兵確認接案時發送的API
    inner class TakeCaseAPIFunction {
        fun sendInfo(token:String,fee:Int,itemId:Int) {
            val call: Call<ResponseTakeCase> = SignUpActivity.ApiClient.getClient.sendInfo(token, RequestTakeCase(fee),itemId)
            call.enqueue(object : Callback<ResponseTakeCase> {
                override fun onResponse(
                    call: Call<ResponseTakeCase>?,
                    response: Response<ResponseTakeCase>?) {

                    if(response!!.isSuccessful){
                        println("send Take case success")
                        val response = response?.body()
                        Toast.makeText(this@ReWardActivity,"申請成功",Toast.LENGTH_SHORT).show()
                        RewardAPIFunction().getReward()          //取得懸賞榜資料
                    }


                }
                override fun onFailure(call: Call<ResponseTakeCase>?, t: Throwable?) {
                    println("test $t")     //t會報錯
                    Log.e("error","test")

                }
            })
        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)

        RewardAPIFunction().getReward()          //取得懸賞榜資料


        var searchByName  = listOf<MissionItem>()
        var searchByBonus = listOf<MissionItem>()

        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(sequence: CharSequence, i: Int, i1: Int, i2: Int) {

                if (et_search.length()<1) {
                    if(et_search_by_bonus.text.length<1) unAssignList = missionList
                    else unAssignList = searchByBonus

                }
                else if(et_search_by_bonus.text.length<1){
                    searchByName =
                        missionList.filter { it.name.contains(et_search.text.toString()) }  //挑出namee裡面有包含搜尋字詞的item並放入list
                    unAssignList = searchByName
                }
                else if(et_search_by_bonus.text.length>=1){
                    searchByName = searchByBonus.filter {  it.name.contains(et_search.text.toString()) }
                    unAssignList = searchByName
                }
                rewardAdapter.notifyDataSetChanged()
                if(unAssignList.size<1) layout_no_result.visibility = View.VISIBLE      //顯示查無結果
                else layout_no_result.visibility = View.INVISIBLE

            }
            override fun afterTextChanged(editable: Editable) {}
        })

        et_search_by_bonus.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(sequence: CharSequence, i: Int, i1: Int, i2: Int) {

                if (et_search_by_bonus.length()<1) {
                    if(et_search.text.length<1) unAssignList = missionList
                    else unAssignList = searchByName
                }
                else if(et_search.text.length<1){
                     searchByBonus = missionList.filter { it.budget >= et_search_by_bonus.text.toString().toInt() }  //挑出namee裡面有包含搜尋字詞的item並放入list
                    unAssignList = searchByBonus
                }
                else if(et_search.text.length>=1){
                    searchByBonus = searchByName.filter { it.budget >= et_search_by_bonus.text.toString().toInt() }
                    unAssignList = searchByBonus
                }
                rewardAdapter.notifyDataSetChanged()
                if(unAssignList.size<1) layout_no_result.visibility = View.VISIBLE      //顯示查無結果
                else layout_no_result.visibility = View.INVISIBLE

            }
            override fun afterTextChanged(editable: Editable) {}
        })

        //傭兵接單點擊事件
        RewardAdapter.setToClick(object :RewardAdapter.mItemClickListener {
            override fun toClick(items: MissionItem) {
                if(ROLE==0 || userId==items.user_id) Toast.makeText(this@ReWardActivity,"抱歉，您無法接案",Toast.LENGTH_SHORT).show()
                else if(ROLE==1){

                    val dialog = DialogTakeCase(this@ReWardActivity, items)     //再把指定購買的項目items傳過去

                    dialog.show()
                    dialog.btn_accept.setOnClickListener {
                        //********實作按下接案按鈕點擊事件
                        itemId = items.id
                        if(dialog.dialog_et_price.text.length>=1){
                            TakeCaseAPIFunction().sendInfo(token,dialog.dialog_et_price.text.toString().toInt(), itemId)       //發送API
                            rewardAdapter.notifyDataSetChanged()

                        }

                        else Toast.makeText(this@ReWardActivity,"請輸入價錢",Toast.LENGTH_SHORT).show()

                        dialog.dismiss()

                    }
                }

            }
        })


        btn_add_mission.setOnClickListener {
            startActivity(Intent(this,AddMissionActivity::class.java))
        }



    }

    override fun onBackPressed() {
        startActivity(Intent(this,HomeActivity::class.java))
    }
}
