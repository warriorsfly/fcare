package com.wxsoft.fcare.data.remote


import com.wxsoft.fcare.data.entity.OutHospitalDiagnosis
import com.wxsoft.fcare.data.entity.Response
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OutHospitalApi {

    @GET("OutHospitalDiagnosis/GetById/{patientId}")
    fun getOutcome(@Path("patientId")patientId:String): Flowable<Response<OutHospitalDiagnosis>>

    @POST("OutHospitalDiagnosis/Save")
    fun save(@Body outHospital: OutHospitalDiagnosis): Flowable<Response<String>>

}