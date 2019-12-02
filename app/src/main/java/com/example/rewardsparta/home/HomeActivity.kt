package com.example.rewardsparta.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.rewardsparta.*
import com.example.rewardsparta.nobodyManager.NobodyActivity
import com.example.rewardsparta.soldierManager.SoldierActivity
import com.example.rewardsparta.reWard.ReWardActivity
import com.example.rewardsparta.signIn.SignInActivity.Companion.token
import com.example.rewardsparta.signUp.SignUpActivity
import com.example.rewardsparta.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    companion object{
        var myName = ""
        var ROLE = 0
        var money = 0
        var experience = 0
        var achieveRate = 0
        var cost = 0
        var userId = 0
    }

    inner class ProfileAPIFunction {
        fun getProfile() {
            val call: Call<ResponseProfile> = SignUpActivity.ApiClient.getClient.getProfile(token)
            call.enqueue(object : Callback<ResponseProfile> {
                override fun onResponse(
                    call: Call<ResponseProfile>?,
                    response: Response<ResponseProfile>?) {

                    if(response?.code()==200){
                        println("get profile success")
                        val response = response?.body()
                        userId = response.id
                        myName = response.name
                        ROLE = response.role

                        tv_name.text = "暱稱:$myName"
                        tv_role.text = "身分:${if(response.role==1) "傭兵" else "村民"}"
                        tv_money.text = "錢包:${response.money}"
                        tv_experience.text = "已完成總件數:${response.experience}"
                        tv_achieve_rate.text = "成功達成率:${response.achieveRate}"
                        tv_total_case_money.text = "已委託懸賞案總金額:${response.cost}"


                    }


                }
                override fun onFailure(call: Call<ResponseProfile>?, t: Throwable?) {
                    println("test $t")     //t會報錯
                    Log.e("error","test")

                }
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        ProfileAPIFunction().getProfile()

        //進入懸賞榜
        btn_reward_activity.setOnClickListener {
            startActivity(Intent(this,ReWardActivity::class.java))
        }
        //進入歷史紀錄
        btn_history.setOnClickListener {
            if(ROLE==1) startActivity(Intent(this, SoldierActivity::class.java))
            else startActivity(Intent(this,NobodyActivity::class.java))
        }
        //登出
        btn_sign_out.setOnClickListener {
            val intent = Intent()
            intent.setClass(this,WelcomeActivity::class.java!!)    //進入welcomeActivity時会清除该进程空间的所有Activity 在welcomeActivity退出时直接使用 finish 方法即可退出app
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
//            startActivity(Intent(this,WelcomeActivity::class.java))
        }




    }

    override fun onBackPressed() {
        startActivity(Intent(this,WelcomeActivity::class.java))
    }
}
