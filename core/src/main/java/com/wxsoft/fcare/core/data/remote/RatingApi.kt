package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.rating.Rating
import com.wxsoft.fcare.core.data.entity.rating.RatingRecord
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.http.*

interface RatingApi {

    /***
     * 评分表列表
     * @param patientId
     */
    @GET("Rating/GetRatings/{patientId}")
    fun getRatings(@Path("patientId")patientId:String): Single<Response<List<Rating>>>

    /***
     * 评分表
     */
    @GET("Rating/GetRatingById/{ratingId}")
    fun getOne(@Path("ratingId")id:String): Single<Response<Rating>>

    @POST("Rating/SaveAnswerRecord")
    fun saveRatingResult(@Body record: RatingRecord):Maybe<Response<RatingRecord>>

    /***
     * 评分结果详情
     */
    @GET("Rating/GetAnswerRecordById/{id}")
    fun getOneRecord(@Path("id")id:String): Single<Response<RatingRecord>>

    /***
     * 评分结果列表
     */
    @GET("Rating/GetLastestAnswerRecordByPatientId/{patientId}")
    fun getRecords(@Path("patientId")id:String): Maybe<Response<List<RatingRecord>>>
}