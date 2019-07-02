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

    @GET("Patient/GetServerDateTime")
    fun getServerDateTime():Maybe<Response<String>>

    @GET("Patient/GetByKeyword/{keyword}/{user}")
    fun searchPatients(@Path("keyword")keyword:String,@Path("user")userId:String):Maybe<Response<List<Patient>>>

    @GET("Patient/DeleteImage/{attachmentId}")
    fun deleteImage(@Path("attachmentId")attachmentId:String):Maybe<Response<String>>

    @GET("Patient/GetPaged/{keyWord}/{pageIndex}/{pageSize}")
    fun getPagedPatients(@Path("keyWord") keyword:String, @Path("pageIndex")index:Int, @Path("pageSize")size:Int):Maybe<Page<Patient>>

    @GET("Patient/GetPatientFromHisByBlh/{blh}/{type}")
        fun getPatientInfo(@Path("blh") blh:String, @Path("type")type:Int):Maybe<Response<Patient>>

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

    //医生已读通知
    @GET("Message/ReceiverHandleNotice/{msgId}/{currUserId}/{currUserName}/{patientName}")
    fun areadyRead(@Path("msgId")msgId:String,@Path("currUserId")currUserId:String,@Path("currUserName")currUserName:String,@Path("patientName")patientName:String):Single<Response<String>>

    @GET("Patient/GetPatientsFromCis/{currUserId}")
    fun getPatientsFromCis(@Path("currUserId")id:String):Maybe<Response<List<Patient>>>

//    @POST("Patient/GetPatientsFromCis")
    @POST("Patient/SaveStrock120")
    fun saveStrock120(@Body strock:Strock120):Maybe<Response<String>>

}