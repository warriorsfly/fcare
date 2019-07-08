package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.InformedConsent
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Talk
import io.reactivex.Maybe
import okhttp3.MultipartBody
import retrofit2.http.*

interface InformedApi {

    @GET("InformedConsent/GetTalkRecords/{patientId}")
    fun getTalkRecords(@Path("patientId")patientId:String): Maybe<Response<List<Talk>>>

    @GET("InformedConsent/GetInformedConsents")
    fun getInformedConsents(): Maybe<Response<List<InformedConsent>>>

    @GET("InformedConsent/GetTalkById/{talkId}")
    fun getTalkById(@Path("talkId")talkId:String): Maybe<Response<Talk>>

    @GET("InformedConsent/GetInformedConsentById/{drugId}")
    fun getInformedConsentById(@Path("drugId")id:String): Maybe<Response<InformedConsent>>

    @GET("InformedConsent/DeleteTalk/{drugId}")
    fun delete(@Path("drugId")id:String): Maybe<Response<String>>

    @Multipart
    @POST("InformedConsent/SaveTalk")
    fun save(@Part("talk")talk: Talk, @Part files: List<MultipartBody.Part>):Maybe<Response<String>>

    @Multipart
    @POST("InformedConsent/SaveTalk")
    fun save(@Part("talk")talk: Talk):Maybe<Response<String>>

}