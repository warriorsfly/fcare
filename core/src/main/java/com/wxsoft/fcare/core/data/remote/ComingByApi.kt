package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.*
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ComingByApi {


    @GET("ComingWay/GetById/{patientId}/{currUserId}")
    fun getOne(@Path("patientId")patientId:String,@Path("currUserId")currUserId:String):Maybe<Response<ComingBy>>

    @POST("ComingWay/Save")
    fun save(@Body comingBy: ComingBy):Maybe<Response<String?>>

    @GET("ComingWay/GetBypass/{patientId}")
    fun getPassing(@Path("patientId")patientId:String):Maybe<Response<Passing>>

    @POST("ComingWay/SaveByPass")
    fun savePassing(@Body passing: Passing):Maybe<Response<String?>>
}