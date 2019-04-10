package com.wxsoft.fcare.core.data.remote



import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.VitalSign
import com.wxsoft.fcare.core.data.entity.VitalSignRecord
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

    @GET("VitalSigns/GetVSByPatientId/{patientId}")
    fun getVSRecordlist(@Path("patientId")id:String): Maybe<Response<List<VitalSign>>>

    @GET("VitalSigns/GetById/{id}")
    fun getOne(@Path("id")id:String): Maybe<Response<VitalSign>>
}