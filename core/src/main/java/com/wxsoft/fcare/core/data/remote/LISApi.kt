package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.lis.LisCr
import com.wxsoft.fcare.core.data.entity.lis.LisItem
import com.wxsoft.fcare.core.data.entity.lis.LisRecord
import io.reactivex.Maybe
import okhttp3.MultipartBody
import retrofit2.http.*

interface LISApi {

    @GET("LIS/GetLisItems")
    fun getLisItems(): Maybe<Response<List<LisItem>>>

    @GET("LIS/GetCrById/{id}")
    fun getCrById(@Path("id")id:String): Maybe<Response<LisCr>>

    @GET("POCT/GetTroponin/{patientId}")
    fun getPoct(@Path("patientId")patientId:String): Maybe<Response<LisCr>>

    @POST("LIS/SaveCr")
    fun saveCr(@Body lisCr: LisCr): Maybe<Response<String>>

    @Multipart
    @POST("POCT/SaveTroponin")
    fun savePoct(@Part("lisCr") lisCr: LisCr): Maybe<Response<String>>

    @Multipart
    @POST("POCT/SaveTroponin")
    fun savePoct(@Part("lisCr")lisCr: LisCr, @Part files: List<MultipartBody.Part>): Maybe<Response<String>>

    @GET("LIS/GetPatientLisRecords/{patientId}/{lisItemId}")
    fun getPatientLisRecords(@Path("patientId")patientId:String,@Path("lisItemId")lisItemId:String): Maybe<Response<List<LisRecord>>>

}