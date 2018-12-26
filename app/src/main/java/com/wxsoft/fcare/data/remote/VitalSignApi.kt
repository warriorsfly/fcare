package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.chest.VitalSign
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import retrofit2.http.*

interface VitalSignApi{

    @POST("VitalSigns")
    fun insert(@Body vitalSign: VitalSign): Maybe<String>

    @PUT("VitalSigns")
    fun update(@Body vitalSign: VitalSign): Maybe<String>

    @GET("VitalSigns/GetById/{id}")
    fun list(@Path("id")id:String): Maybe<List<VitalSign>>
}