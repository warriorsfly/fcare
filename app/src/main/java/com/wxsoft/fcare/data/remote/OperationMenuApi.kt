package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.OperationMenu
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface OperationMenuApi{
    /**
     * 获取病人操作菜单
     * @param id 病人ID
     */
    @GET("NavMenuItem/GetChildren/{id}")
    fun getMenuByPatient(@Path("id")id:String): Maybe<List<OperationMenu>>


    /**
     * @param id 系统代码
     */
    @GET("NavMenuItem/Get/{id}")
    fun getMenuByCode(@Path("id")id:String): Single<List<OperationMenu>>


    /**
     * @param id 系统代码
     */
    @GET("NavMenuItem/GetNavMenItems/{pid}/{uid}")
    fun getMenuByUserAndPatient(@Path("uid")uid:String,@Path("pid")pid:String): Maybe<List<OperationMenu>>



}