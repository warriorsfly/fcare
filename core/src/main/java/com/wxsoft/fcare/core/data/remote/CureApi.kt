package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Cure
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Thrombolysis
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CureApi {

    @GET("TreatStrategy/GetTreatmentOperation/{patientId}")
    fun loadCure(@Path("patientId")patientId:String): Maybe<Response<Cure>>

    @POST("TreatStrategy/SaveTreatmentOperation")
    fun save(@Body cure: Cure):Maybe<Response<String>>

}