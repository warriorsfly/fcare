package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Department
import com.wxsoft.fcare.core.data.entity.NotiUserItem
import com.wxsoft.fcare.core.data.entity.Notify
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationApi {

    @GET("Notice/GetNeedNoticeDepartments")
    fun getNotifyWho():Maybe<Response<List<Department>>>

    @POST("Notice/SendNotices/{patientId}/{userId}")
    fun notify(@Path("userId")userId:String,@Path("patientId")id:String, @Body departments:List<String>):Maybe<Response<String>>

    @GET("Notice/AnswerNotice/{patientId}/{userId}")
    fun answer(@Path("userId")userId:String,@Path("patientId")id:String):Maybe<Response<String?>>

    @GET("Message/GetWaitNoticeUsers/{patientId}")
    fun getNotifyUsers(@Path("patientId")patientId:String):Maybe<Response<List<NotiUserItem>>>

    @GET("Notice/QuickNotice/{patientId}/{currUserId}")
    fun submitCallTime(@Path("patientId")patientId:String,@Path("currUserId")currUserId:String):Maybe<Response<Int>>

    @POST("Message/SendMessageToUsers")
    fun submitNotify(@Body notyfi:Notify):Maybe<Response<String>>



}