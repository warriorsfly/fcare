package com.wxsoft.fcare.core.data.remote



import com.wxsoft.fcare.core.data.entity.*
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApi {

    @GET("Task/get-by-date/{date}")
    fun tasks(@Path("date")date:String): Maybe<Response<List<Task>>>

    @GET("Task/get-by-id/{id}")
    fun task(@Path("id")date:String): Single<Response<Task>>

    @POST("Task/start-new-task")
    fun save(@Body task: Task):Maybe<Response<String>>

    @GET("User/doctors/{accountId}")
    fun getDoctors(@Path("accountId")accountId:String): Maybe<Response<List<User>>>

    @GET("User/nurses/{accountId}")
    fun getNurses(@Path("accountId")accountId:String): Maybe<Response<List<User>>>

    @GET("User/driver/{accountId}")
    fun getDrivers(@Path("accountId")accountId:String): Maybe<Response<List<User>>>

    @GET("Task/arrive/{id}")
    fun arrive(@Path("id")taskId:String): Maybe<Response<String>>

    @GET("Task/met/{id}")
    fun met(@Path("id")taskId:String): Maybe<Response<String>>

    @GET("Task/return/{id}")
    fun returning(@Path("id")taskId:String): Maybe<Response<String>>
    
    @GET("Task/arrive-hos/{id}")
    fun arriveHos(@Path("id")taskId:String): Maybe<Response<String>>
    @GET("Task/CancelTask/{id}/{user}/{reason}")
    fun cancel(@Path("id")taskId:String,@Path("user")user:String,@Path("reason")reason:String): Maybe<Response<String>>

    @POST("Task/GetPaged")
    fun getTasks(@Body item: PatientsCondition):Maybe<Page<Task>>

    @GET("EnumDic/enumItems/226/{patientId}")
    fun getDicts(@Path("patientId")id:String): Maybe<List<Dictionary>>

    @GET("Task/UpdateTaskTime/{id}/{status}/{time}")
    fun change(@Path("id")taskId:String,@Path("status")status:Int,@Path("time")time:String): Maybe<Response<String>>
}