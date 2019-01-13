package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.ElectroCardiogram
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import okhttp3.MultipartBody
import retrofit2.http.*

interface ECGApi {
    @GET("ECG/GetElectroCardiogramByPatientId/{patientId}")
    fun getPatientEcgs(@Path("patientId")id:String):Maybe<Response<List<ElectroCardiogram>>>

    @Multipart
    @POST("ECG/SaveElectroCardiogram")
    fun save(@Part("diogram")diogram: ElectroCardiogram,@Part files: List<MultipartBody.Part>):Maybe<Response<String>>


}