package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Thrombolysis
import com.wxsoft.fcare.core.result.Resource
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path

interface ThrombolysisApi {
    @GET("Throm/GetById/{patientId}/{isPreHospital}")
    fun loadThrombolysis(@Path("patientId")patientId:String, @Path("isPreHospital")isPre:Boolean): Maybe<Response<Thrombolysis>>


}