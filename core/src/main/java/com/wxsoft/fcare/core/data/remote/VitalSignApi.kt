package com.wxsoft.fcare.core.data.remote



import com.wxsoft.fcare.core.data.entity.VitalSign
import io.reactivex.Maybe
import retrofit2.http.*

interface VitalSignApi{

    @POST("VitalSigns")
    fun insert(@Body vitalSign: VitalSign): Maybe<String>

    @PUT("VitalSigns")
    fun update(@Body vitalSign: VitalSign): Maybe<String>

    @GET("VitalSigns/GetById/{id}")
    fun list(@Path("id")id:String): Maybe<List<VitalSign>>
}