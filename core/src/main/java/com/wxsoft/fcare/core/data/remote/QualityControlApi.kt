package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.*
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface QualityControlApi {

    @GET("QualityControl/GetTimeLineByPatientId/{patientId}")
    fun getTimePoints(@Path("patientId")id:String): Maybe<Response<List<List<TimePoint>>>>

    @GET("QualityControl/GetCanAddTimePointByPatientId/{patientId}")
    fun getAddTimePoints(@Path("patientId")id:String): Maybe<Response<List<TimePoint>>>

    @POST("QualityControl/SaveTimePoint/")
    fun saveTimePoints(@Body point: TimePointChange): Maybe<Response<String>>


    @GET("QualityControl/GetTimeQualityByPatientId/{patientId}")
    fun getQualities(@Path("patientId")id:String): Maybe<Response<List<TimeQuality>>>

    @GET("Patient/GetEmssModules/{patientId}/{currUserId}/{isPreHospital}")
    fun getOperations(@Path("patientId")id:String,@Path("currUserId")userId:String,@Path("isPreHospital")pre:Boolean): Maybe<Response<List<WorkOperation>>>

    @GET("Patient/GetEmssModules/{patientId}/{currUserId}/{isPreHospital}")
    fun getOperationGroups(@Path("patientId")id:String,@Path("currUserId")userId:String,@Path("isPreHospital")pre:Boolean): Maybe<Response<List<Record<WorkOperation>>>>

}