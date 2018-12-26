package com.wxsoft.fcare.data.remote

import com.wxsoft.fcare.data.entity.chest.Evaluation
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface EvaluationApi {

    @GET("Evaluation/GetEvaluationByPatientId/{id}")
    fun loadPatientEvaluations(@Path("id")id:String): Flowable<List<Evaluation>>


    @POST("Evaluation/SaveEvalations")
    fun addEvaluation(@Body evaluations: List<Evaluation>): Flowable<String>

}