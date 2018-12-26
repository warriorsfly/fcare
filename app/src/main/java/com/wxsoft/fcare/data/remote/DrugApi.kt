package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.AnticoagulationDrug
import com.wxsoft.fcare.data.entity.Drug
import com.wxsoft.fcare.data.entity.Patient
import com.wxsoft.fcare.data.entity.Response
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DrugApi {

    @GET("ACS/GetById/{patientId}")
    fun getAcs(@Path("patientId")patientId:String):Flowable<Response<Drug>>

    @POST("ACS/Save")
    fun save(@Body acs: Drug):Flowable<Response<String>>

    @GET("EnumDic/enumItems/{dictId}")
    fun getOtherDrugs(@Path("dictId")dictId:String):Flowable<List<AnticoagulationDrug>>

}