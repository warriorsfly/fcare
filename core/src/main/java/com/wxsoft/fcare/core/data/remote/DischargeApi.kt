package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Diagnosis
import com.wxsoft.fcare.core.data.entity.DisChargeDiagnosis
import com.wxsoft.fcare.core.data.entity.Pharmacy
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DischargeApi {

    @GET("Outcome/GetById/{patientId}")
    fun getOt(@Path("patientId")patientId:String): Maybe<Response<Diagnosis>>

    @POST("Outcome/Save")
    fun saveOt(@Body diagnosis : Diagnosis): Maybe<Response<Diagnosis>>

    @GET("OutHospitalDiagnosis/GetById/{patientId}")
    fun getOtDiagnosis(@Path("patientId")patientId:String): Maybe<Response<DisChargeDiagnosis>>

    @POST("OutHospitalDiagnosis/Save")
    fun saveOtDiagnosis(@Body diagnosis : DisChargeDiagnosis): Maybe<Response<String>>

}