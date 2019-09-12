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

    @GET("ECG/UpdateECGTime/{patientId}/{dateTime}/{flag}/{location}")
    fun updateECGTime(@Path("patientId")id:String,@Path("dateTime")dateTime:String?,@Path("flag")flag:Int,@Path("location")location:Int=2):Maybe<Response<Int>>

    @GET("ECG/DeleteEcgImage/{drugId}")
    fun deleteImage(@Path("drugId")id:String):Maybe<Response<Ecg>>

    @Multipart
    @POST("ECG/SaveElectroCardiogram")
    fun save(@Part("diogram")diogram: Ecg, @Part files: List<MultipartBody.Part>):Maybe<Response<String>>

    @POST("ECG/Diagnosed")
    fun diagnosed(@Body ecg:Ecg):Maybe<Response<Ecg>>
}