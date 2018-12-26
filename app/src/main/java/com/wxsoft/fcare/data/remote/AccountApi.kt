package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.Account
import com.wxsoft.fcare.data.entity.LoginInfo
import com.wxsoft.fcare.data.entity.Response
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountApi {


    @POST("Security")
    fun login(@Body info: LoginInfo): Flowable<Response<Account>>
}