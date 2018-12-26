package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.GpsLocation
import com.wxsoft.fcare.data.entity.Patient
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface GpsApi {

    @POST("Gis/Save")
    fun save(@Body location: GpsLocation): Flowable<String>
}