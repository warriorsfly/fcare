package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Account
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

    @GET("Security/UpdateAccountPwd/{accountId}/{oldPwd}/{newPwd}")
    fun changePassWord(@Path("accountId")id:String, @Path("oldPwd")oldPwd:String, @Path("newPwd")newPwd:String): Maybe<Response<Account>>
}