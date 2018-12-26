package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.Dictionary
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
}