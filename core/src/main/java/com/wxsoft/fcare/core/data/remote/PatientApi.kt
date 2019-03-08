package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.*
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface PatientApi{

    @GET("Patient/GetPatients")
    fun patients():Maybe<Response<List<Patient>>>

    @GET("Patient/GetPaged/{keyWord}/{pageIndex}/{pageSize}")
    fun getPagedPatients(@Path("keyWord") keyword:String, @Path("pageIndex")index:Int, @Path("pageSize")size:Int):Maybe<Page<Patient>>

    @GET("Patient/GetById/{id}")
    fun getOne(@Path("id")id:String):Single<Response<Patient>>

    @GET("Patient/GetByWristband/{rFid}")
    fun getPatientByRFID(@Path("rFid")rFid:String):Maybe<Response<Patient>>

    @GET("Patient/SetPatientWristband/{patientId}/{wristband_Number}")
    fun bindRfid(@Path("patientId")patientId:String,@Path("wristband_Number")rFid:String):Single<Response<String>>

    @Multipart
    @POST("Patient/SavePatientInfo")
    fun save(@Part("patient")patient: Patient,@Part files: List<MultipartBody.Part>):Observable<Response<String>>

    @Multipart
    @POST("Patient/SavePatientInfo")
    fun save(@Part("patient")patient: Patient):Observable<Response<String>>

    @GET("QualityControl/GetQualityIndexByPatientId/{patientId}")
    fun getQualityIndexByPatientId(@Path("patientId")patientId:String):Maybe<Response<Quality>>

    @POST("Patient/GetPaged")
    fun getPatients(@Body item: PatientsCondition):Maybe<Page<Patient>>

}