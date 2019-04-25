package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.hardware.LepuDetection
import com.wxsoft.fcare.core.data.entity.hardware.MindrayDetection
import com.wxsoft.fcare.core.data.entity.hardware.MindrayDevices
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path

interface HardwareApi {
    /**
     * 获取生命体征设备列表
     */
    @GET("Device/GetMindrayDevices")
    fun getMindrayDevices(): Maybe<List<MindrayDevices>>

    /**
     * 获取生命体征设备数据
     */
    @GET("Device/GetMindrayDetection/{deviceId}")
    fun getVital(@Path("deviceId")deviceId:String): Maybe<MindrayDetection>

    /**
     * 获取肌钙蛋白数据
     */
    @GET("Device/GetLepuDetection/{NoOrName}")
    fun getJGDB(@Path("NoOrName")NoOrName:String): Maybe<LepuDetection>



}