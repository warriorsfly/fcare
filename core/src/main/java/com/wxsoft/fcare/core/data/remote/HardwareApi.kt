package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.EmrItem
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface HardwareApi {
    /**
     * 获取emr时间轴
     */
    @GET("Patient/GetEmr/{patientId}/{currUserId}/{isPreHospital}")
    fun getEmrs(@Path("patientId")patientId:String, @Path("currUserId")userId:String, @Path("isPreHospital")pre:Boolean): Single<Response<List<EmrItem>>>


}