package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.DiagnoseRecord
import com.wxsoft.fcare.core.data.entity.Diagnosis
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DiagnoseApi {

    @GET("Diagnosis/GetDiagnoseById/{id}")
    fun getDiagnosis(@Path("id")id:String): Maybe<Response<Diagnosis>>

    @GET("Diagnosis/GetDiagnosisByPatientId/{patientId}")
    fun getDiagnosisList(@Path("patientId")id:String): Maybe<Response<List<DiagnoseRecord>>>

    @POST("Diagnosis/SaveDiagnosis")
    fun save(@Body diagnosis : Diagnosis): Maybe<Response<Diagnosis>>

}