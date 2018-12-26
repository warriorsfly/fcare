package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.Response
import com.wxsoft.fcare.data.entity.Thrombolysis
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ThromApi {

    @GET("Throm/GetById/{patientId}/{isPreHospital}")
    fun getThrombolysis(@Path("patientId")patientId:String,
                        @Path("isPreHospital")isPreHospital:Boolean): Flowable<Response<Thrombolysis>>

    @POST("Throm/Save")
    fun save(@Body throm: Thrombolysis): Flowable<Response<String>>

}