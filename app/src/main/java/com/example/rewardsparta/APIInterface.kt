package com.example.rewardsparta

import com.example.rewardsparta.reWard.MissionItem
import com.example.rewardsparta.reWard.missionList
import com.example.rewardsparta.signIn.SignInActivity.Companion.token
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    //註冊
    @Headers("Accept: application/json; Content-type: application/json")
    @POST("api/register")
    fun toSignUp (@Body item: RequestSignUp): Call<ResponseSignUp>

    //登入
    @Headers("Accept: application/json; Content-type: application/json")
    @POST("api/login")
    fun toSignIn (@Body item: RequestSignIn): Call<ResponseSignIn>

    //主頁
    @Headers("Accept: application/json; Content-type: application/json")
    @GET("api/profile")
    fun getProfile (@Header("remember_token") token:String): Call<ResponseProfile>

    //懸賞榜清單
    @Headers("Accept: application/json; Content-type: application/json")
    @GET("api/reward")
    fun getRewardList (@Header("remember_token") token:String): Call<ResponseReward>

    //傭兵申請接單時發送API
    //fee由body帶 token由Headers帶 id用網址帶
    @Headers("Accept: application/json; Content-type: application/json")
    @POST("api/reward/{id}")
    fun sendInfo(@Header("remember_token") token:String,
                 @Body item:RequestTakeCase,
                 @Path("id") id:Int): Call<ResponseTakeCase>

    //傭兵或村民發案時發送的API
    @Headers("Accept: application/json; Content-type: application/json")
    @POST("api/reward")
    fun sendAddMission (@Header("remember_token") token:String, @Body item: RequestAddMission): Call<ResponseAddMission>


    //村民/傭兵發案紀錄 傭兵接案紀錄
    @Headers("Accept: application/json; Content-type: application/json")
    @GET("api/history")
    fun getHistory (@Header("remember_token") token:String): Call<ResponseHistory>

    //案主選擇傭兵並傳id給後端
    @Headers("Accept: application/json; Content-type: application/json")
    @POST("api/reward/{id}/choose")
    fun sendChoice (@Header("remember_token") token:String,
                    @Body item: RequestChooseHunter,
                    @Path("id") id:Int ): Call<ResponseChooseHunter>

//    //傭兵交付任務
//    @Headers("Accept: application/json; Content-type: multipart/form-data")
//    @POST("api/reward/{id}/report")
//    fun sendSubmit (@Header("remember_token") token:String,
//                    @Body item: RequestSubmit,
//                    @Path("id") id:Int ): Call<ResponseSubmit>

    //傭兵交付任務
    @Headers("Accept: application/json")
    @Multipart
    @POST("api/reward/{id}/report")
    fun sendSubmit(@Header("remember_token") token:String,
                    @Path("id") id:Int,
                    @Part file: MultipartBody.Part,
                    @Part item: MultipartBody.Part):Call<ResponseSubmit>


    //案主判定案子是否成功 且結束懸賞
    @Headers("Accept: application/json; Content-type: application/json")
    @POST("api/reward/{id}/done")
    fun endMission(@Header("remember_token") token:String,
                   @Body item: RequestEndMission,
                   @Path("id") id:Int): Call<ResponseEndMission>


}