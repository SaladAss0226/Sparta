package com.example.rewardsparta

data class RequestSignUp(
    val name:String,
    val account:String,
    val password:String,
    val role:Int,
    val bank_account:String
)

data class ResponseSignUp(
    val result:String
)

data class RequestSignIn(
    val account:String,
    val password:String
)

data class ResponseSignIn(
    val user:ResponseInfo
)

data class ResponseInfo(
    val name:String,
    val role:Int,
    val money:Int,
    val remember_token:String
)

data class ResponseProfile(
    val name:String,
    val money:Int,
    val cost:Int,
    val role:Int,
    val id:Int,
    val achieveRate:Double,
    val experience:Int
)

//懸賞清單
//id是案子的id
//user_id是案主id
//user_reward_id是獵人id
data class ResponseReward(
    val reward:List<ResponseRewardItem>
)

data class ResponseRewardItem(
    val id: Int,
    val descript:String,
    val name:String,
    val report_descript:String,
    val hunters:List<ResponseHuntersItem>,
    val budget:Int,
    val category: Int,
    val done: Int,
    val chosen:Int,
    val user_id: Int
)
data class ResponseHuntersItem(
    val name:String,
    val user_rewards_id:Int,
    val fee:Int
)

//hunter接案
data class RequestTakeCase(
    val fee:Int
)

data class ResponseTakeCase(
    val result:String
)

data class RequestAddMission(
    val name:String,
    val category: Int,
    val descript: String,
    val budget: Int
)

data class ResponseAddMission(
    val reward:ResponseAMItem
)

data class ResponseAMItem(
    val id: Int,
    val descript:String,
    val name:String,
    val reported_descript:String,
    val hunters:List<ResponseHuntersItem>,
    val budget:Int,
    val category: Int,
    val done: Int,
    val chosen:Int,
    val user_id:Int
)

data class ResponseHistory(
    val history: List<History>,
    val posts: List<Post>
)

data class History(
    val category: Int,
    val chosen: Int,
    val descript: String,
    val done: Int?,
    val fee: Int,
    val name: String,
    val reported_descript: String?,
    val reward_id: Int
)

data class Post(
    val budget: Int,
    val category: Int,
    val chosen: Int,
    val descript: String,
    val done: Int?,
    val hunters: List<Hunter>,
    val id: Int,
    val img: String?,
    val name: String,
    val reported_descript: String?,
    val user_id: Int
)

data class Hunter(
    val achieveRate: Int,
    val experience: Int,
    val fee: Int,
    val name: String,
    val user_rewards_id: Int
)

data class RequestChooseHunter(
    val user_reward_id :Int
)
data class ResponseChooseHunter(
    val result:String
)

data class RequestSubmit(
    val reported_descript:String
    //val img:Any
)
data class ResponseSubmit(
    val result:String
)

data class RequestEndMission(
    val done:Int,
    val key:String
)

data class ResponseEndMission(
    val result:String
)


