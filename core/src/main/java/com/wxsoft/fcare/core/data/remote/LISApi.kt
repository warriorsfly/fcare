package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.lis.LisCr
import com.wxsoft.fcare.core.data.entity.lis.LisItem
import com.wxsoft.fcare.core.data.entity.lis.LisRecord
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LISApi {

    @GET("LIS/GetLisItems")
    fun getLisItems(): Maybe<Response<List<LisItem>>>

    @GET("LIS/GetCrById/{id}")
    fun getCrById(@Path("id")id:String): Maybe<Response<LisCr>>

    @POST("LIS/SaveCr")
    fun saveCr(@Body lisCr: LisCr): Maybe<Response<String>>

    @GET("LIS/GetPatientLisRecords/{patientId}/{lisItemId}")
    fun getPatientLisRecords(@Path("patientId")patientId:String,@Path("lisItemId")lisItemId:String): Maybe<Response<List<LisRecord>>>

}