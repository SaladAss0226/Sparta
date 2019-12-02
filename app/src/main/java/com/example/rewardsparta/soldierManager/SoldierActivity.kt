package com.example.rewardsparta.soldierManager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rewardsparta.R
import com.example.rewardsparta.home.HomeActivity
import kotlinx.android.synthetic.main.activity_soldier.*

class SoldierActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soldier)

        btn_goto_postactivity.setOnClickListener {
            startActivity(Intent(this,PostActivity::class.java))
        }

        btn_goto_takecase_activity.setOnClickListener {
            startActivity(Intent(this,TakeCaseActivity::class.java))
        }


    }

    override fun onBackPressed() {
        startActivity(Intent(this,HomeActivity::class.java))
    }
}
