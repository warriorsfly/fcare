package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Account
import com.wxsoft.fcare.core.data.entity.LoginInfo
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountApi {


    @POST("Security")
    fun login(@Body info: LoginInfo): Maybe<Response<Account>>
}