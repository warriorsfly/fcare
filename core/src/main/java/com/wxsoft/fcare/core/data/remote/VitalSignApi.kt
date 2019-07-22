package com.wxsoft.fcare.core.data.remote



import com.wxsoft.fcare.core.data.entity.*
import io.reactivex.Maybe
import retrofit2.http.*

interface VitalSignApi{

    @POST("VitalSigns")
    fun insert(@Body vitalSign: VitalSign): Maybe<String>

    @PUT("VitalSigns")
    fun update(@Body vitalSign: VitalSign): Maybe<String>

    @GET("VitalSigns/GetByPatientId/{patientId}")
    fun list(@Path("patientId")id:String): Maybe<List<VitalSign>>

    @GET("VitalSigns/GetByPatientId/{patientId}")
    fun getVitalRecordlist(@Path("patientId")id:String): Maybe<Response<List<VitalSignRecord>>>

    @GET("VitalSigns/GetByPatientId/{patientId}")
    fun getVSRecordlist(@Path("patientId")id:String): Maybe<Response<List<VitalSign>>>

    @GET("VitalSigns/GetById/{drugId}")
    fun getOne(@Path("drugId")id:String): Maybe<Response<VitalSign>>

    @GET("VitalSignsCollectResult/GetByPatientId/{patientId}")
    fun getVitalSignList(@Path("patientId")id:String): Maybe<Response<List<BloodPressureItem>>>

    @POST("VitalSignsCollectResult/Save")
    fun insert(@Body item: BloodPressureItem): Maybe<String>

    @GET("EvaluateResult/GetByPatientId/{patientId}")
    fun getEvaluates(@Path("patientId")id:String): Maybe<Response<List<EvaluateItem>>>

    @POST("EvaluateResult/Save")
    fun insert(@Body item: EvaluateItem): Maybe<Response<String>>

    @POST("VitalSignsCollectResult/GetExcutePlanByPatientId/{patientId}")
    fun getPlan(@Path("patientId")id:String): Maybe<Response<String>>
}