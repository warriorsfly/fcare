package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Ecg
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import okhttp3.MultipartBody
import retrofit2.http.*

interface ECGApi {
    /**
     *
     */
    @GET("ECG/GetElectroCardiogramByPatientId/{patientId}/{location}")
    fun getPatientEcgs(@Path("patientId")id:String,@Path("location")location:Int=2):Maybe<Response<Ecg>>

    @Multipart
    @POST("ECG/SaveElectroCardiogram")
    fun save(@Part("diogram")diogram: Ecg, @Part files: List<MultipartBody.Part>):Maybe<Response<String>>


}