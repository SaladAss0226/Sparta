package com.example.rewardsparta.soldierManager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rewardsparta.R
import com.example.rewardsparta.reWard.PostMissionItem
import com.example.rewardsparta.reWard.TakeCaseMissionItem
import kotlinx.android.synthetic.main.example_reward.view.*
import kotlin.properties.Delegates

class TakeCaseAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var unAssignList by Delegates.observable(listOf<TakeCaseMissionItem>()) {
        _,_,_ -> notifyDataSetChanged()
    }

    companion object{

        private var listener: TakeCaseAdapter.mItemClickListener? = null         //本地listener
        fun setToClick(listener: TakeCaseAdapter.mItemClickListener){         //fragment會呼叫此方法並在那邊override toClick這個方法 好讓下面的setOnClickListener可以用
            this.listener = listener                                        //把參數listener指定給本地listener
        }
    }
    interface mItemClickListener{
        fun toClick(items:TakeCaseMissionItem)
    }

    inner class KillViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val missionName = itemView.tv_mission_name
        val missionDescript = itemView.tv_have_done
        val fee = itemView.tv_have_success
        val toplayout = itemView.toplayout_example
        val imgv = itemView.imgv_mission_icon
        val hunters = itemView.tv_hunters
        val tv_processing = itemView.tv_processing
        val tv_have_reported = itemView.tv_have_reported
        fun bind(item: TakeCaseMissionItem){
            imgv.setImageResource(R.drawable.knife)
            missionName.text = item.name
            missionDescript.text = item.descript
            fee.text = "出價:${item.fee}元"

            if(item.chosen==0) hunters.text = "已申請 待審中"
            else if(item.done==null && item.chosen==1) {
                tv_processing.visibility = View.VISIBLE
                hunters.visibility = View.INVISIBLE
            }
            if(item.done==null && item.reported_descript!==null){
                tv_have_reported.visibility = View.VISIBLE
            }
            if(item.done==1) {
                tv_processing.visibility = View.INVISIBLE
                hunters.visibility = View.VISIBLE
                tv_have_reported.visibility = View.INVISIBLE
                hunters.text = "任務成功"
            }
            if(item.done==0) {
                tv_processing.visibility = View.INVISIBLE
                hunters.visibility = View.VISIBLE
                tv_have_reported.visibility = View.INVISIBLE
                hunters.text = "任務失敗"
            }
        }
    }

    inner class CollectViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val missionName = itemView.tv_mission_name
        val missionDescript = itemView.tv_have_done
        val fee = itemView.tv_have_success
        val toplayout = itemView.toplayout_example
        val imgv = itemView.imgv_mission_icon
        val hunters = itemView.tv_hunters
        val tv_processing = itemView.tv_processing
        val tv_have_reported = itemView.tv_have_reported
        fun bind(item: TakeCaseMissionItem){
            imgv.setImageResource(R.drawable.icon_collect)
            missionName.text = item.name
            missionDescript.text = item.descript
            fee.text = "出價:${item.fee}元"

            var list  = mutableListOf<String>()
            if(item.chosen==0) hunters.text = "已申請 待審中"
            else if(item.done==null && item.chosen==1) {
                tv_processing.visibility = View.VISIBLE
                hunters.visibility = View.INVISIBLE
            }
            if(item.done==null && item.reported_descript!==null){
                tv_have_reported.visibility = View.VISIBLE
            }
            if(item.done==1) {
                tv_processing.visibility = View.INVISIBLE
                hunters.visibility = View.VISIBLE
                tv_have_reported.visibility = View.INVISIBLE
                hunters.text = "任務成功"
            }
            if(item.done==0) {
                tv_processing.visibility = View.INVISIBLE
                hunters.visibility = View.VISIBLE
                tv_have_reported.visibility = View.INVISIBLE
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