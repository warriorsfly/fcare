package com.wxsoft.fcare.core.data.remote



import com.wxsoft.fcare.core.data.entity.Dictionary
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ShareApi{

//    @GET("Patient/ShareItems/{patientId}")
//    fun getShareItems(@Path("patientId")id:String): Maybe<List<VitalSign>>

    /**
     * 胸痛大诊断
     */
    @GET("EnumDic/enumItems/230")
    fun getShareItems(): Maybe<List<Dictionary>>

    @POST("Patient/ShareItems/{patientId}")
    fun getImageUrl(@Path("patientId")patientId:String, @Body imgType:List<String>): Maybe<Response<String>>
}