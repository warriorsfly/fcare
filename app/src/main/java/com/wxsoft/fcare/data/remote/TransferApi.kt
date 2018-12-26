package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TransferApi {

    @GET("ComingWay/GetById/{patientId}")
    fun getOne(@Path("patientId")patientId:String):Maybe<Response<Transfer>>

    @POST("ComingWay/Save")
    fun save(@Body transfer: Transfer):Maybe<Response<String?>>
}