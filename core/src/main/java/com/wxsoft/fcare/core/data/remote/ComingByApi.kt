package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Account
import com.wxsoft.fcare.core.data.entity.ComingBy
import com.wxsoft.fcare.core.data.entity.LoginInfo
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ComingByApi {


    @GET("ComingWay/GetById/{patientId}")
    fun getOne(@Path("patientId")patientId:String):Maybe<Response<ComingBy>>

    @POST("ComingWay/Save")
    fun save(@Body comingBy: ComingBy):Maybe<Response<String?>>
}