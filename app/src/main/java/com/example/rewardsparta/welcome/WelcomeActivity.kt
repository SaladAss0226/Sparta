package com.example.rewardsparta.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rewardsparta.R
import com.example.rewardsparta.signIn.SignInActivity
import com.example.rewardsparta.signUp.SignUpActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)


        btn_create_account.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        btn_Sign_in.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }



    }

    override fun onBackPressed() {
        finish()
    }
}
