package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.NewTimeLine
import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.TimeQuality
import com.wxsoft.fcare.core.data.entity.WorkOperation
import com.wxsoft.fcare.core.data.entity.lis.LisCr
import com.wxsoft.fcare.core.data.entity.lis.LisItem
import com.wxsoft.fcare.core.data.entity.lis.LisRecord
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface QualityControlApi {

    @GET("QualityControl/GetTimeLineByPatientId/{patientId}")
    fun getTimeLines(@Path("patientId")id:String): Maybe<Response<List<NewTimeLine>>>

    @GET("QualityControl/GetTimeQualityByPatientId/{patientId}")
    fun getQualities(@Path("patientId")id:String): Maybe<Response<List<TimeQuality>>>

    @GET("Patient/GetEmssModules/{patientId}/{currUserId}")
    fun getOperations(@Path("patientId")id:String,@Path("currUserId")userId:String): Maybe<Response<List<WorkOperation>>>

}