package com.wxsoft.fcare.core.data.remote

import com.wxsoft.fcare.core.data.entity.Response
import com.wxsoft.fcare.core.data.entity.rating.Rating
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface RatingApi {

    /***
     * 评分表列表
     * @param code
     */
    @GET("Rating/GetRatings")
    fun getRatings(): Single<Response<List<Rating>>>

    /***
     * 评分表
     */
    @GET("Rating/GetRatingById/{ratingId}")
    fun getOne(@Path("ratingId")id:String): Single<Response<Rating>>
}