package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.lis.LisCr
import com.wxsoft.fcare.core.data.entity.lis.LisItem
import com.wxsoft.fcare.core.data.entity.lis.LisJCRecord
import com.wxsoft.fcare.core.data.entity.lis.LisType
import io.reactivex.Maybe
import okhttp3.MultipartBody
import retrofit2.http.*

interface LISApi {

    @GET("LIS/GetLisItems")
    fun getLisItems(): Maybe<Response<List<LisItem>>>

    @GET("LIS/GetCrById/{drugId}")
    fun getCrById(@Path("drugId")id:String): Maybe<Response<LisCr>>

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
    fun getPatientLisRecords(@Path("patientId")patientId:String,@Path("lisItemId")lisItemId:String): Maybe<Response<List<LisType>>>

    @GET("YJReport/GetJYResults/{patientId}")
    fun getJYResults(@Path("patientId")patientId:String): Maybe<Response<List<LisType>>>

    @GET("YJReport/GetJCResults/{patientId}")
    fun getJCResults(@Path("patientId")patientId:String): Maybe<Response<List<LisJCRecord>>>


}