package com.wxsoft.fcare.core.data.remote



import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.Task
import com.wxsoft.fcare.core.data.entity.User
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApi {

    @GET("Task/get-by-date/{date}")
    fun tasks(@Path("date")date:String): Maybe<Response<List<Task>>>

    @POST("Task/start-new-task")
    fun save(@Body task: Task):Maybe<Response<String>>

    @GET("User/doctors/{accountId}")
    fun getDoctors(@Path("accountId")accountId:String): Maybe<Response<List<User>>>

    @GET("User/nurses/{accountId}")
    fun getNurses(@Path("accountId")accountId:String): Maybe<Response<List<User>>>

    @GET("User/driver/{accountId}")
    fun getDrivers(@Path("accountId")accountId:String): Maybe<Response<List<User>>>


}