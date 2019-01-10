package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Pharmacy
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.drug.Drug
import com.wxsoft.fcare.core.data.entity.drug.DrugPackage
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PharmacyApi {

    @GET("Drug/GetAllDrugs")
    fun getAllDrugs(): Maybe<Response<List<Drug>>>

    @GET("Drug/GetAllDrugPackages")
    fun getAllDrugPackages(): Maybe<Response<List<DrugPackage>>>

    @GET("Drug/GetDrugRecordByPatientId/{patientId}")
    fun getDrugRecord(@Path("patientId")patientId:String):Maybe<Response<Pharmacy>>

    @POST("Drug/SaveDrugRecord")
    fun save(@Body pharmacy: Pharmacy):Maybe<Response<Pharmacy>>

}