package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.EmrLog
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EmrLogApi{
    /**
     * 保存log
     */

    @Deprecated(message = "app不需要提供 log 到服务器")
    @POST("OperationLog/")
    fun log(@Body operation: EmrLog): Flowable<String>

    /**
     * 获取病人的操作日志
     * @param id 病人ID
     */
    @GET("OperationLog/Get/{id}")
    fun getEmrLog(@Path("id")id:String): Flowable<List<EmrLog>>

}