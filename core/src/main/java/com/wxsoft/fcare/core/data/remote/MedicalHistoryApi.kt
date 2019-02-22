package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.MedicalHistory
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import okhttp3.MultipartBody
import retrofit2.http.*

interface MedicalHistoryApi {

    @GET("MedicalHistory/GetById/{patientId}")
    fun loadMedicalHistory(@Path("patientId")patientId:String): Maybe<Response<MedicalHistory>>
  @GET("MedicalHistory/GetCommonlyUsedDrugs/{patientId}")
    fun loadDrugHistory(@Path("patientId")patientId:String): Maybe<Response<MedicalHistory>>

    @Multipart
    @POST("MedicalHistory/Save")
    fun save(@Part("medicalHistory")medicalHistory: MedicalHistory, @Part files: List<MultipartBody.Part>):Maybe<Response<String>>

    @Multipart
    @POST("MedicalHistory/Save")
    fun save(@Part("medicalHistory")medicalHistory: MedicalHistory):Maybe<Response<String>>
}