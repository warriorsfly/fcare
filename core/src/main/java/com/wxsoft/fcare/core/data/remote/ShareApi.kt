package com.wxsoft.fcare.core.data.remote



import com.wxsoft.fcare.core.data.entity.Dictionary
import io.reactivex.Maybe
import retrofit2.http.GET

interface ShareApi{

//    @GET("Patient/ShareItems/{patientId}")
//    fun getShareItems(@Path("patientId")id:String): Maybe<List<VitalSign>>

    /**
     * 胸痛大诊断
     */
    @GET("EnumDic/enumItems/230")
    fun getShareItems(): Maybe<List<Dictionary>>
}