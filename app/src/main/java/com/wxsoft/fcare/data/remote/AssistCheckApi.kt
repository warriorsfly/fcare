package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.Response
import com.wxsoft.fcare.data.entity.chest.AssistCheck
import io.reactivex.Flowable
import retrofit2.http.*

interface AssistCheckApi {
    @GET("AssistCheck/GetById/{patientId}")
    fun getAssistCheck(@Path("patientId")id:String): Flowable<Response<AssistCheck>>

    @POST("AssistCheck")
    fun insert(@Body check: AssistCheck): Flowable<String>

    @PUT("AssistCheck")
    fun update(@Body check: AssistCheck): Flowable<String>
}