package com.example.rewardsparta.signIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.rewardsparta.*
import com.example.rewardsparta.home.HomeActivity
import com.example.rewardsparta.signUp.SignUpActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {

    companion object{
        var token:String = ""
    }

    inner class SignInAPIFunction {
        fun signIn(item: RequestSignIn) {
            val call: Call<ResponseSignIn> = SignUpActivity.ApiClient.getClient.toSignIn(item)
            call.enqueue(object : Callback<ResponseSignIn> {
                override fun onResponse(
                    call: Call<ResponseSignIn>?,
                    response: Response<ResponseSignIn>?) {

                    if(response?.code()==200){
                        println("test success")
                        val response = response?.body()
                        token = response.user.remember_token
                        Toast.makeText(this@SignInActivity,"${response.user.name}，歡迎回來",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignInActivity,HomeActivity::class.java))
                    }
                    else Toast.makeText(this@SignInActivity,"帳號或密碼錯誤",Toast.LENGTH_SHORT).show()


                }
                override fun onFailure(call: Call<ResponseSignIn>?, t: Throwable?) {
                    println("test $t")     //t會報錯
                    Log.e("error","test")

                }
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        btn_sign_in_enter_game.setOnClickListener {

            if(et_password_signin.text.length>0 && et_account_signin.text.length>0){
                SignInAPIFunction().signIn(
                    RequestSignIn(
                        et_account_signin.text.toString(),
                        et_password_signin.text.toString()
                    )
                )
            }
            else Toast.makeText(this,"請輸入帳號及密碼",Toast.LENGTH_SHORT).show()
        }

    }
}
