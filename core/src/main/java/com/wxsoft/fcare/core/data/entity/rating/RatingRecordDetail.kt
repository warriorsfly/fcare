package com.wxsoft.fcare.core.data.entity.rating

import com.google.gson.annotations.SerializedName

/**
 * 评分结果（一题一个记录）
 */
data class RatingRecordDetail(val id:String,
                              val patientId:String,
                              @SerializedName("recordId")val recordId:String,
                              @SerializedName("subjectId")val subjectId:String,
                              @SerializedName("myAnswer")val answers:String,
                              @SerializedName("passScore")val score:Int)