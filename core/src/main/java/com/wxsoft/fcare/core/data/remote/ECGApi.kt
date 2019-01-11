package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.ElectroCardiogram
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path

interface ECGApi {
    @GET("ECG/GetElectroCardiogramByPatientId/{patientId}")
    fun getPatientEcgs(@Path("patientId")id:String):Maybe<Response<ElectroCardiogram>>
}