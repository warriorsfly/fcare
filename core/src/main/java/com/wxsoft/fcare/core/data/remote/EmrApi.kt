package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.*
import com.wxsoft.fcare.core.data.entity.chest.Intervention
import com.wxsoft.fcare.core.data.entity.chest.OutCome
import com.wxsoft.fcare.core.data.entity.drug.ACSDrug
import com.wxsoft.fcare.core.data.entity.drug.DrugRecord
import com.wxsoft.fcare.core.data.entity.lis.LisCr
import com.wxsoft.fcare.core.data.entity.rating.RatingRecord
import com.wxsoft.fcare.core.data.entity.rating.ScencelyRatingResult
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * emr信息通通从这个接口取，和别的接口有重叠，暂时考虑这样 可以防止多次引用各种api
 */
interface EmrApi{
    /**
     * 获取emr时间轴
     */
    @GET("Emr/GetEmrModules/{patientId}/{currUserId}")
    fun getEmrs(@Path("patientId")patientId:String,@Path("currUserId")userId:String): Single<Response<List<EmrItem>>>

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
    fun getEcgs(@Path("patientId")id:String,@Path("location")location:Int=2):Maybe<Response<Ecg>>

    @Multipart
    @POST("ECG/SaveElectroCardiogram")
    fun saveEcg(@Part("diogram")diogram: Ecg, @Part files: List<MultipartBody.Part>):Maybe<Response<String>>

    @POST("ECG/Diagnosed")
    fun diagnose(@Body diogram: Ecg):Maybe<Response<Ecg>>

    //病史
    @GET("MedicalHistory/GetById/{patientId}")
    fun loadMedicalHistory(@Path("patientId")patientId:String): Maybe<Response<MedicalHistory>>

    /***
     * 评分结果列表
     */
    @GET("Rating/GetAnswerRecords/{patientId}")
    fun getRecords(@Path("patientId")id:String): Single<Response<List<RatingRecord>>>

    /***
     * 评分表列表
     * @param patientId
     */
    @GET("Rating/GetSceneWithAnswerRecords/{patientId}")
    fun getScencelyRatings(@Path("patientId")patientId:String): Single<Response<List<ScencelyRatingResult>>>
    /**
     * 获取诊断列表
     */
    @GET("Diagnosis/GetDiagnosisByPatientId/{patientId}")
    fun getDiagnosisList(@Path("patientId")id:String): Maybe<Response<List<DiagnoseRecord>>>

    @GET("Diagnosis/GetLatestDiagnosis/{patientId}")
    fun getLastDiagnose(@Path("patientId")id:String): Maybe<Response<Diagnosis>>
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

    //获取主诉
    @GET("CC/GetCCsByPatientId/{patientId}")
    fun getComplaints(@Path("patientId")patientId:String):Maybe<Response<List<Complain>>>

    //获取治疗策略
    @GET("TreatStrategy/GetById/{patientId}")
    fun getStrategy(@Path("patientId")patientId:String):Maybe<Response<Strategy>>

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

    /**
     * 获取转归信息
     */
    @GET("Outcome/GetById/{patientId}")
    fun getOt(@Path("patientId")patientId:String): Maybe<Response<OutCome>>

    /**
     * acs给药
     */
    @GET("Drug/GetEmrAcsByPatientId/{patientId}")
    fun getACSDrug(@Path("patientId")patientId:String):Maybe<Response<ACSDrug>>

    /**
     * 来院方式
     */
    @GET("ComingWay/GetById/{patientId}/{accoutId}")
    fun getComing(@Path("patientId")patientId:String,@Path("accoutId")accountId:String):Maybe<Response<ComingBy>>

    /**
     * 获取资料图片信息
     */
    @GET("Emr/GetEmrImages/{patientId}")
    fun getEmrImages(@Path("patientId")patientId:String):Maybe<Response<List<EmrRecord>>>
  /**
     * 获取资料图片信息
     */
    @GET("/api/Emr/DeleteImage/{typeId}/{attachmentId}")
    fun deleteImage(@Path("typeId")typeId:String,@Path("attachmentId")imageId:String):Maybe<Response<String>>

    /**
     *保存
     */
    @Multipart
    @POST("Emr/SaveEmrImages")
    fun savingImages(@Part("eimage")image: EmrRecord, @Part files: List<MultipartBody.Part>):Maybe<Response<String>>

    @GET("POCT/GetTroponin/{patientId}")
    fun getPoct(@Path("patientId")patientId:String): Maybe<Response<LisCr>>
}