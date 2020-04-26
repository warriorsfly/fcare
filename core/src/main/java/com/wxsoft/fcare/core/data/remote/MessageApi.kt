package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Measure
import com.wxsoft.fcare.core.data.entity.Message
import com.wxsoft.fcare.core.data.entity.Page
import com.wxsoft.fcare.core.data.entity.Response
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface MessageApi {
    @GET("Message/GetMessages/{userId}/{pageIndex}/{pageSize}")
    fun loadMeasure(@Path("userId")id:String,@Path("userId")index:Int,@Path("userId")size:Int): Maybe<Page<Message>>

    @GET("Message/IgnoreMessage/{msgId}/{currUserId}")
    fun ignoreMessage(@Path("msgId")msgId:String,@Path("currUserId")currUserId:String): Maybe<Response<String>>

}