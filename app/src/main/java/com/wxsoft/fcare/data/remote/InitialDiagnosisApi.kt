package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.Response
import com.wxsoft.fcare.data.entity.chest.InitialDiagnosis
import io.reactivex.Maybe
import retrofit2.http.*

interface InitialDiagnosisApi {

    @GET("InitialDiagnosis/GetById/{patientId}")
    fun getDiagnosis(@Path("patientId")id:String):Maybe<Response<InitialDiagnosis?>>

    @POST("InitialDiagnosis")
    fun insert(@Body initialDiagnosis: InitialDiagnosis):Maybe<String>

    @PUT("InitialDiagnosis")
    fun update(@Body initialDiagnosis: InitialDiagnosis):Maybe<String>
}