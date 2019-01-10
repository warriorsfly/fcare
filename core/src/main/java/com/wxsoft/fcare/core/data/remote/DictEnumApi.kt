package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Dictionary
import io.reactivex.Maybe
import retrofit2.http.GET

interface DictEnumApi {

    @GET("EnumDic/enumItems/1")
    fun loadDictEvaluations(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/4")
    fun loadDict4Diagnosis(): Maybe<List<Dictionary>>

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

    @GET("EnumDic/enumItems/210")
    fun loadMedicalHistoryProviderItems(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/211")
    fun loadMedicalHistoryItems(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/212")
    fun loadMeasuresItems(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/213")
    fun loadCureResultItems(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/214")
    fun loadOutcallResultItems(): Maybe<List<Dictionary>>

    @GET("EnumDic/enumItems/215")
    fun loadDiagnoseTypeResultItems(): Maybe<List<Dictionary>>


}