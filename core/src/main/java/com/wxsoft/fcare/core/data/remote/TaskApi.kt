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
    fun task(@Path("id")id:String): Single<Response<Task>>

    @POST("Task/start-new-task")
    fun save(@Body task: Task):Maybe<Response<String>>

    @GET("User/doctors/{accountId}")
    fun getDoctors(@Path("accountId")accountId:String): Maybe<Response<List<User>>>

    @GET("User/nurses/{accountId}")
    fun getNurses(@Path("accountId")accountId:String): Maybe<Response<List<User>>>

    @GET("User/driver/{accountId}")
    fun getDrivers(@Path("accountId")accountId:String): Maybe<Response<List<User>>>
    /**
     * 获取接诊医生
     */
    @GET("User/GetEmergencyDepartmentDoctor/{patientId}")
    fun getEmergencyDoctor(@Path("patientId")patientId:String): Maybe<Response<List<User>>>

    /**
     * 获取接诊护士
     */
    @GET("User/GetEmergencyDepartmentNurse/{patientId}")
    fun getEmergencyNurse(@Path("patientId")patientId:String): Maybe<Response<List<User>>>

    /**
     * 获取会诊医生
     */
    @GET("User/GetConsultantDoctor/{accountId}/{patientId}")
    fun getConsultantDoctor(@Path("accountId")accountId:String,@Path("patientId")patientId:String): Maybe<Response<List<User>>>

    @GET("User/GetUserFromHisByKeyword/{keyword}/{isDoctor}/{accountId}")
    fun searchUser(@Path("keyword")keyword:String,@Path("isDoctor")isDoctor:Boolean,@Path("accountId")accountId:String): Maybe<Response<List<User>>>

    @GET("User/GetEmergencyDepartmentDoctorAndNurse/{currUserId}/{diagnoseType}")
    fun getSignUsers(@Path("currUserId")currUserId:String,@Path("diagnoseType")diagnoseType:String): Maybe<Response<List<User>>>



    @GET("Task/arrive/{drugId}/{time}")
    fun arrive(@Path("drugId")taskId:String,@Path("time")time:String): Maybe<Response<String>>

    @GET("Task/met/{drugId}/{time}")
    fun met(@Path("drugId")taskId:String,@Path("time")time:String): Maybe<Response<String>>

    @GET("Task/return/{drugId}/{time}")
    fun returning(@Path("drugId")taskId:String,@Path("time")time:String): Maybe<Response<String>>

    @GET("Task/GetByKeyword/{keyword}")
    fun searchTasks(@Path("keyword")keyword:String): Maybe<Response<List<Task>>>
    
    @GET("Task/arrive-hos/{drugId}/{time}")
    fun arriveHos(@Path("drugId")taskId:String,@Path("time")time:String): Maybe<Response<String>>
    @GET("Task/CancelTask/{drugId}/{user}/{reason}")
    fun cancel(@Path("drugId")taskId:String,@Path("user")user:String,@Path("reason")reason:String): Maybe<Response<String>>

    @POST("Task/GetPaged")
    fun getTasks(@Body item: PatientsCondition):Maybe<Page<Task>>

    @GET("Task/ChoosePatient/{accountId}")
    fun getPatientsForChoose(@Path("accountId")id:String): Maybe<Response<List<Patient>>>

    @GET("Task/SetPatientToTask/{taskId}/{patientId}")
    fun taskChoosePatient(@Path("taskId")taskId: String,@Path("patientId")patientId: String): Maybe<Response<String>>

    @GET("EnumDic/enumItems/226/{patientId}")
    fun getDicts(@Path("patientId")id:String): Maybe<List<Dictionary>>

    @GET("Task/UpdateTaskTime/{drugId}/{status}/{time}")
    fun change(@Path("drugId")taskId:String,@Path("status")status:Int,@Path("time")time:String): Maybe<Response<String>>
}