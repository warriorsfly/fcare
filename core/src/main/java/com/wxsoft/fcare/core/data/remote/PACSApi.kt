package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Complain
import com.wxsoft.fcare.core.data.entity.Pacs
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Strategy
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PACSApi {

    @GET("PACS/GetPacsRecordByPatientId/{patientId}")
    fun getPAC(@Path("patientId")patientId:String): Maybe<Response<Pacs>>

    @POST("PACS/Save")
    fun savePAC(@Body pacs : Pacs): Maybe<Response<String>>

    @GET("PACS/NoticeStartCT/{patientId}/{currUserId}")
    fun notice(@Path("patientId")id:String,@Path("currUserId")accountId:String):Maybe<Response<String>>

    /*
     *保存主诉
     */
    @POST("CC/SavePatientCCs")
    fun saveCC(@Body ccs : List<Complain>): Maybe<Response<String>>

    @GET("CC/GetCCsByPatientId/{patientId}")
    fun getCCs(@Path("patientId")patientId:String): Maybe<Response<List<Complain>>>

    /*
     *获取治疗策略
     */
    @GET("TreatStrategy/GetById/{patientId}")
    fun getStrategy(@Path("patientId")patientId:String): Maybe<Response<Strategy>>

    @POST("TreatStrategy/Save")
    fun saveStrategy(@Body ccs : Strategy): Maybe<Response<String>>

}