package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Account
import com.wxsoft.fcare.core.data.entity.Hospital
import com.wxsoft.fcare.core.data.entity.LoginInfo
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountApi {


    @POST("Security")
    fun login(@Body info: LoginInfo): Maybe<Response<Account>>

    @GET("Security/AddOrUpdateJPushAccount/{user}/{pushId}")
    fun jPush(@Path("user")id:String, @Path("pushId")pushId:String): Maybe<Response<String>>

    @GET("User/UpdateHosptial/{userId}/{hospitalId}")
    fun updateHospital(@Path("userId")userId:String, @Path("hospitalId")hospitalId:String): Maybe<Response<Boolean>>

    @GET("User/GetAllHospitals")
    fun getAllHospital(): Maybe<Response<List<Hospital>>>

    @GET("Security/UpdateAccountPwd/{accountId}/{oldPwd}/{newPwd}")
    fun changePassWord(@Path("accountId")id:String, @Path("oldPwd")oldPwd:String, @Path("newPwd")newPwd:String): Maybe<Response<Account>>

    @GET("Patient/DeletePatient/{patientId}/{currUserId}")
    fun deletePatient(@Path("patientId")patientId:String, @Path("currUserId")currUserId:String): Maybe<Response<String>>


}