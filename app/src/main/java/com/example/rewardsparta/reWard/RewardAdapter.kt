package com.example.rewardsparta.reWard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rewardsparta.R
import com.example.rewardsparta.home.HomeActivity.Companion.myName
import kotlinx.android.synthetic.main.example_reward.view.*

class RewardAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        var unAssignList = listOf<MissionItem>()

        private var listener:mItemClickListener? = null         //本地listener
        fun setToClick(listener:mItemClickListener){         //fragment會呼叫此方法並在那邊override toClick這個方法 好讓下面的setOnClickListener可以用
            this.listener = listener                        //把參數listener指定給本地listener
        }

    }

    interface mItemClickListener{
        fun toClick(items:MissionItem)
    }





    inner class KillViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val missionName = itemView.tv_mission_name
        val missionDescript = itemView.tv_have_done
        val bonus = itemView.tv_have_success
        val hunters = itemView.tv_hunters
        val toplayout = itemView.toplayout_example
        val imgv = itemView.imgv_mission_icon
        val layoutSoldout = itemView.layout_soldout
        fun bind(item: MissionItem){
            imgv.setImageResource(R.drawable.knife)
            missionName.text = item.name
            missionDescript.text = item.descript
            bonus.text = "賞金:${item.budget.toString()}元"

            var list  = mutableListOf<String>()
            for(i in 0 until item.hunters!!.size) list.add(item.hunters[i].name)
            hunters.text = "申請者:${list}"
            if (list.filter { it == myName }.size>=1) {    //若自己已經申請
                layoutSoldout.visibility = View.VISIBLE               //標上紅字
            }
            else  layoutSoldout.visibility = View.INVISIBLE

        }
    }

    inner class CollectViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val missionName = itemView.tv_mission_name
        val missionDescript = itemView.tv_have_done
        val bonus = itemView.tv_have_success
        val hunters = itemView.tv_hunters
        val toplayout = itemView.toplayout_example
        val imgv = itemView.imgv_mission_icon
        val layoutSoldout = itemView.layout_soldout
        fun bind(item: MissionItem){
            imgv.setImageResource(R.drawable.icon_collect)
            missionName.text = item.name
            missionDescript.text = item.descript
            bonus.text = "賞金:${item.budget.toString()}元"

            var list  = mutableListOf<String>()
            for(i in 0 until item.hunters!!.size) list.add(item.hunters[i].name)
            hunters.text = "申請者:${list}"
            if (list.filter { it == myName }.size>=1) {
                layoutSoldout.visibility = View.VISIBLE               //標上SOLD OUT紅字
            }
            else  layoutSoldout.visibility = View.INVISIBLE
        }

    }

    //判斷data其中一項屬性來判斷該使用哪種viewType
    override fun getItemViewType(position: Int): Int {
        return if(unAssignList[position].category==1) 1 else 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return  when (viewType){
            1->{
                val inflater = LayoutInflater.from(parent.context)
                val example = inflater.inflate(R.layout.example_reward, parent, false)
                KillViewHolder(example)
            }
            else->{
                val inflater = LayoutInflater.from(parent.context)
                val example = inflater.inflate(R.layout.example_reward, parent, false)
                CollectViewHolder(example)
            }
        }

    }

    override fun getItemCount() = unAssignList.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is KillViewHolder) {
            holder.bind(unAssignList[position])
            holder.toplayout.setOnClickListener {
                listener?.toClick(unAssignList[position])
            }
        }

        if(holder is CollectViewHolder) {
            holder.bind(unAssignList[position])
            holder.toplayout.setOnClickListener{
                listener?.toClick(unAssignList[position])
            }
        }



    }
}