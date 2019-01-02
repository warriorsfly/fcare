package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Car
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.GET

interface CarApi {

    /***
     * 车辆列表
     */
    @GET("Car/GetCars")
    fun cars(): Maybe<Response<List<Car>>>
}