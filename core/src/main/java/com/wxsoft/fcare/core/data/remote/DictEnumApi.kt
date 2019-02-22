package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Dictionary
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

    @GET("EnumDic/enumItems/5")
    fun loadDetour(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/16")
    fun loadThromPlaces(): Maybe<List<Dictionary>>

    /**
     * 非心源ACS
     */
    @GET("EnumDic/enumItems/27")
    fun loadDict27Diagnosis(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/20")
    fun loadConsciousness(): Maybe<List<Dictionary>>

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
     * 二级诊断病种 （根据病人id自动获取对应大病下的二级病种）
     */
    @GET("EnumDic/enumItems/4/{patientId}")
    fun loadSecondTypes(@Path("patientId")id:String): Maybe<List<Dictionary>>

    /**
     * 主诉及症状
     */
    @GET("EnumDic/enumItems/221/{patientId}")
    fun loadComplaints(@Path("patientId")id:String): Maybe<List<Dictionary>>

}