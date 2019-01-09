package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Measure
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PharmacyApi {

    @GET("Drug/GetAllDrugs")
    fun getAllDrugs(): Maybe<Response<Measure>>

    @GET("Drug/GetAllDrugPackages")
    fun getAllDrugPackages(): Maybe<Response<Measure>>

}