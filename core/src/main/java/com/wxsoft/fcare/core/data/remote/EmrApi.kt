package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.entity.chest.Intervention
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
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
//
//    @Deprecated(replaceWith = ReplaceWith("getEmrs",""),message = "时间轴接口调整，界面部分调试完毕之后这部分代码直接删除")
//    @GET("Patient/GetPreEmssTimeLine/{patientId}")
//    fun getPreEmrs(@Path("patientId")patientId:String): Single<Response<List<EmrItem>>>
//
//    @Deprecated(replaceWith = ReplaceWith("getEmrs",""),message = "时间轴接口调整，界面部分调试完毕之后这部分代码直接删除")
//    @GET("Patient/GetInHospitalEmssTimeLine/{patientId}")
//    fun getInEmrs(@Path("patientId")patientId:String): Single<Response<List<EmrItem>>>

    @GET("Patient/GetEmssTimeLine/{patientId}/{currUserId}/{isPreHospital}")
    fun getEmrs(@Path("patientId")patientId:String,@Path("currUserId")userId:String,@Path("isPreHospital")pre:Boolean): Single<Response<List<EmrItem>>>

    /**
     * 获取个人信息
     */
    @GET("Patient/GetById/{id}")
    fun getBaseInfo(@Path("id")id:String):Single<Response<Patient>>

    /**
     * 获取生命体征列表
     */
    @GET("VitalSigns/GetByPatientId/{id}")
    fun getVitals(@Path("id")id:String): Maybe<Response<List<VitalSign>>>

    @GET("VitalSigns/GetById/{id}")
    fun getVital(@Path("id")id:String): Maybe<Response<VitalSign>>

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

    /**
     * 获取诊断列表
     */
    @GET("Diagnosis/GetDiagnosisByPatientId/{patientId}")
    fun getDiagnosisList(@Path("patientId")id:String): Maybe<Response<List<Diagnosis>>>
    @GET("Measure/GetById/{patientId}")
    fun loadMeasure(@Path("patientId")patientId:String): Maybe<Response<Measure>>

    @GET("Throm/GetByPatientId/{patientId}")
    fun loadThrombolysis(@Path("patientId")patientId:String): Maybe<Response<List<Thrombolysis>>>

    /**
     * 获取知情同意书
     */
    @GET("InformedConsent/GetTalkRecords/{patientId}")
    fun getTalks(@Path("patientId")patientId:String): Maybe<Response<List<Talk>>>

    @GET("Drug/GetDrugRecordByPatientId/{patientId}")
    fun getDrugRecord(@Path("patientId")patientId:String):Maybe<Response<List<DrugRecord>>>

    /**
     * 出院诊断
     */
    @GET("OutHospitalDiagnosis/GetById/{patientId}")
    fun getOtDiagnosis(@Path("patientId")patientId:String): Maybe<Response<DisChargeDiagnosis>>

    @GET("PACS/GetPacsRecordByPatientId/{patientId}")
    fun getPAC(@Path("patientId")patientId:String): Maybe<Response<Pacs>>

    @GET("Intervention/GetById/{patientId}")
    fun getIntervention(@Path("patientId")id:String):Maybe<Response<Intervention>>

    @GET("CABG/GetById/{patientId}")
    fun getCABG(@Path("patientId")patientId:String):Maybe<Response<CABG>>



}