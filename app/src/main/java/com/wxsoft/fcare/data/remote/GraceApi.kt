package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.Grace
import com.wxsoft.fcare.data.entity.Response
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GraceApi {

    @GET("GraceEvaluation/GetById/{patientId}")
    fun getGrace(@Path("patientId")patientId:String): Flowable<Response<Grace>>

    @POST("GraceEvaluation/Save")
    fun save(@Body grace: Grace): Flowable<Response<String>>

}