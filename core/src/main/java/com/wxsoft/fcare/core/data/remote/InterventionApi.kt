package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.User
import com.wxsoft.fcare.core.data.entity.chest.Intervention
import io.reactivex.Maybe
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface InterventionApi{

    @GET("Intervention/GetById/{patientId}")
    fun getIntervention(@Path("patientId")id:String):Maybe<Response<Intervention>>


    @GET("User/intervention-doctors/{accountId}")
    fun getInterventionDocs(@Path("accountId")accountId:String): Maybe<Response<List<User>>>

    @POST("Intervention/Save")
    fun save(@Body intervention: Intervention):Maybe<Response<String>>
}