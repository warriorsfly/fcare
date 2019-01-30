package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.CABG
import com.wxsoft.fcare.core.data.entity.Pharmacy
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.drug.Drug
import com.wxsoft.fcare.core.data.entity.drug.DrugPackage
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
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
    fun getDrugRecord(@Path("patientId")patientId:String):Maybe<Response<List<DrugRecord>>>

    @POST("Drug/SaveDrugRecords")
    fun save(@Body drugRecords: List<DrugRecord>):Maybe<Response<List<DrugRecord>>>


    //CABG 不想再写一个Api 就在这里面写了
    @GET("CABG/GetById/{patientId}")
    fun getCABG(@Path("patientId")patientId:String):Maybe<Response<CABG>>

    @POST("CABG/Save")
    fun saveCABG(@Body cabg: CABG):Maybe<Response<String>>

}