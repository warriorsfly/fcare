package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.entity.rating.RatingRecord
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * emr信息通通从这个接口取，和别的接口有重叠，暂时考虑这样 可以防止多次引用各种api
 */
interface EmrApi{

    /**
     * 获取个人Emr列表
     */
    @GET("Patient/GetPreEmssTimeLine/{patientId}")
    fun getPreEmrs(@Path("patientId")patientId:String): Single<Response<List<EmrItem>>>

    @GET("Patient/GetInHospitalEmssTimeLine/{patientId}")
    fun getInEmrs(@Path("patientId")patientId:String): Single<Response<List<EmrItem>>>

    /**
     * 获取个人信息
     */
    @GET("Patient/GetById/{id}")
    fun getBaseInfo(@Path("id")id:String):Single<Response<Patient>>

    /**
     * 获取生命体征列表
     */
    @GET("VitalSigns/GetById/{id}")
    fun getVitals(@Path("id")id:String): Maybe<List<VitalSign>>

    /**
     * 体格检查
     */
    @GET("BodyCheck/GetById/{patientId}")
    fun getBodyCheck(@Path("patientId")patientId:String): Maybe<Response<CheckBody>>

    /**
     * 心电图
     */
    @GET("ECG/GetElectroCardiogramByPatientId/{patientId}/{location}")
    fun getEcgs(@Path("patientId")id:String,@Path("location")location:Int=1):Maybe<Response<ElectroCardiogram>>

    @Multipart
    @POST("ECG/SaveElectroCardiogram")
    fun saveEcg(@Part("diogram")diogram: ElectroCardiogram, @Part files: List<MultipartBody.Part>):Maybe<Response<String>>

    @POST("ECG/Diagnosed")
    fun diagnose(@Body diogram: ElectroCardiogram):Maybe<Response<ElectroCardiogram>>

    //病史
    @GET("MedicalHistory/GetById/{patientId}")
    fun loadMedicalHistory(@Path("patientId")patientId:String): Maybe<Response<MedicalHistory>>

    /***
     * 评分结果列表
     */
    @GET("Rating/GetAnswerRecords/{patientId}")
    fun getRecords(@Path("patientId")id:String): Single<Response<List<RatingRecord>>>

    /***
     * 诊断
     */
    @GET("Diagnosis/GetDiagnosisByPatientId/{patientId}/{location}")
    fun getDiagnosis(@Path("patientId")patientId:String,
                     @Path("location")location:Int): Maybe<Response<Diagnosis>>



    @GET("Measure/GetById/{patientId}")
    fun loadMeasure(@Path("patientId")patientId:String): Maybe<Response<Measure>>
}