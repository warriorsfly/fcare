package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Car
import io.reactivex.Single
import retrofit2.http.GET

interface CarApi {

    /***
     * 车辆列表
     */
    @GET("Car/GetCars")
    fun cars():Single<List<Car>>
}