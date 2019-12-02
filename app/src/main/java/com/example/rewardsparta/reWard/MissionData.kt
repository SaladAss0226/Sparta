package com.example.rewardsparta.reWard

import com.example.rewardsparta.Hunter
import com.example.rewardsparta.ResponseHuntersItem

data class MissionItem(
    val id:Int,
    val descript:String,
    val name:String,
    val report_descript:String?,
    val hunters:List<ResponseHuntersItem>?,
    val budget:Int,
    val category: Int,
    val done: Int?,
    val chosen:Int,
    val user_id: Int
)
//懸賞榜list
val missionList = mutableListOf<MissionItem>()



data class PostMissionItem(
    val id:Int,
    val descript:String,
    val name:String,
    val report_descript:String?,
    val hunters:List<Hunter>,
    val budget:Int,
    val category: Int,
    val done: Int?,
    val chosen:Int,
    val user_id: Int,
    val img:String?
)

data class TakeCaseMissionItem(
    val category: Int,
    val chosen: Int,
    val descript: String,
    val done: Int?,
    val fee: Int,
    val name: String,
    val reported_descript: String?,
    val reward_id: Int
)

//村民list
val nobodyMissionList = mutableListOf<PostMissionItem>()
//傭兵list
val soldierPostMissionList = mutableListOf<PostMissionItem>()

val soldierTakeCaseList =mutableListOf<TakeCaseMissionItem>()

