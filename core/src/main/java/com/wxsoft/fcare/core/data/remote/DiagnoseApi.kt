package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Diagnosis
import com.wxsoft.fcare.core.data.entity.Pharmacy
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DiagnoseApi {

    @GET("Diagnosis/GetDiagnosisByPatientId/{patientId}/{location}")
    fun getDiagnosis(@Path("patientId")patientId:String,
                     @Path("location")location:Int): Maybe<Response<Diagnosis>>

    @POST("Diagnosis/SaveDiagnosis")
    fun save(@Body diagnosis : Diagnosis): Maybe<Response<Diagnosis>>

}