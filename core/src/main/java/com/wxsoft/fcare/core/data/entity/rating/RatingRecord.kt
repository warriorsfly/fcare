package com.wxsoft.fcare.core.data.entity.rating

import com.google.gson.annotations.SerializedName

/**
 * 评分
 */
data class RatingRecord(val id:String="",
                        val patientId:String,
                        val ratingId:String,
                        val ratingName:String,
                        /**
                         * 1低危2中危3高危
                         */
                        @SerializedName("resultLevel")val level:Int=0,
                        var createdDate:String?=null,
                        val score:Int=0){

    @SerializedName("answerRecordDetails")var records:List<SubjectRecord> = emptyList()
}