package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.InformedConsent
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Thrombolysis
import com.wxsoft.fcare.core.result.Resource
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path

interface ThrombolysisApi {
    @GET("Throm/GetThromById/{id}")
    fun loadThrombolysis(@Path("id")patientId:String): Maybe<Response<Thrombolysis>>

    @GET("InformedConsent/GetInformedConsentById/{id}")
    fun getInformedConsentById(@Path("id")id:String): Maybe<Response<InformedConsent>>

}