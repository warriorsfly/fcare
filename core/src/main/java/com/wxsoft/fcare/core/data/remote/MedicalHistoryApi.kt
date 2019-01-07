package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.MedicalHistory
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MedicalHistoryApi {

    @GET("MedicalHistory/GetById/{patientId}")
    fun loadMedicalHistory(@Path("patientId")patientId:String): Maybe<Response<MedicalHistory>>

    @POST("MedicalHistory/Save")
    fun save(@Body medicalHistory: MedicalHistory): Maybe<Response<String>>

}