package com.example.rewardsparta.missionManager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rewardsparta.Hunter
import com.example.rewardsparta.R
import kotlinx.android.synthetic.main.example_choose_hunter.view.*
import kotlinx.android.synthetic.main.example_reward.view.tv_have_done


class ChooseHunterAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        var unAssignList = listOf<Hunter>()

        private var listener: mItemClickListener? = null         //本地listener
        fun setToClick(listener: mItemClickListener){         //fragment會呼叫此方法並在那邊override toClick這個方法 好讓下面的setOnClickListener可以用
            Companion.listener = listener                                        //把參數listener指定給本地listener
        }
    }
    interface mItemClickListener{
        fun toClick(items:Hunter)
    }

    inner class HuntersViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val name = itemView.tv_name
        val fee = itemView.tv_fee
        val experience = itemView.tv_have_done
        val achieveRate = itemView.tv_have_success
        val toplayout = itemView.toplayout_example
        val imgv = itemView.imgv_mission_icon
        fun bind(item: Hunter){
            imgv.setImageResource(R.drawable.ic_welcome_img)
            name.text = "暱稱:${item.name}"
            fee.text = "出價:${item.fee}元"
            experience.text = "已接手件數:${item.experience}"
            achieveRate.text = "成功完成件數:${item.achieveRate}"
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val example = inflater.inflate(R.layout.example_choose_hunter, parent, false)
                return  HuntersViewHolder(example)
    }

    override fun getItemCount() = unAssignList.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HuntersViewHolder) {
            holder.bind(unAssignList[position])
            holder.toplayout.setOnClickListener {
                listener?.toClick(unAssignList[position])
            }
        }
    }
}