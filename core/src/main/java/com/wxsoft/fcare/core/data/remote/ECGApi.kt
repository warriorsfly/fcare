package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.ElectroCardiogram
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.*

interface ECGApi {
    @GET("ECG/GetElectroCardiogramByPatientId/{patientId}")
    fun getPatientEcgs(@Path("patientId")id:String):Maybe<Response<List<ElectroCardiogram>>>

    @POST("ECG/SaveElectroCardiogram")
    fun save(@Body ecg:ElectroCardiogram):Maybe<Response<String>>

}