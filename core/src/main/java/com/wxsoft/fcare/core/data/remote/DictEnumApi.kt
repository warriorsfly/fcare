package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.NotifyType
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Tag
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path

interface DictEnumApi {

    @GET("EnumDic/enumItems/1")
    fun loadDictEvaluations(): Maybe<List<Dictionary>>

    /**
     * 胸痛大诊断
     */
    @GET("EnumDic/enumItems/4")
    fun loadDict4Diagnosis(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/5/{patientId}")
    fun loadDetour(@Path("patientId")patientId:String): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/16")
    fun loadThromPlaces(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/{id}")
    fun loadTreatments(@Path("id")id:String): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/234")
    fun loadNoRefushionResons(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/236/{patientId}")
    fun loadCalls(@Path("patientId")id:String): Maybe<List<Dictionary>>

    /**
     * 非心源ACS
     */
    @GET("EnumDic/enumItems/27")
    fun loadDict27Diagnosis(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/28")
    fun loadDict28Diagnosis(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/20/{patientId}")
    fun loadConsciousness(@Path("patientId")id:String): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/238")
    fun loadloadAdress(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/239")
    fun loadHandway(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/246")
    fun loadCardType(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/248")
    fun loadTranstype(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/249")
    fun loadNetHospital(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/250")
    fun loadNYHA(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/253")
    fun loadRoute(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/254")
    fun loadInfarctPosition(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/255")
    fun loadNarrowLevel(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/256")
    fun loadIntracavityImage(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/257")
    fun loadFunctionTest(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/258")
    fun loadBracketNum(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/251")
    fun loadRiskFactors(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/259")
    fun loadComplication(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/224")
    fun loadComplications(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/7")
    fun loadKillip(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/240")
    fun loadPatientOutcom(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/21")
    fun loadTroponinUnit(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/206")
    fun loadCoordinationItems(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/207")
    fun loadSkinItems(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/208")
    fun loadPupilsItems(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/209")
    fun loadResponseLightItems(): Maybe<List<Dictionary>>

    /**
     * 疾病史来源
     */
    @GET("EnumDic/enumItems/210")
    fun loadMedicalHistoryProviderItems(): Maybe<List<Dictionary>>

    /**
     * 疾病史
     */
    @GET("EnumDic/enumItems/211/{patientId}")
    fun loadMedicalHistoryItems(@Path("patientId")id:String): Maybe<List<Dictionary>>
//    fun loadMedicalHistoryItems(): Maybe<List<Dictionary>>

    /**
     * 现场治疗措施
     */
    @GET("EnumDic/enumItems/212")
    fun loadMeasuresItems(): Maybe<List<Dictionary>>

    /**
     * 抢救结果
     */
    @GET("EnumDic/enumItems/213")
    fun loadCureResultItems(): Maybe<List<Dictionary>>

    /**
     * 配合程度
     */
    @GET("EnumDic/enumItems/214")
    fun loadOutcallResultItems(): Maybe<List<Dictionary>>

    /**
     * 疾病大类
     */
    @GET("EnumDic/enumItems/215")
    fun loadDiagnoseTypeResultItems(): Maybe<List<Dictionary>>

    /**
     * 疾病严重程度
     */
    @GET("EnumDic/enumItems/216")
    fun loadIllnessResultItems(): Maybe<List<Dictionary>>

    /**
     * 卒中大诊断
     */
    @GET("EnumDic/enumItems/217")
    fun loadApoplexyResultItems(): Maybe<List<Dictionary>>

    /**
     * 脑梗死小诊断
     */
    @GET("EnumDic/enumItems/218")
    fun loadInfarct(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/219")
    fun loadAssistantTypes(): Maybe<List<Dictionary>>

    /**
     * 并发症
     */
    @GET("EnumDic/enumItems/259/{patientId}")
    fun loadComplication(@Path("patientId")patientId:String): Maybe<List<Dictionary>>

    /**
     * 二级诊断病种 （根据病人id自动获取对应大病下的二级病种）
     */
    @GET("EnumDic/enumItems/4/{patientId}")
    fun loadSecondTypes(@Path("patientId")id:String): Maybe<List<Dictionary>>

    /**
     * 主诉及症状
     */
    @GET("EnumDic/enumItems/221/{patientId}")
    fun loadComplaints(@Path("patientId")id:String): Maybe<List<Dictionary>>

    /**
     * 治疗策略
     */
    @GET("EnumDic/enumItems/14/{patientId}")
    fun loadStrategys(@Path("patientId")id:String): Maybe<List<Dictionary>>

    /**
     * 子集诊断
     */
    @GET("EnumDic/enumChildItems/{currrentEnumItemId}")
    fun loadSonList(@Path("currrentEnumItemId")currrentEnumItemId:String): Maybe<List<Dictionary>>

    /**
     * 常见心电诊断
     */
    @GET("EnumDic/enumItems/227")
    fun loadEcgCommonDiagnoses(): Maybe<List<Dictionary>>

    /**
     * 胸痛用药
     */
    @GET("EnumDic/enumItems/{drugId}")
    fun loadDicts(@Path("drugId")id:String): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/{drugId}/{patientId}")
    fun loadDictsByPatient(@Path("drugId")id:String,@Path("patientId")patientId:String): Maybe<List<Dictionary>>

    /**
     * 发送通知类型
     */
    @GET("Message/GetMessageTemplates")
    fun loadNotifyTypes(): Maybe<Response<List<NotifyType>>>

    /**
     * 腕带列表
     */
    @GET("Patient/GetTags/{currUserHospitalId}")
    fun loadTags(@Path("currUserHospitalId")currUserHospitalId:String): Maybe<Response<List<Tag>>>

    @GET("Patient/UnBindTag/{tagId}")
    fun unbindTag(@Path("tagId")tagId:String):Maybe<Response<String>>

}