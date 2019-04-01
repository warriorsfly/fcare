package com.wxsoft.fcare.core.data.remote.version

import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.version.Version
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path

interface VersionApi{

    @GET("Version/CheckUpdate/{version}")
    fun check(@Path("version")code:Long): Maybe<Response<Version>>
}