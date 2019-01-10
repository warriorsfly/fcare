package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.EmrItem
import com.wxsoft.fcare.core.data.entity.Patient
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.http.*

interface PatientApi{

    @GET("Patient/GetPatients")
    fun patients():Maybe<Response<List<Patient>>>

    @GET("Patient/GetById/{id}")
    fun getOne(@Path("id")id:String):Single<Response<Patient>>

    @GET("Patient/GetByWristband/{rFid}")
    fun getPatientByRFID(@Path("rFid")rFid:String):Maybe<Response<Patient>>

    @GET("Patient/SetPatientWristband/{patientId}/{wristband_Number}")
    fun bindRfid(@Path("patientId")patientId:String,@Path("wristband_Number")rFid:String):Single<Response<String>>

    @POST("Patient/SavePatientInfo")
    fun save(@Body patient:Patient):Maybe<String>

    @GET("Patient/GetPreEmssTimeLine")
    fun getEmrs(@Query("patientId")patientId:String):Single<Response<List<EmrItem>>>
}