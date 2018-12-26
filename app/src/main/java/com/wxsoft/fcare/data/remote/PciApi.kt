package com.wxsoft.fcare.data.remote


import com.wxsoft.fcare.data.entity.Doctor
import com.wxsoft.fcare.data.entity.Pci
import com.wxsoft.fcare.data.entity.Response
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PciApi {

    @GET("PCI/GetById/{patientId}")
    fun getPci(@Path("patientId")patientId:String): Flowable<Response<Pci>>

    @POST("PCI/Save")
    fun save(@Body pci: Pci): Flowable<String>

    @GET("User/GetDoctors/{currUserId}")
    fun getDoctors(@Path("currUserId")currUserId:String): Flowable<Response<List<Doctor>>>

    @GET("User/GetInterventions/{currUserId}")
    fun getInterventionPersons(@Path("currUserId")currUserId:String): Flowable<Response<List<Doctor>>>

    @GET("PCI/StartConduit/{patientId}/{currUserId}/{statrConduitTime}")
    fun statrConduitTime(@Path("patientId")patientId:String,
                        @Path("currUserId")currUserId:String,
                        @Path("statrConduitTime")statrConduitTime:String): Flowable<Response<String>>

    @GET("PCI/ActivateConduit/{patientId}/{currUserId}/{activateConduitTime}")
    fun activateConduitTime(@Path("patientId")patientId:String,
                         @Path("currUserId")currUserId:String,
                         @Path("activateConduitTime")activateConduitTime:String): Flowable<Response<String>>

    @GET("PCI/ArriveConduit/{patientId}/{currUserId}/{arriveConduitTime}")
    fun arriveConduitTime(@Path("patientId")patientId:String,
                            @Path("currUserId")currUserId:String,
                            @Path("arriveConduitTime")arriveConduitTime:String): Flowable<Response<String>>

}