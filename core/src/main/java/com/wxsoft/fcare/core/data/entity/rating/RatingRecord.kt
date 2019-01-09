package com.wxsoft.fcare.core.data.entity.rating

import com.google.gson.annotations.SerializedName

/**
 * 评分
 */
data class RatingRecord(val id:String,
                        val patientId:String,
                        @SerializedName("scoreSheetId")val ratingId:String,
                        @SerializedName("scoreSheetName")val ratingName:String,
                        /**
                         * 1低危2中危3高危
                         */
                        @SerializedName("riskLevel")val level:Int,
                        @SerializedName("passScore")val score:Int)