package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.OutCome
import com.wxsoft.fcare.data.entity.Response
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OutComeApi {

    @GET("Outcome/GetById/{patientId}")
    fun getOutcome(@Path("patientId")patientId:String): Flowable<Response<OutCome>>

    @POST("Outcome/Save")
    fun save(@Body outcome: OutCome): Flowable<Response<String>>


}