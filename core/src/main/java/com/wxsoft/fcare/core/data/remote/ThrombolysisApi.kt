package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Complication
import com.wxsoft.fcare.core.data.entity.InformedConsent
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Thrombolysis
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ThrombolysisApi {
    @GET("Throm/GetThromById/{id}")
    fun loadThrombolysis(@Path("id")patientId:String): Maybe<Response<Thrombolysis>>

    @GET("Throm/GetByPatientId/{patientId}")
    fun loadThrom(@Path("patientId")patientId:String): Maybe<Response<List<Thrombolysis>>>

    @GET("InformedConsent/GetInformedConsentById/{id}")
    fun getInformedConsentById(@Path("id")id:String): Maybe<Response<InformedConsent>>

    @POST("Throm/Save")
    fun save(@Body thrombolysis: Thrombolysis):Maybe<Response<String>>

    @POST("Complication/Save")
    fun saveComplication(@Body complication: List<Complication>):Maybe<Response<String>>

    @GET("Complication/GetByPatientId/{patientId}/{sceneType}")
    fun getComplication(@Path("patientId")id:String,@Path("sceneType")sceneType:String): Maybe<Response<List<Complication>>>

}