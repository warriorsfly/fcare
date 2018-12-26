package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.Patient
import com.wxsoft.fcare.data.entity.Response
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import org.intellij.lang.annotations.Flow
import retrofit2.http.*

interface PatientApi{

    @GET("Patient/GetPatients")
    fun patients():Flowable<Response<List<Patient>>>

    @GET("Patient/GetById/{id}")
    fun getOne(@Path("id")id:String):Single<Response<Patient>>

    @GET("Patient/GetByWristband/{rFid}")
    fun getPatientByRFID(@Path("rFid")rFid:String):Flowable<Response<Patient>>

    @GET("Patient/SetPatientWristband/{patientId}/{wristband_Number}")
    fun bindRfid(@Path("patientId")patientId:String,@Path("wristband_Number")rFid:String):Flowable<Response<String>>

    @POST("Patient/SavePatientInfo")
    fun save(@Body patient:Patient):Flowable<String>
}