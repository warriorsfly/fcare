package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.DisChargeDiagnosis
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.chest.OutCome
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DischargeApi {

    @GET("Outcome/GetById/{patientId}")
    fun getOt(@Path("patientId")patientId:String): Maybe<Response<OutCome>>

    @POST("Outcome/Save")
    fun saveOt(@Body outCome : OutCome): Maybe<Response<String>>

    /**
     * 出院诊断
     */
    @GET("OutHospitalDiagnosis/GetById/{patientId}")
    fun getOtDiagnosis(@Path("patientId")patientId:String): Maybe<Response<DisChargeDiagnosis>>

    @POST("OutHospitalDiagnosis/Save")
    fun saveOtDiagnosis(@Body diagnosis : DisChargeDiagnosis): Maybe<Response<String>>

}