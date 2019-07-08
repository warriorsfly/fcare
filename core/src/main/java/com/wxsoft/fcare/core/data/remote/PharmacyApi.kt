package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.CABG
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.drug.*
import io.reactivex.Maybe
import retrofit2.http.*

interface PharmacyApi {

    @GET("Drug/GetAllDrugs")
    fun getAllDrugs(): Maybe<Response<List<Drug>>>

    @GET("Drug/GetAllDrugPackages")
    fun getAllDrugPackages(): Maybe<Response<List<DrugPackage>>>

    @GET("Drug/GetDrugRecordByPatientId/{patientId}")
    fun getDrugRecord(@Path("patientId")patientId:String):Maybe<Response<List<DrugRecord>>>

    @GET("Drug/GetAcsByPatientId/{patientId}")
    fun getACSDrug(@Path("patientId")patientId:String):Maybe<Response<ACSDrug>>

    @POST("Drug/SaveDrugRecords")
    fun save(@Body drugRecords: List<DrugRecord>):Maybe<Response<List<DrugRecord>>>

    @POST("Drug/SaveAcs")
    fun saveAcs(@Body drug: ACSDrug):Maybe<Response<String>>

    @GET("Drug/GetDrugs/{diagnosisCode}")
    fun loadDrugs(@Path("diagnosisCode")diagnosisCode:String): Maybe<Response<List<DrugTypeitem>>>


    //CABG 不想再写一个Api 就在这里面写了
    @GET("CABG/GetById/{patientId}")
    fun getCABG(@Path("patientId")patientId:String):Maybe<Response<CABG>>

    @POST("CABG/Save")
    fun saveCABG(@Body cabg: CABG):Maybe<Response<String>>

    @DELETE("Drug/DeleteDrugRecordById/{drugId}")
    fun deleteDrug(@Path("drugId")id:String):Maybe<Response<String>>


}