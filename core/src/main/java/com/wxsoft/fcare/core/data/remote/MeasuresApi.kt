package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Measure
import com.wxsoft.fcare.core.data.entity.MedicalHistory
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface MeasuresApi {
    @GET("Measure/GetById/{patientId}")
    fun loadMeasure(@Path("patientId")patientId:String): Maybe<Response<Measure>>

    @POST("Measure/Save")
    fun save(@Body measure: Measure): Maybe<Response<String>>
}