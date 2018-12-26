package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.Department
import com.wxsoft.fcare.data.entity.Response
import io.reactivex.Flowable
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotifyApi {

    @GET("Notice/GetNeedNoticeDepartments")
    fun getNotifyWho():Maybe<Response<List<Department>>>

    @POST("Notice/SendNotices/{patientId}/{userId}")
    fun notify(@Path("userId")userId:String,@Path("patientId")id:String, @Body departments:List<String>):Maybe<Response<String>>

    @GET("Notice/AnswerNotice/{patientId}/{userId}")
    fun answer(@Path("userId")userId:String,@Path("patientId")id:String):Maybe<Response<String?>>
}