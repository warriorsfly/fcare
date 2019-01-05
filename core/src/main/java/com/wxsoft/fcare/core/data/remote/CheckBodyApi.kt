package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.CheckBody
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CheckBodyApi {

    @GET("BodyCheck/GetById/{patientId}")
    fun loadCheckBody(@Path("patientId")patientId:String): Maybe<Response<CheckBody>>

    @POST("BodyCheck/Save")
    fun save(@Body checkBody: CheckBody): Maybe<Response<String>>

}