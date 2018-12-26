package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.Detour
import com.wxsoft.fcare.data.entity.Response
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DetourApi {

    @GET("Bypass/GetById/{patientId}")
    fun getDetour(@Path("patientId")patientId:String): Flowable<Response<Detour>>

    @POST("Bypass/Save")
    fun save(@Body detour: Detour): Flowable<String>


}