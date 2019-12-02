package com.example.rewardsparta.signUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.rewardsparta.APIInterface
import com.example.rewardsparta.R
import com.example.rewardsparta.RequestSignUp
import com.example.rewardsparta.ResponseSignUp
import com.example.rewardsparta.signIn.SignInActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity() {

    //此為唯一的APIClient 之後接任何api也都是用這個物件
    object ApiClient{
        var baseUrl = "http://35.221.252.120/"
        val getClient: APIInterface
            get() {
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                return retrofit.create(APIInterface::class.java)
            }
    }

    inner class SignUpAPIFunction {
        fun signUp(item: RequestSignUp) {
            val call: Call<ResponseSignUp> = ApiClient.getClient.toSignUp(item)
            call.enqueue(object : Callback<ResponseSignUp> {
                override fun onResponse(
                    call: Call<ResponseSignUp>?,
                    response: Response<ResponseSignUp>?) {

                    if(response?.code()==201){
                        println("test success")
                        Toast.makeText(this@SignUpActivity,response?.body().result,Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                    }
                    if(response?.code()==416) Toast.makeText(this@SignUpActivity,"帳號或名稱已有人使用",Toast.LENGTH_SHORT).show()

                }
                override fun onFailure(call: Call<ResponseSignUp>?, t: Throwable?) {
                    println("test $t")     //t會報錯
                    Log.e("error","test")

                }
            })
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        btn_get_started.setOnClickListener {

            if(et_account.text.length>0 && et_name.text.length>0 && et_password.text.length>0 && radioGroup.checkedRadioButtonId!==null) {
                SignUpAPIFunction().signUp(
                    RequestSignUp(
                        et_name.text.toString(),
                        et_account.text.toString(),
                        et_password.text.toString(),
                        if (radioGroup.checkedRadioButtonId == R.id.rb_soldier) 1 else 0,
                        et_bank_account.text.toString()

                    )
                )
            }
            else Toast.makeText(this,"角色、姓名、帳號或密碼皆不得留白",Toast.LENGTH_SHORT).show()
        }

    }
}
