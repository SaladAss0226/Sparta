package com.example.rewardsparta.soldierManager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rewardsparta.R
import com.example.rewardsparta.reWard.PostMissionItem
import kotlinx.android.synthetic.main.example_reward.view.*

class PostAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        var unAssignList = listOf<PostMissionItem>()

        private var listener: PostAdapter.mItemClickListener? = null         //本地listener
        fun setToClick(listener: PostAdapter.mItemClickListener){         //fragment會呼叫此方法並在那邊override toClick這個方法 好讓下面的setOnClickListener可以用
            this.listener = listener                                        //把參數listener指定給本地listener
        }
    }
    interface mItemClickListener{
        fun toClick(items:PostMissionItem)
    }

    inner class KillViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val missionName = itemView.tv_mission_name
        val missionDescript = itemView.tv_have_done
        val bonus = itemView.tv_have_success
        val toplayout = itemView.toplayout_example
        val imgv = itemView.imgv_mission_icon
        val hunters = itemView.tv_hunters
        val tv_processing = itemView.tv_processing
        fun bind(item: PostMissionItem){
            imgv.setImageResource(R.drawable.knife)
            missionName.text = item.name
            missionDescript.text = item.descript
            bonus.text = "賞金:${item.budget.toString()}元"

            var list  = mutableListOf<String>()
            if(item.chosen==0) {
                for (i in 0 until item.hunters!!.size) list.add(item.hunters[i].name)
                hunters.text = "申請者:${list}"
            }
            else if(item.done==null && item.chosen==1) {
                tv_processing.visibility = View.VISIBLE
                hunters.visibility = View.INVISIBLE
            }
            if(item.done==1) {
                tv_processing.visibility = View.INVISIBLE
                hunters.visibility = View.VISIBLE
                hunters.text = "任務成功"
            }
            if(item.done==0) {
                tv_processing.visibility = View.INVISIBLE
                hunters.visibility = View.VISIBLE
                hunters.text = "任務失敗"
            }
        }
    }

    inner class CollectViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val missionName = itemView.tv_mission_name
        val missionDescript = itemView.tv_have_done
        val bonus = itemView.tv_have_success
        val toplayout = itemView.toplayout_example
        val imgv = itemView.imgv_mission_icon
        val hunters = itemView.tv_hunters
        val tv_processing = itemView.tv_processing
        fun bind(item: PostMissionItem){
            imgv.setImageResource(R.drawable.icon_collect)
            missionName.text = item.name
            missionDescript.text = item.descript
            bonus.text = "賞金:${item.budget.toString()}元"

            var list  = mutableListOf<String>()
            if(item.chosen==0) {
                for (i in 0 until item.hunters!!.size) list.add(item.hunters[i].name)
                hunters.text = "申請者:${list}"
            }
            else if(item.done==null && item.chosen==1) {
                tv_processing.visibility = View.VISIBLE
                hunters.visibility = View.INVISIBLE
            }
            if(item.done==1) {
                tv_processing.visibility = View.INVISIBLE
                hunters.visibility = View.VISIBLE
                hunters.text = "任務成功"
            }
            if(item.done==0) {
                tv_processing.visibility = View.INVISIBLE
                hunters.visibility = View.VISIBLE
                hunters.text = "任務失敗"
            }
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